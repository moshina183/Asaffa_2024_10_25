/* global Swal, SMW, url */

SMW.moveToVehicles = {
  _serviceMvToVehicles: "rest-api/OrderToVehicle/confirmItems",
  _serviceVehData: "rest-api/OrderToVehicle/items",

  init: function () {
    SMW.moveToVehicles.loadData();
    SMW.moveToVehicles.bindEvents();
  },

  bindEvents: function () {
    this.unbindEvents();
    $("#btnItemsTransact").hide();
    $("#btnItemsTransactCancel").hide();
    $("#BTN_ITEMS_VEH_CONFIRM").on("click", this.onBtnItemsConfirmClick);
    $("#BTN_ITEMS_VEH_CANCEL").on("click", this.onBtnItemsCancelClick);
    $("#btnItemsTransact").on("click", this.onBtnItemsTransactClick);
    $("#btnItemsTransactCancel").on(
      "click",
      this.onBtnItemsTransactCancelClick
    );
    $("#btnItemsMovVehPrint").on("click", this.onBtnItemsMovVehPrint);
    $("#vehiclesPrintContainer").on(
      "keyup",
      ".mtv-input",
      this.onQuantityKeyUp
    );
  },

  unbindEvents: function () {
    $("#BTN_ITEMS_VEH_CONFIRM").unbind();
    $("#BTN_ITEMS_VEH_CANCEL").unbind();
    $("#btnItemsTransact").unbind();
    $("#btnItemsTransactCancel").unbind();
    $("#btnItemsMovVehPrint").unbind();
    $("#vehiclesPrintContainer").unbind();
  },

  onBtnItemsMovVehPrint: function () {
    SMW.mobWebsite.printPage("#VehBtnsContainer");
  },

  onBtnItemsConfirmClick: function () {
    var enteredInputsCount = 0;
    $(".mtv-input").each(function () {
      if ($.trim($(this).val()).length == 0 || $(this).val() == 0) {
        $(this).closest("tr").addClass("hide-cls");
      } else {
        enteredInputsCount++;
        $(this).addClass("disabled-cls");
      }
    });
    var error = $("<div class='smw-error'/>");
    if (enteredInputsCount == 0) {
      $(".smw-error").empty();
      error
        .html("Please enter data for atleast 1 item")
        .insertAfter("#itemsVehicleTable")
        .show()
        .delay(5000)
        .fadeOut();
      $(".hide-cls").removeClass("hide-cls");
      $("#itemsVehicleTable .mtv-input").removeClass("disabled-cls");
    } else {
      $(".smw-error").empty();
      $(".hide-cls").hide();
      $(".disabled-cls").attr("disabled", "disabled");
      $("#btnItemsTransact").show();
      $("#btnItemsTransactCancel").show();
      $("#btnItemsMovVehPrint").hide();
      $("#BTN_ITEMS_VEH_CONFIRM").hide();
      $("#BTN_ITEMS_VEH_CANCEL").hide();
    }
  },

  onQuantityKeyUp: function () {
    var data = $.trim($(this).val());
    var regx = /^\d{0,4}(\.\d{0,3})?$/;
    if (data != null && data != "") {
      if ($.isNumeric(data) == false || data.match(regx) == null) {
        var error = $("<div class='smw-error smw-rightalign'/>");
        var parentTr = $(this).closest("tr");
        $(".smw-error").empty();
        error
          .html("Invalid item quantity! ")
          .insertBefore(parentTr)
          .show()
          .delay(5000)
          .fadeOut();
        $(this).val("");
      }
    }
  },
  onBtnItemsTransactClick: function () {
    //send data to server to store
    if ($("#itemsVehicleTable .mtv-tr:visible").length > 0) {
      var dataArr = new Array();
      $("#itemsVehicleTable .mtv-tr:visible").each(function () {
        var eachObj = {};
        var id = $(this).find(".mtv-input").attr("id");
        var quantity = $(this).find(".mtv-input").val();
        var headerId = $(this).find(".mtv-input").attr("data-headerId");
        eachObj.itemId = id;
        eachObj.quantity = quantity;
        eachObj.headerId = headerId;
        dataArr.push(eachObj);
      });
      //make ajax call to server to store data.
      var data = JSON.stringify({
        customerName: null,
        transactionType: "MOVE_ORDER",
        customerSiteId: 0,
        customerSite: null,
        paymentMethod: null,
        userName: localStorage.getItem("userName"), // need to be added from the cookie
        orgId: localStorage.getItem("organizationId"), // need to be added from the cookie
        custAccountId: 0,
        transactionTypeId: 0,
        subInvCode: null,
        transType: "MOVE_ORDER",
        totalAmount: 0.0,
        locationId: null,
        items: dataArr,
      });
      SMW.ajax.call(
        url + SMW.moveToVehicles._serviceMvToVehicles,
        data,
        SMW.moveToVehicles.onProcessSuccess,
        true
      );
      $("#itemsVehicleTable").hide();
      $("#btnItemsTransact").hide();
      $("#btnItemsMovVehPrint").hide();
      $("#btnItemsTransactCancel").hide();
      $('button[name="btnItemsCancel"]').hide();
    }
  },

  // updated on 20-02-2019 - Balaji Manickam - 4i Apps
  // Showing success message after material is successfully processed!!!
  onProcessSuccess: function (data) {
    $.mobile.changePage("#PAGE_HOME");
    Swal.fire({
      title: "Material Request From Stores",
      text: `Your material request number is ${data.orderNumber}`,
      type: "success",
      showConfirmButton: true,
      showCancelButton: true,
      cancelButtonText: "Print",
    }).then((result) => {
      if (!result.value) {
        SMW.moveToVehicles.onPrintMaterialRequestOrder(data.orderNumber);
      }
    });
  },

  onBtnItemsTransactCancelClick: function () {
    $(".hide-cls").show().removeClass("hide-cls");
    $("#itemsVehicleTable .mtv-input")
      .removeAttr("disabled")
      .removeClass("disabled-cls");
    $("#btnItemsTransact").hide();
    $("#btnItemsTransactCancel").hide();
    $("#btnItemsMovVehPrint").hide();
    $("#BTN_ITEMS_VEH_CONFIRM").show();
    $("#BTN_ITEMS_VEH_CANCEL").show();
  },

  loadData: function () {
    var username = localStorage.getItem("userName");
    var data = {};
    SMW.ajax.call(
      url +
        "rest-api/OrderToVehicle/items?userName=" +
        localStorage.getItem("userName"),
      data,
      SMW.moveToVehicles.onLoadDataSuccess,
      true
    );
    $("#itemsVehicleTable").show();
    $("#BTN_ITEMS_VEH_CONFIRM").show();
    $("#BTN_ITEMS_VEH_CANCEL").show();
  },

  onLoadDataSuccess: function (data) {
    SMW.moveToVehicles.drawTable(data);
  },

  drawTable: function drawTable(data) {
    $("#itemsVehicleTable").empty();
    if (data.length != 0) {
      $("#CONTENT_MOV_TO_VEH").css("display", "block");
      $("#ERROR_MOV_TO_VEH").css("display", "none");
      $("#itemsVehicleTable").append(
        "<tr><th style='display:none'>Id</th> <th>Description</th> <th>Quantity</th></tr>"
      );
      for (var i = 0; i < data.length; i++) {
        SMW.moveToVehicles.drawRow(data[i]);
      }
    } else {
      $("#CONTENT_MOV_TO_VEH").css({ display: "none" });
      $("#ERROR_MOV_TO_VEH").css("display", "block");
      $("#ERROR_MOV_TO_VEH").html("<h5>No Data found</h5");
    }
  },

  drawRow: function (rowData) {
    var row = $("<tr class='mtv-tr'/>");
    $("#itemsVehicleTable").append(row); //this will append tr element to table... keep its reference for a while since we will add cels into it
    row.append($("<td style='display:none'>" + rowData.itemId + "</td>"));
    row.append($("<td align=left>" + rowData.description + "</td>"));
    row.append(
      $(
        "<td> <input type='text' class='mtv-input' data-headerId='" +
          rowData.headerId +
          "' id='" +
          rowData.itemId +
          "' min='1' max='9999'></input></td>"
      )
    );
  },

  onPrintMaterialRequestOrder: function (orderNumber) {
    SMW.ajax.showLoader();
    $.ajax({
      type: "POST",
      url: url + "/rest-api/PrintOnMaterial/orders?orderNumber=" + orderNumber,
      dataType: "json",
      traditional: true,
      success: function (data, textStatus, jqXHR) {
        if (data.printInvoiceFlag === "T") {
          window.open(SMW.orp + orderNumber + ".pdf", "_blank");
        } else {
          Swal.fire({
            icon: "error",
            title: "Oops...",
            text: `Encountered error while running On Material Request!`,
          });
        }
        SMW.ajax.hideLoader();
      },
      error: function (data, textStatus, jqXHR) {
        SMW.ajax.hideLoader();
      },
    });
  },
};

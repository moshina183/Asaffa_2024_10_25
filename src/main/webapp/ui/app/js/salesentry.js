/* global error, url, SMW, Swal */
var inputFromReview = {};

SMW.mobWebsite.salesEntry = {
  init: async function () {
    $("#manualInvoiceCreateDate").val("");
    $("#manualInvoiceCreate").val("");
    $("#refNo").val("");
    SMW.ajax.showLoader();
    await SMW.mobWebsite.salesEntry.loadCustomerData();
    await SMW.mobWebsite.salesEntry.loadRentalKnockOff();
    await SMW.mobWebsite.salesEntry.salesEntryInit();
    await SMW.mobWebsite.salesEntry.bindEvents();
    SMW.ajax.hideLoader();
  },
  // Step 3
  salesEntryInit: function () {
    $("#invoicedataError").empty();
    $("#payment").html("");
    $("#refNo").empty();
    $("#refNo").html("");
    // Dinesh-VAT
    // $("#vanVat").html("");
    $("#locationAddress").html("");
    $("#rentalKnockOffMeaning").html("");
    $("#itemsOrderReturn").empty();
  },

  bindEvents: function () {
    this.unbindEvents();
    $("#manualInvoiceCreate").on("keyup", this.onInvoiceKeyUp);
    $("#manualInvoiceCreate").on("blur", this.invoiceCheck);
    $("#refNo").on("change", this.onRefNoCheck);
    $("#itemsOrderReturn").on("change", ".ord-input", this.onQuantityKeyUp);
    $("#rentalKnockOff").on("change", this.clearSaleEntryTable);
    $("#cust").on("change", getCustomerSite);
    $("#custsite").on("change", this.getLocationAddress);
    $("#btn_Invoice_Review").on("tap", onSalesBtnItemsReviewClick);
    $("#btn_Invoice_Confirm").on("tap", confirmSave);
  },
  unbindEvents: function () {
    $("#manualInvoiceCreate").unbind();
    $("#refNo").unbind();
    $("#itemsOrderReturn").unbind();
    $("#rentalKnockOff").unbind();
    $("#cust").unbind();
    $("#custsite").unbind();
    $("#btn_Invoice_Review").unbind();
    $("#btn_Invoice_Confirm").unbind();
  },
  onRefNoCheck: function () {
    var refNoVal = $("#refNo").val();
    if(refNoVal != null || refNoVal != ""){
      $.post(url + "rest-api/SalesEntry/referencNo", {
        referenceNum: $("#refNo").val(),
      }).done(function (data, textStatus, jqXHR) {
        if (data) {
          //alert("Entered Reference No already exists. ");
          $("#refNo").val("");
          Swal.fire({
            title: "Oops!!!",
            html:
              "<p>" +
              "Entered Reference No already exists." +
              "</p>",
            type: "error",
            showConfirmButton: false,
            timer: 2000,
            animation: false,
            customClass: "animated heartBeat",
          });
        }
        });
    }
  },
  invoiceCheck: function () {
    var invoiceNumber = $.trim($("#manualInvoiceCreate").val());
    if (invoiceNumber == null || invoiceNumber == "") {
      $("#manualInvoiceCreateDate").val("");
      $("#manualInvoiceCreateDate").prop("disabled", true);
    } else {
      $("#manualInvoiceCreateDate").prop("disabled", false);
    }
  },
  onInvoiceKeyUp: function () {
    var data = $.trim($("#manualInvoiceCreate").val());
    var regx = /^\d{0,4}(\d{0,3})?$/;
    if (data != null && data != "") {
      if ($.isNumeric(data) == false || data.match(regx) == null) {
        var error = $("<div class='smw-error smw-rightalign'/>");
        var parentTr = $(this).closest("tr");
        //error.empty();
        $(".smw-error").empty();
        var error = $("<div class='smw-error'/>");
        $(".smw-error").empty();
        error
          .html("Invalid Amount Format! ")
          .insertBefore("#manualInvoiceCreate")
          .show()
          .delay(5000)
          .fadeOut();
        error
          .html("Please enter only numbers! ")
          .insertBefore("#manualInvoiceCreate")
          .show()
          .delay(5000)
          .fadeOut();
        $("#manualInvoiceCreate").val("");
      }
    }
  },
  loadTransType: function () {
    var trans = $("#transType").empty();
    $.post(url + "rest-api/invTransactionTypes", function () {}).done(function (
      data,
      textStatus,
      jqXHR
    ) {
      SMW.mobWebsite.salesEntry.transType(data);
    });
  },
  transType: function (data) {
    var appenddata = "";
    $.each(data, function (key, value) {
      appenddata +=
        "<option value = '" +
        value.transactionId +
        "'>" +
        value.transactionName +
        "</option>";
    });
    $("#transType").html(appenddata);
  },
  // Step 2:
  loadRentalKnockOff: async function () {
    $("#manualInvoiceCreateDate").datepicker({
      dateFormat: "dd-M-yy",
    });
    var maxDate = new Date();
    $("#manualInvoiceCreateDate").datepicker("option", "maxDate", maxDate);
    $("#loadRentalKnockOff").empty();
    var rentalKnockOff = $("#rentalKnockOff").empty();
    rentalKnockOff.append("<option value='Select Rental Type'>Select</option>");
    await $.post(url + "rest-api/rentalKnockOff", function () {}).done(
      function (data, textStatus, jqXHR) {
        SMW.mobWebsite.salesEntry.rentalKnockOff(data);
      }
    );
  },
  rentalKnockOff: function (data) {
    var appenddata = "";
    $.each(data, function (key, value) {
      appenddata +=
        "<option value = '" +
        value.rentalKnockOffCode +
        "'>" +
        value.rentalKnockOffDesc +
        "</option>";
    });
    $("#rentalKnockOff").html(appenddata);
    var rentalKnockOffMeaning = $("#rentalKnockOff option:selected").val();
    $("#rentalKnockOffMeaning").html(rentalKnockOffMeaning);
  },
  clearSaleEntryTable: function () {
    $("#itemsOrderReturn").empty();
    $("#custsite").val("0");
  },
  getLocationAddress: function () {
    $("#locationAddress").empty();
    $("#locationAddress").html($("#addressLoc").val());
    getPaymentMethod();

  },
  // Step 1:
  loadCustomerData: async function () {
    await $.post(
      url + "/rest-api/customers?userName=" + localStorage.getItem("userName"),
      function () {}
    ).done(function (data, textStatus, jqXHR) {
      SMW.mobWebsite.salesEntry.getCustomer(data);
    });
  },
  getCustomer: function (data) {
    $("#cust").empty();
    var appenddata = "";
    appenddata =
      "<option value='0' selected='selected'>Select Customer Name</option>";
    $.each(data, function (key, value) {
      appenddata +=
        "<option value = '" +
        value.customerId +
        "'>" +
        value.customerName +
        "</option>";
    });
    $("#cust").html(appenddata);
  },
  onQuantityKeyUp: function () {
    var data = $.trim($(this).val());
    var regx = /^\d{0,4}(\.\d{0,3})?$/;
    if (data != null && data != "") {
      var error = $("<div class='smw-error smw-rightalign'/>");
      var parentTr = $(this).closest("tr");
      $(".smw-error").empty();
      if ($.isNumeric(data) == false || data.match(regx) == null) {
        error
          .html("Invalid item quantity! ")
          .insertBefore(parentTr)
          .show()
          .delay(5000)
          .fadeOut();
        $(this).val("");
      } else {
        var stockQty = parentTr.find("#qtyOnHand").val().replace("/", "");
        var actualVal = parseFloat(stockQty);
        var enteredVal = parseFloat(data);
        
        //var actualVal = parseInt(stockQty);
        //var enteredVal = parseInt(data);
        
        if (enteredVal > actualVal) {
          var result = actualVal + 1;
          if (!(Number.isInteger(v) && Number.isInteger(increment))) {
            result = parseFloat(result.toFixed(10)); // No need to round, return directly for integers
          }
          error
            .html("Item quantity should be less than " + (result))
            .insertBefore(parentTr)
            .show()
            .delay(5000)
            .fadeOut();
          $(this).val("");
        } else {
          checkDiscounted($(this));
        }
      }
    }
  },
};

function checkDiscounted(event) {
  if ($("#rentalKnockOff option:selected").val() === "VSINVOICEKO") {
    var itemId = event.attr("data-itemId");
    var customerId = $("#cust option:selected").val();
    var siteId = $("#custsite option:selected").val();
    var orgId = localStorage.getItem("organizationId");
    SMW.ajax.showLoader();
    $.post(
      url +
        "rest-api/SalesEntry/item/check/discount?userName=" +
        localStorage.getItem("userName") +
        "&customerId=" +
        customerId +
        "&partySiteId=" +
        siteId +
        "&itemId=" +
        itemId +
        "&organizationId=" +
        orgId
    ).done(function (data, textStatus, jqXHR) {
      SMW.ajax.hideLoader();
      if (data) {
        var error = $("<tr class='smw-error'/>");
        var parentTr = event.closest("tr");
        error
          .html("-ve discount not allowed with spl promotion")
          .insertBefore(parentTr)
          .show()
          .delay(5000)
          .fadeOut();
        event.val("");
      }
    });
  }
}

function getCustomerSite() {
  SMW.ajax.showLoader();
  $("#itemsOrderReturn").empty();
  $.post(url + "/rest-api/customerSites", {
    customerId: $("#cust option:selected").text(),
    userName: localStorage.getItem("userName"),
  }).done(function (data, textStatus, jqXHR) {
    SMW.ajax.hideLoader();
    getCustomerSiteData(data);
  });
}

function getCustomerSiteData(data) {
  var appenddata = "";
  appenddata += "<option value = '0'>Select</option>";
  $.each(data, function (key, value) {
    if (value.address == null) {
      var error = $("<div class='smw-error'/>");
      $(".smw-error").empty();
      error
        .html(
          "No Location Map to Customer ID :" +
            $("#cust").val() +
            " Please contact to Administrator"
        )
        .insertAfter("#cust")
        .show()
        .delay(5000)
        .fadeOut();
    } else {
      appenddata +=
        "<option value = '" +
        value.locationId +
        "'>" +
        value.address +
        "</option>";
      $(
        "<input type='hidden' id='addressLoc' value=" + value.addressLoc + "/>"
      );
    }
  });
  $("#custsite").html(appenddata);
}

function getPaymentMethod() {
  SMW.ajax.showLoader();
  $("#itemsOrderReturn").empty();
  $.post(url + "/rest-api/paymentMethod?", {
    locationId: $("#custsite").val(),
  }).done(function (data, textStatus, jqXHR) {
    SMW.ajax.hideLoader();
    getPayment(data);
  });
}

function getPayment(data) {
  $("#payment").empty();
  $("#payment").html(data.method);
  // getVatMethod();
  rowData();
}
// Dinesh VAt JS
// function getVatMethod() {
//   SMW.ajax.showLoader();
//   $("#itemsOrderReturn").empty();
//   $.post(url + "/rest-api/vatValue?", {
//     userName: localStorage.getItem("userName"),
//     custName: $("#cust option:selected").text()
//   }).done(function (data, textStatus, jqXHR) {
//     SMW.ajax.hideLoader();
//     getVatRate(data);
//   });
// }

// function getVatRate(data) {
//   $("#vanVat").empty();
//   $("#vanVat").html(data.vatRate);
// }

function rowData() {
  SMW.ajax.showLoader();
  var customerId = $("#cust option:selected").val();
  var locationId = $("#custsite option:selected").val();
  $.post(
    url +
      "rest-api/SalesEntry/items?userName=" +
      localStorage.getItem("userName") +
      "&customerId=" +
      customerId +
      "&partySiteId=" +
      locationId +
      "&organizationId=" +
      localStorage.getItem("organizationId")
  ).done(function (data, textStatus, jqXHR) {
    SMW.ajax.hideLoader();
    drawTable(data);
  });
}

function drawTable(data) {
  if (data.length == 0) {
    $("#itemsOrderReturn").empty();
    var row = $("<tr class='odr-tr'/>");
    $("#itemsOrderReturn").append(row); // this will append tr element to
    row.append($("<td colspan='2'>No Data found</td>"));
    $("#btn_Invoice_Confirm").hide();
  } else {
    $("#btn_Invoice_Confirm").show();
    $("#itemsOrderReturn").empty();
    var row = $(
      "<tr class='odr-tr'><th>Description</th><th>Quantity</th></tr>"
    );
    $("#itemsOrderReturn").append(row);
    for (var i = 0; i < data.length; i++) {
      drawRow(data[i]);
    }
  }
}

function drawRow(rowData) {
  var row = $("<tr class='odr-tr'/>");
  $("#itemsOrderReturn").append(row); // this will append tr element to
  row.append($("<td id='description'>" + rowData.description + "<br/></td>"));
  if (rowData.price == 0 || rowData.priceCnt == 0) {
    row.append(
      $(
        "<td> <input type='text' class='ord-input' data-headerId='" +
          rowData.headerId +
          "' data-linevat='" +
          rowData.perVATRate +
          "' id='" +
          rowData.itemId +
          "' min='1' max='9999' disabled='disabled'></input></td>"
      )
    );

  } else {
    row.append(
      $(
        "<td> <input type='text' class='ord-input' data-itemId='" +
          rowData.itemId +
          "' data-headerId='" +
          rowData.headerId +
          "' data-linevat='" +
          rowData.perVATRate +
          "' id='" +
          rowData.itemId +
          "' min='1' max='9999'></input></td>"
      )
    );
  }
  row.append(
    $("<input type='hidden' id='itemPrice' value=" + rowData.price + "/>")
  );
  row.append(
    $(
      "<input type='hidden' id='qtyOnHand' value=" +
        rowData.quantityOnHand +
        "/>"
    )
  );
}

function displayTable(data) {
  for (var i = 0; i < data.length; i++) {
    drawRow(data[i]);
  }
}

function displayRow(rowData) {
  var row = $("<tr class='odr-tr'/>");
  $("#itemsOrderReturn").append(row); // this will append tr element to
  row.append($("<td>" + rowData.description + "</td>"));
  row.append($("<td>" + rowData.quantity + "</td>"));
}

function onSalesBtnItemsConfirmClick() {
  var enteredInputsCount = 0;
  var quantyCounter = 0;
  var quantyFlag = false;
  if ($("#itemsOrderReturn .odr-tr:visible").length > 0) {
    $("#itemsOrderReturn .odr-tr:visible").each(function () {
      if (
        $.trim($(this).find(".ord-input").val()).length === 0 ||
        $(this).find(".ord-input").val() === 0
      ) {
        $(this).closest("tr").addClass("hide-cls");
      } else {
        enteredInputsCount++;
        $(this).addClass("disabled-cls");
      }
      if (
        $(this).find(".ord-input").val() >
        parseFloat($(this).find("#qtyOnHand").val())
      ) {
        quantyFlag = true;
        quantyCounter++;
      }
      if (quantyFlag) {
        var error = $("<div class='smw-error'/>");
        $(".smw-error").empty();
        error
          .html(
            "Please enter quantity less than on hands qty." +
              parseFloat($(this).find("#qtyOnHand").val())
          )
          .insertAfter("#itemsOrderReturn")
          .show()
          .delay(3000)
          .fadeOut();
        $(".hide-cls").removeClass("hide-cls");
        $("#itemsOrderReturn .ord-input").removeClass("disabled-cls");
      }
    });
  }
  if (enteredInputsCount === 0) {
    var error = $("<div class='smw-error'/>");
    $(".smw-error").empty();
    error
      .html("Please enter data for atleast 1 item")
      .insertAfter("#itemsOrderReturn")
      .show()
      .delay(3000)
      .fadeOut();
    $(".hide-cls").removeClass("hide-cls");
    $("#itemsOrderReturn .ord-input").removeClass("disabled-cls");
  } else if (quantyCounter === 0) {
    save();
  }
}

function save() {
  SMW.ajax.showLoader();
  var manualInvoiceCreateDate = $("#manualInvoiceCreateDate").val();
  var manualInvoiceCreate = $("#manualInvoiceCreate").val();
  var refNoVal = $("#refNo").val();
  var data;
  if ($("#itemsOrderReturn .odr-tr:visible").length > 0) {
    var dataArr = new Array();
    $("#itemsOrderReturn .odr-tr:visible").each(function () {
      var eachObj = {};
      var quantity = $(this).find(".ord-input").val();
      var headerId = $(this).find(".ord-input").attr("data-headerId");
      var perVATRate = $(this).find(".ord-input").attr("data-linevat");
      var description = $(this).find("#description").text();
      var itemPrice = $(this).find("#itemPrice").val();
      eachObj.quantity = quantity;
      eachObj.headerId = headerId;
      eachObj.perVATRate = perVATRate;
      eachObj.description = description;
      eachObj.price = parseFloat(itemPrice);
      if ($.trim(quantity).length != 0 && quantity > 0) {
        dataArr.push(eachObj);
      }
    });
    $.ajax({
      type: "POST",
      url:
        url +
        "rest-api/SalesEntry/confirmItems?manualInvoiceCreateDate=" +
        manualInvoiceCreateDate +
        "&manualInvoiceCreate=" +
        manualInvoiceCreate +
        "&refNoParam=" +
        inputFromReview.refNoVal,
      data: (vehicleDetails = JSON.stringify({
        transactionType: $("#transType").text(),
        customerName: $("#cust option:selected").text(),
        customerSiteId: $("#custsite").val(),
        customerSite: $("#custsite option:selected").text(),
        paymentMethod: $("#payment").text(),
        userName: localStorage.getItem("userName"), // need to be added from the cookie
        orgId: localStorage.getItem("organizationId"), // need to be added from the cookie
        custAccountId: $("#cust").val(),
        transactionTypeId: "1041",
        subInvCode: null,
        transType: "ORDER",
        totalAmount: 0.0,
        rentalKnockOff: $("#rentalKnockOff option:selected").text(),
        referenceNo : $("#refNo").val(),
        items: dataArr,
        // vatRate:$("#vanVat").text()
      })),
      dataType: "json",
      contentType: "application/json; charset=utf-8",
      traditional: true,
      success: function (data) {
        localStorage.orderReturnsInvoice = data.invoiceNum;
        localStorage.customerSite = $("#custsite option:selected").text();
        SMW.mobWebsite.salesEntry.confirmartion.init();
        if (data.result == false) {
          SMW.ajax.hideLoader();
          $("#invoicedataError").html("Error getting data");
        } else {
          $.mobile.changePage("#PAGE_INVOICE_ENTRY_CONFIRMATION");
        }
        SMW.ajax.hideLoader();
      },
      error: function (xhr, a, b) {
        SMW.ajax.hideLoader();
      },
    });
  }
}

function confirmSave() {
  var refNoVal = $("#refNo").val();
  if (inputFromReview.custOption != undefined) {
    SMW.ajax.showLoader();
    $.ajax({
      type: "POST",
      url:
        url +
        "rest-api/SalesEntry/confirmItems?manualInvoiceCreateDate=" +
        inputFromReview.manualInvoiceCreateDate +
        "&manualInvoiceCreate=" +
        inputFromReview.manualInvoiceCreate +
        "&refNoParam=" +
        inputFromReview.refNoVal,
      data: (vehicleDetails = JSON.stringify({
        transactionType: inputFromReview.transTypeOption,
        customerName: inputFromReview.custOption,
        customerSiteId: inputFromReview.custSiteVal,
        customerSite: inputFromReview.custSiteOption,
        paymentMethod: inputFromReview.paymentText,
        vatRate:inputFromReview.vatRateText,
        userName: localStorage.getItem("userName"), // need to be added from the cookie
        orgId: localStorage.getItem("organizationId"), // need to be added from the cookie
        custAccountId: inputFromReview.custVal,
        transactionTypeId: inputFromReview.transTypeVal,
        referenceNo: inputFromReview.refNoVal,
        subInvCode: null,
        transType: "ORDER",
        totalAmount: 0.0,
        rentalKnockOff: inputFromReview.rentalKnockOffVal,
        items: inputFromReview.reviewItems,
      })),
      dataType: "json",
      contentType: "application/json; charset=utf-8",
      traditional: true,
      success: function (data) {
        localStorage.orderReturnsInvoice = data.invoiceNum;
        localStorage.customerSite = inputFromReview.custSiteOption;
        SMW.mobWebsite.salesEntry.confirmartion.init();
        if (data.result === false) {
          SMW.ajax.hideLoader();
          var error = $("<div class='smw-error'/>");
          $(".smw-error").empty();
          error
            .html("Error getting Data!!!")
            .insertAfter("#reviewdataError")
            .show()
            .delay(3000)
            .fadeOut();
          $(".hide-cls").removeClass("hide-cls");
          $("#reviewdataError .ord-input").removeClass("disabled-cls");
        } else {
          // Clear Review Form
          SMW.mobWebsite.salesEntry.review.clearReviewForm();
          // Clear old values
          inputFromReview = {};

          $.mobile.changePage("#PAGE_INVOICE_ENTRY_CONFIRMATION");
        }
        SMW.ajax.hideLoader();
      },
      error: function (xhr, a, b) {
        SMW.ajax.hideLoader();
      },
    });
  } else {
    var error = $("<div class='smw-error'/>");
    $(".smw-error").empty();
    error
      .html("Not accepted an empty value!!!")
      .insertAfter("#reviewdataError")
      .show()
      .delay(3000)
      .fadeOut();
    $(".hide-cls").removeClass("hide-cls");
    $("#reviewdataError .ord-input").removeClass("disabled-cls");
  }
}
// dinesh - logic need to modify
function onSalesBtnItemsReviewClick() {
  var refNoVal = $("#refNo").val();
  var enteredInputsCount = 0;
  var quantyCounter = 0;
  
  if ($("#itemsOrderReturn .odr-tr:visible").length > 0) {
    $("#itemsOrderReturn .odr-tr:visible").each(function () {
      if (
        $.trim($(this).find(".ord-input").val()).length === 0 ||
        $(this).find(".ord-input").val() === 0
      ) {
        $(this).closest("tr").addClass("hide-cls");
      } else {
        enteredInputsCount++;
        $(this).addClass("disabled-cls");
      }
      if (
        $(this).find(".ord-input").val() >
        parseFloat($(this).find("#qtyOnHand").val())
      ) {
        quantyCounter++;
      }
    });
    $.post(url + "rest-api/SalesEntry/invoiceNo", {
      invoiceNum: $("#manualInvoiceCreate").val().replace(/\ /g, ""),
    }).done(function (data, textStatus, jqXHR) {
      if (data) {
        var messageText;
        $.post(
          url + "lookup/message-text?messageName=" + "VANSALES INVOICE EXIST"
        ).done(function (data) {
          Swal.fire({
            title: "Oops!!!",
            html:
              "<p>" +
              data[1].messageText +
              "</P><br/><p>" +
              data[0].messageText +
              "</p>",
            type: "error",
            showConfirmButton: false,
            timer: 2000,
            animation: false,
            customClass: "animated heartBeat",
          });
        });
      } else if (enteredInputsCount === 0) {
        var error = $("<div class='smw-error'/>");
        $(".smw-error").empty();
        error
          .html("Please enter data for atleast 1 item")
          .insertAfter("#itemsOrderReturn")
          .show()
          .delay(3000)
          .fadeOut();
        $(".hide-cls").removeClass("hide-cls");
        $("#itemsOrderReturn .ord-input").removeClass("disabled-cls");
      } else if (quantyCounter === 0) {
        showReview();
      }
    });
  }
}

function showReview() {
  SMW.ajax.showLoader();
  // send data to server to store
  inputFromReview.manualInvoiceCreateDate = $("#manualInvoiceCreateDate").val();
  inputFromReview.manualInvoiceCreate = $("#manualInvoiceCreate")
    .val()
    .replace(/\ /g, "");
  inputFromReview.refNoVal = $("#refNo").val();
  if ($("#itemsOrderReturn .odr-tr:visible").length > 0) {
    var dataArr = new Array();
    $("#itemsOrderReturn .odr-tr:visible").each(function () {
      var eachObj = {};
      var quantity = $(this).find(".ord-input").val();
      var headerId = $(this).find(".ord-input").attr("data-headerId");
      var perVATRate = $(this).find(".ord-input").attr("data-linevat");
      var description = $(this).find("#description").text();
      var itemPrice = $(this).find("#itemPrice").val();
      eachObj.quantity = quantity;
      eachObj.headerId = headerId;
      eachObj.perVATRate = perVATRate;
      eachObj.description = description;
      eachObj.price = parseFloat(itemPrice);
      if ($.trim(quantity).length !== 0 && quantity > 0) {
        dataArr.push(eachObj);
      }
    });
    $.ajax({
      type: "POST",
      url:
        url +
        "rest-api/SalesEntry/ReviewItems?manualInvoiceCreateDate=" +
        inputFromReview.manualInvoiceCreateDate +
        "&manualInvoiceCreate=" +
        inputFromReview.manualInvoiceCreate +
        "&refNoParam=" +
        inputFromReview.refNoVal,
      data: (vehicleDetails = JSON.stringify({
        transactionType: $("#transType").text(),
        customerName: $("#cust option:selected").text(),
        customerSiteId: $("#custsite").val(),
        customerSite: $("#custsite option:selected").text(),
        paymentMethod: $("#payment").text(),
        userName: localStorage.getItem("userName"), // need to be added from the cookie
        orgId: localStorage.getItem("organizationId"), // need to be added from the cookie
        custAccountId: $("#cust").val(),
        transactionTypeId: "1041",
        subInvCode: null,
        transType: "ORDER",
        totalAmount: 0.0,
        rentalKnockOff: $("#rentalKnockOff option:selected").text(),
        referenceNo: $("#refNo").text(),
        items: dataArr,
        // vatRate:$("#vanVat").val()
        // vatRate:$("#vanVat").text()
      })),
      dataType: "json",
      contentType: "application/json; charset=utf-8",
      traditional: true,
      success: function (data) {
        localStorage.orderReturnsInvoice = data.invoiceNum;
        localStorage.customerSite = $("#custsite option:selected").text();
        SMW.mobWebsite.salesEntry.review.init();
        if (data.result === false && data.balanceFlag == true) {
          SMW.ajax.hideLoader();
          $("#invoicedataError").html("Error getting data");
        }
        else if (data.balanceFlag == false) {
          SMW.ajax.hideLoader();
          var balance;
          if(data.creditBalance == null){
            balance = 0.00;
          }
          else{
            balance = data.creditBalance;
          }
            alert("Total amount exceeds the Credit limit. Available balance: "+balance);
        } 
        else {
          $.mobile.changePage("#PAGE_INVOICE_ENTRY_REVIEW");
        }
        inputFromReview.transTypeOption = $("#transType").text();
        inputFromReview.custOption = $("#cust option:selected").text();
        inputFromReview.custSiteVal = $("#custsite").val();
        inputFromReview.custSiteOption = $("#custsite option:selected").text();
        inputFromReview.paymentText = $("#payment").text();
        // inputFromReview.vatRateText = $("#vanVat").text();
        inputFromReview.custVal = $("#cust").val();
        inputFromReview.transTypeVal = "1041";
        inputFromReview.rentalKnockOffVal = $(
          "#rentalKnockOff option:selected"
        ).text();
        inputFromReview.reviewItems = dataArr;
        SMW.ajax.hideLoader();
      },
      error: function (xhr, a, b) {
        SMW.ajax.hideLoader();
      },
    });
  }
}

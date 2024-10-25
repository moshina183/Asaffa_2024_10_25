/* global SMW, Swal, url */

var time = {};
SMW.mobWebsite = {
  // Scoped variables
  _serviceOrgDetails: "rest-api/organizations",
  init: function () {
    $.extend($.mobile.zoom, { locked: true, enabled: false });
    $(document).on("mobileinit ready", SMW.mobWebsite.onmobWebSiteInit);
  },
  onmobWebSiteInit: function () {
    SMW.mobWebsite.bindEvents();
  },
  // --one more more event config
  bindEvents: function () {
    this.unbindEvents();
    $("#BTN_LOGIN").on("tap", SMW.mobWebsite.onBtnLoginTap);
    $("#INVOICE_ENTRY").on("tap", SMW.mobWebsite.onInvoiceEntryTap);
    $("#ORDERS_RETURNS").on("tap", SMW.mobWebsite.onOrdersReturnTap);
    $("#MOVE_ORDER_INVENTORY").on(
      "tap",
      SMW.mobWebsite.onMoveOrderInventoryTap
    );
    $("#MOVE_ORDER_VEHICLE").on("tap", SMW.mobWebsite.onMoveOrderVehichleTap);
    $("#ONHAND_QUANTITY").on("tap", SMW.mobWebsite.onOnHandQuaantityTap);
    $("#EOD_SALES_REPORT").on("tap", SMW.mobWebsite.onPrintVanSalesReport);
    $("#SALES_INVOICE_BROWSER").on("tap", SMW.mobWebsite.onSalesInvoiceBrowser);
    $(".smw-logout").on("tap", SMW.mobWebsite.onBtnLogoutTap);
    $(".smw-home").on("tap", SMW.mobWebsite.onBtnHomeTap);
    $(".smw-cancel").on("tap", SMW.mobWebsite.onBtnHomeTap);
    $("#BTN_ITEMS_VEH_CANCEL").on("tap", SMW.mobWebsite.onBtnHomeTap);
    $('button[name="btnItemsCancel"]').on("tap", SMW.mobWebsite.onBtnHomeTap);
  },
  // --off the component
  unbindEvents: function () {
    $("#BTN_LOGIN").unbind();
    $("#INVOICE_ENTRY").unbind();
    $("#ORDERS_RETURNS").unbind();
    $("#MOVE_ORDER_INVENTORY").unbind();
    $("#MOVE_ORDER_VEHICLE").unbind();
    $("#ONHAND_QUANTITY").unbind();
    $("#EOD_SALES_REPORT").unbind();
    $("#SALES_INVOICE_BROWSER").unbind();
    $(".smw-logout").unbind();
    $(".smw-home").unbind();
    $(".smw-cancel").unbind();
    $("#BTN_ITEMS_VEH_CANCEL").unbind();
    $('button[name="btnItemsCancel"]').unbind();
  },
  //--logout
  // localStoreage-? 
  onBtnLogoutTap: function () {
    $.mobile.changePage("#PAGE_LOGIN");
    localStorage.clear();
  },
  // --home page
  // 
  onBtnHomeTap: function () {
    SMW.mobWebsite.salesEntry.review.clearReviewForm();
    console.log($(this));
    if ($(this).hasClass("noHome")) {
      return;
    } else if (
      $(this)[0].form != null &&
      $(this)[0].form.id == "invoice_review_form"
    ) {
      $.mobile.changePage("#PAGE_INVOICE_ENTRY");
      return;
    }
    $.mobile.changePage("#PAGE_HOME");
  },
  // 
  confirmartion: function () {
    $.mobile.changePage("#PAGE_INVOICE_ENTRY_CONFIRMATION");
  },
  // getBaseUrl()--?
  callOrgDetails: function () {
    var data = {};
    SMW.ajax.callCTypeErrorCallBack(
      getBaseUrl() + SMW.mobWebsite._serviceOrgDetails,
      data,
      SMW.mobWebsite.onCallOrgDetailsSuccess,
      true,
      "application/json; charset=utf-8",
      function () {
        $("<div class='smw-error'/>")
          .text("Request processing failed, Please contact administrator")
          .insertBefore("#smw-username");
      }
    );
  },
  onCallOrgDetailsSuccess: function (resp) {
    var data = resp;
    $("#orgdropdown").empty();
    $("#orgdropdown").append(
      $("<option></option>").attr("value", 0).text("Please select organization")
    );
    for (var i = 0; i < data.length; i++) {
      $("#orgdropdown").append(
        $("<option></option>")
          .attr("value", data[i].organizationId)
          .text(data[i].organizationName)
      );
    }
  },
  // updated on 14-02-2019 - Balaji Manickam - 4i Apps
  // Added option to check current time of server
  onBtnLoginTap: function () {
    SMW.ajax.showLoader();
    $.get(url + "user/login/time/")
      .done(function (data) {
        if (data.hour == null || data.minute == null) {
          SMW.mobWebsite.doProceedToLogin();
        } else {
          SMW.mobWebsite.getBusinessHours(data.hour, data.minute);
        }
      })
      .fail(function (data) {
        SMW.ajax.hideLoader();
      });
  },
  getBusinessHours: async function (currentHour, currentMin) {
    $.get(url + "user/login/time/business-hours")
      .done(function (data) {
        if (data.startTime == null || data.endTime == null) {
          SMW.mobWebsite.doProceedToLogin();
        } else {
          time.startTimeHour = data.startTime.substring(
            0,
            data.startTime.indexOf(":")
          );
          time.startTimeMin = data.startTime.substring(
            data.startTime.indexOf(":") + 1
          );
          time.endTimeHour = data.endTime.substring(
            0,
            data.endTime.indexOf(":")
          );
          time.endTimeMin = data.endTime.substring(
            data.endTime.indexOf(":") + 1
          );
          time.startTime =
            time.startTimeHour < 13
              ? data.startTime + "AM"
              : time.startTimeHour - 12 + ":" + time.startTimeMin + "PM";
          time.endTime =
            time.endTimeHour < 13
              ? data.endTime + "AM"
              : time.endTimeHour - 12 + ":" + time.endTimeMin + "PM";
          if (
            (currentHour > time.startTimeHour &&
              currentHour < time.endTimeHour) ||
            (currentHour == time.startTimeHour &&
              currentHour < time.endTimeHour &&
              currentMin >= time.startTimeMin) ||
            (currentHour == time.endTimeHour &&
              currentHour > time.startTimeHour &&
              currentMin <= time.endTimeMin) ||
            (currentHour == time.startTimeHour &&
              currentHour == time.endTimeHour &&
              currentMin >= time.startTimeMin &&
              currentMin <= time.endTimeMin)
          ) {
            SMW.mobWebsite.doProceedToLogin();
            SMW.ajax.hideLoader();
          } else {
            var startTime = time.startTime;
            var endTime = time.endTime;
            $.post(
              url + "lookup/message-text?messageName=" + "VANSALES LOGIN"
            ).done(function (data) {
              Swal.fire({
                title: "Sorry!",
                html:
                  "<p>" +
                  data[1].messageText +
                  " " +
                  startTime +
                  "-" +
                  endTime +
                  "</P><br/><p>" +
                  data[0].messageText +
                  " " +
                  startTime +
                  "-" +
                  endTime +
                  "</p>",
                animation: false,
                customClass: "animated swing",
                type: "warning",
              });
              SMW.ajax.hideLoader();
            });
          }
        }
        time = {};
      })
      .fail(function (data) {
        SMW.ajax.hideLoader();
      });
  },
  doProceedToLogin: async function () {
    localStorage.custName = $("#smw-username").val();
    var uid = $("#smw-username").val();
    var pwd = $("#smw-password").val();
    var orgid = $("#orgdropdown").val();
    var dataLogin = { username: uid, password: pwd, organizationId: orgid };
    var username = $("#smw-username").val();
    var password = $("#smw-password").val();
    var organization = $("#orgdropdown").val();
    var characterReg = /^([a-zA-Z_0-9@\!#\$\^%&*()+=\-\\\';,\.\/\{\}\|\":<>\?]{1,})$/;
    var errDiv = "<div class='smw-error'/>";
    if (!characterReg.test(username)) {
      $(".smw-error").empty();
      $(errDiv)
        .html("Please enter username")
        .insertBefore("#smw-username")
        .delay(10000)
        .fadeOut();
      return false;
    }
    if (password === "") {
      $(".smw-error").empty();
      $(errDiv)
        .html("Please enter password")
        .insertBefore("#smw-password")
        .delay(10000)
        .fadeOut();
      return false;
    }
    await SMW.ajax.callCType(
      getBaseUrl() + "user/login",
      dataLogin,
      SMW.mobWebsite.onBtnJoinSuccess,
      true,
      "application/x-www-form-urlencoded"
    );
  },
  onBtnJoinSuccess: function (resp) {
    $("#userName").html(localStorage.custName);
    var errDiv1 = "<div class='smw-error'/>";
    if (!resp.valid) {
      if (resp.userName === null) {
        $(errDiv1)
          .html("Invalid credentials")
          .insertBefore("#smw-username")
          .delay(10000)
          .fadeOut();
      }
    } else if (resp.salesManName === null) {
      $(errDiv1)
        .html(
          "Sales Man Name Not Attached to the vehicles:" +
            resp.userName +
            " Please contact to Administrator"
        )
        .insertBefore("#smw-username")
        .delay(10000)
        .fadeOut();
    } else {
      localStorage.setItem("organizationId", resp.organizationId);
      localStorage.setItem("userId", resp.userId);
      localStorage.setItem("userName", resp.userName);
      localStorage.setItem("salesManName", resp.salesManName);
      $.mobile.changePage("#PAGE_HOME");
    }
  },
  onPrintVanSalesReport: function () {
    $.mobile.changePage("#PAGE_VAN_SALES_BROWSER");
    SMW.vanSalesReport.init();
    $("#SALES_INVOICE").empty();
  },
  onSalesInvoiceBrowser: function (e) {
    e.preventDefault();
    $.mobile.changePage("#PAGE_SALES_INVOICE_BROWSER");
    SMW.salesInvoiceBrowser.init();
    $("#SALES_INVOICE").empty();
  },
  onInvoiceEntryTap: function (e) {
    e.preventDefault();
    $.mobile.changePage("#PAGE_INVOICE_ENTRY");
    SMW.mobWebsite.salesEntry.init();
    $("#cust").val("0");
    $("#custsite").val("0");
  },
  onOrdersReturnTap: function (e) {
    e.preventDefault();
    $.mobile.changePage("#PAGE_ORDERS_RETURN");
    SMW.mobWebsite.OrdersAndReturns.init();
    $("#SELECT_ORDERS_RETURN_CUST").val("0");
    $("#SELECT_ORDERS_RETURN_CUST_SITE").val("0");
  },
  onMoveOrderInventoryTap: function (e) {
    e.preventDefault();
    $.mobile.changePage("#PAGE_MOVE_ORDER_INVENTORY");
    SMW.moveToInventory.init();
    $("#smw-ddloc").val("0");
  },
  onMoveOrderVehichleTap: function (e) {
    e.preventDefault();
    $.mobile.changePage("#PAGE_MOVE_ORDER_VEHICLE");
    SMW.moveToVehicles.init();
    $("#itemsVehicleTable").empty();
  },
  onOnHandQuaantityTap: function (e) {
    e.preventDefault();
    $.mobile.changePage("#PAGE_ONHAND_QUANTITY");
    SMW.onHandQuantity.init();
    $("#itemsMoveTable").empty();
  },
  showError: function (head, desc) {
    $.mobile.loading("hide");
    var cpg = $.mobile.activePage;
    var errObj = $("<div>");
    var errBtn = $("<div>")
      //.attr('href', '#')
      .attr("data-role", "button")
      .attr("data-icon", "delete")
      .attr("data-iconpos", "notext")
      .addClass("ui-btn-right")
      .html("Close")
      .appendTo(errObj);
    errBtn.button();
    errBtn.on("click", function () {
      errObj.popup("close").remove();
    });
    var errObjHeader = $("<h4>").appendTo(errObj);
    var errObjDesc = $("<div>").appendTo(errObj);
    errObj.css("padding", "15px");
    errObjHeader.html(head);
    errObjDesc.html(desc);
    errObj.appendTo(cpg);
    errObj.popup().popup("open");
  },
  printPage: function (selector) {
    window.open("http://192.168.1.207:8080/invoice/SampleInvoicePrint.html");
  },
};

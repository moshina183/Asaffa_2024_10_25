/* global SMW, url, Swal */

SMW.mobWebsite.OrdersAndReturns = {
    init: function () {
        SMW.mobWebsite.OrdersAndReturns.hideButton();
        SMW.mobWebsite.OrdersAndReturns.loadTransData();
        SMW.mobWebsite.OrdersAndReturns.emptyTable();
        SMW.mobWebsite.OrdersAndReturns.loadCustomerData();
        SMW.mobWebsite.OrdersAndReturns.bindEvents();
        SMW.mobWebsite.OrdersAndReturns.onOrdersReturnInit();
        SMW.mobWebsite.OrdersAndReturns.disableCancelButton();
    },

    hideButton: function () {
        $("#CANCEL_SEC_BUTTON").hide();
        $("#RETURN_SEC_BUTTON").hide();
    },

    emptyTable: function () {
        $("#ITEMS_ORDER_CANCEL_ORT").empty();
    },

    onOrdersReturnInit: function () {
        $('#ITEMS_ORDER_RETURN_ORT .odr-tr:visible').each(function () {
            $("#ITEMS_ORDER_RETURN_ORT").empty();
            $("#orderreturnsdataError").empty();
        });
        SMW.mobWebsite.OrdersAndReturns.bindEvents();
        SMW.ajax.hideLoader();
    },

    bindEvents: function () {

        SMW.mobWebsite.OrdersAndReturns.unbindEvents();

        $('#ORDER_RETURNS_TRANS').on('change', SMW.mobWebsite.OrdersAndReturns.onTransSelection);
        $('#SELECT_ORDERS_RETURN_REF_NO').on('change', SMW.mobWebsite.OrdersAndReturns.onRefNo);
        $("#ITEMS_ORDER_RETURN_ORT").on('keyup', '.ord-input', SMW.mobWebsite.OrdersAndReturns.onQuantityKeyUp);
        $('#ORD_BTNITEMS_CANCEL').on('click', SMW.mobWebsite.OrdersAndReturns.onCancel);
        $("#ORD_BTNITEMS_CONFIRM").on('tap', SMW.mobWebsite.OrdersAndReturns.onOrderReturnSave);
        $("#ORD_BTNITEMS_CANCEL_INVOICE").on('tap', SMW.mobWebsite.OrdersAndReturns.onOrderCancel);
        $('#SELECT_ORDERS_RETURN_CUST').on('change', SMW.mobWebsite.OrdersAndReturns.getCustomerSites);
        $('#SELECT_ORDERS_RETURN_CUST_SITE').on('change', SMW.mobWebsite.OrdersAndReturns.onOrdersReturnPaymentTap);
        $('#ITEMS_ORDER_CANCEL_ORT').on('change', "input[name='checkRadio']", SMW.mobWebsite.OrdersAndReturns.enableCancelButton);
    },

    unbindEvents: function () {
        $('#ORDER_RETURNS_TRANS').unbind();
        $("#ITEMS_ORDER_RETURN_ORT").unbind();
        $('#ORD_BTNITEMS_CANCEL').unbind();
        $("#ORD_BTNITEMS_CONFIRM").unbind();
        $("#ORD_BTNITEMS_CANCEL_INVOICE").unbind();
        $('#SELECT_ORDERS_RETURN_CUST').unbind();
        $('#SELECT_ORDERS_RETURN_CUST_SITE').unbind();
        $('#SELECT_ORDERS_RETURN_REF_NO').unbind();
        $('#ITEMS_ORDER_CANCEL_ORT').unbind();
    },

    onRefNo: function () {
        var rtnRefNoValue = $("#SELECT_ORDERS_RETURN_REF_NO").val();
    if(rtnRefNoValue != null || rtnRefNoValue != ""){
      $.post(url + "rest-api/SalesEntry/referencNo", {
        referenceNum: $("#SELECT_ORDERS_RETURN_REF_NO").val(),
      }).done(function (data, textStatus, jqXHR) {
        if (data) {
          //alert("Entered Reference No already exists. ");
          $("#SELECT_ORDERS_RETURN_REF_NO").val("");
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

    enableCancelButton: function () {
        $('#ORD_BTNITEMS_CANCEL_INVOICE').prop('disabled', false);
    },

    disableCancelButton: function () {
        $('#ORD_BTNITEMS_CANCEL_INVOICE').prop('disabled', true);
    },

    disableConfirmButton: function () {
        $('#ORD_BTNITEMS_CONFIRM').prop('disabled', true);
    },

// updated on 26-02-2019 - Balaji Manickam - 4i Apps
    isEnabled: function () {
        var customer = $("#SELECT_ORDERS_RETURN_CUST").val();
        var customerSite = $("#SELECT_ORDERS_RETURN_CUST_SITE").val();
        if (customer == 0 && customerSite == 0) {
            $('#ORD_BTNITEMS_CANCEL_INVOICE').prop('disabled', true);
        }
    },

    getCustomerSites: function () {
        SMW.mobWebsite.OrdersAndReturns.disableCancelButton();
        SMW.mobWebsite.OrdersAndReturns.disableConfirmButton();
        $("#ITEMS_ORDER_CANCEL_ORT_ENABLE").hide();
        SMW.ajax.showLoader();
        $.post(url + "rest-api/customerSites",
                {customerId: $("#SELECT_ORDERS_RETURN_CUST option:selected").text(), userName: localStorage.getItem("userName")})
                .done(function (data, textStatus, jqXHR) {
                    SMW.mobWebsite.OrdersAndReturns.getCustomerSitePopulation(data);
                    $("#ITEMS_ORDER_RETURN_ORT").empty();
                    SMW.ajax.hideLoader();
                });
    },

    getCustomerSitePopulation: function (data) {
        $("#SELECT_ORDERS_RETURN_CUST_SITE").empty();
        var appenddata = '';
        appenddata += "<option value = '0'>Select</option>";
        $.each(data, function (key, value) {
            if (value.address == null) {
                var error = $("<div class='smw-error'/>");
                $('.smw-error').empty();
                error.html("No Location Map to Customer ID :" + $("#SELECT_ORDERS_RETURN_CUST").val() + " Please contact to Administrator").insertAfter("#SELECT_ORDERS_RETURN_CUST").show().delay(5000).fadeOut();
            } else {
                appenddata += "<option value = '" + value.locationId + "'>"
                        + value.address + "</option>";
            }
        });
        $('#SELECT_ORDERS_RETURN_CUST_SITE').html(appenddata);
    },

// Updated on 26-02-2019 by Balaji Manickam - 4i Apps
    onOrdersReturnPaymentTap: function () {
        if ($("#ORDER_RETURNS_TRANS").val() == 'cancel') {
            SMW.mobWebsite.OrdersAndReturns.disableCancelButton();
            $("#ITEMS_ORDER_CANCEL_ORT_ENABLE").show();
            $("#ITEMS_ORDER_CANCEL_ORT").empty();
            SMW.mobWebsite.OrdersAndReturns.loadDrawTableCancelData();
        } else {
            $("#ITEMS_ORDER_RETURN_ORT_ENABLE").show();
            $("#ITEMS_ORDER_RETURN_ORT").empty();
            SMW.ajax.showLoader();
            $.post(url + "rest-api/paymentMethod?",
                    {locationId: $("#SELECT_ORDERS_RETURN_CUST_SITE").val()})
                    .done(function (data, textStatus, jqXHR) {
                        //SMW.ajax.hideLoader();
                        SMW.mobWebsite.OrdersAndReturns.getPayment(data).promise().done(
                            function () {
                            SMW.ajax.hideLoader();
                        });
                    });
            $('#ORD_BTNITEMS_CONFIRM').prop('disabled', false);
        }
        SMW.mobWebsite.OrdersAndReturns.vatMethod();
    },

    getPayment: function (data) {
        $('#PAYMENTS_ORDERS_RETURNS').empty();
        $('#PAYMENTS_ORDERS_RETURNS').html(data.method);
        SMW.mobWebsite.OrdersAndReturns.loadDrawTableReturnData();
    },

    // // VAT
    // vatMethod: function () {
    //     SMW.ajax.showLoader();
    //     $("#itemsOrderReturn").empty();
    //     $.post(url + "rest-api/vatValue?", {
    //       userName: localStorage.getItem("userName"),
    //       custName: $("#SELECT_ORDERS_RETURN_CUST option:selected").text() 
    //     }).done(function (data, textStatus, jqXHR) {
    //       SMW.mobWebsite.OrdersAndReturns.getVatRate(data).promise().done(
    //         function () {
    //         SMW.ajax.hideLoader();
    //     });
    //     });
    //   },

    //   getVatRate: function (data) {
    //     $("#VAT_ORDERS_RETURNS").empty();
    //     $("#VAT_ORDERS_RETURNS").html(data.vatRate);
    //   },


// updated on 05-03-2018 - Balaji Manickam - 4i apps
    onOrderCancel: function () {
        SMW.ajax.showLoader();
        $.post(url + "rest-api/OrderReturns/cancellation-limit?vehicleNo=" + localStorage.userName,
                function () {
                })
                .done(function (data) {
                    if (data) {
                        $.post(url + "lookup/message-text?messageName=" + 'VANSALES CONFIRM CANCEL INV')
                                .done(function (data) {
                                    SMW.ajax.hideLoader();
                                    Swal.fire({
                                        title: 'Are you Sure?',
                                        html: "<p>" + data[1].messageText + "</P><br/><p>" + data[0].messageText + "</p>",
                                        type: 'warning',
                                        showCancelButton: true,
                                        confirmButtonText: 'Yes, cancel it!',
                                        cancelButtonText: 'Back'
                                    }).then((result) => {
                                        if (result.value) {
                                            SMW.mobWebsite.OrdersAndReturns.doProceedCancel();
                                        }
                                    });
                                });
                    } else {
                        $.post(url + "lookup/message-text?messageName=" + 'VANSALES INV CANCEL LIMIT')
                                .done(function (data) {
                                    SMW.ajax.hideLoader();
                                    Swal.fire({
                                        title: 'Sorry!',
                                        html: "<p>" + data[1].messageText + "</P><br/><p>" + data[0].messageText + "</p>",
                                        type: 'error',
                                        showConfirmButton: false,
                                        timer: 2000,
                                        animation: false,
                                        customClass: 'animated swing'
                                    });
                                })
                                .fail(function (data, textStatus, jqXHR) {
                                    SMW.ajax.hideLoader();
                                });
                    }
                })
                .fail(function (data) {
                    SMW.ajax.hideLoader();
                });
    },

    doProceedCancel: function () {
        SMW.ajax.showLoader();
        $("input[name='checkRadio']:checked").each(function () {
            var invoiceNum = $(this).closest('tr').find('#invoiceNum').text();
            if (invoiceNum != "") {
                $.post(url + "rest-api/OrderReturns/cancelItems?invoiceNum=" + invoiceNum,
                        function () {
                        })
                        .done(function (data, textStatus, jqXHR) {
                            SMW.ajax.hideLoader();
                            $.post(url + "lookup/message-text?messageName=" + 'VANSALES CANCEL INV SUCCESS')
                                    .done(function (data) {
                                        Swal.fire({
                                            title: 'Cancelled!',
                                            html: "<p>" + data[1].messageText + "</P><br/><p>" + data[0].messageText + "</p>",
                                            type: 'success',
                                            showConfirmButton: false,
                                            timer: 1500
                                        });
                                    });
                            SMW.mobWebsite.OrdersAndReturns.onCancel();
                        })
                        .fail(function (data, textStatus, jqXHR) {
                            SMW.ajax.hideLoader();
                        });
            }
        });
    },

    onOrderReturnSave: function () {
        var enteredInputsCount = 0;
        var flag = false;
        $(".ord-input").each(function () {
            if ($.trim($(this).val()).length == 0 || $(this).val() == 0)
            {
                $(this).closest('tr').addClass("hide-cls");
            } else {
                enteredInputsCount++;
                flag = true;
            }
        });
        if (flag) {
            SMW.mobWebsite.OrdersAndReturns.save();
        } else {
            var error = $("<div class='smw-error'/>");
            $('.smw-error').empty();
            error.html("Please enter data for atleast 1 item").insertAfter("#ITEMS_ORDER_RETURN_ORT").show().delay(3000).fadeOut();
        }
    },

    save: function () {
        var rtnRefNoValue = $("#SELECT_ORDERS_RETURN_REF_NO").val();
        SMW.ajax.showLoader();
        if ($('#ITEMS_ORDER_RETURN_ORT .odr-tr:visible').length > 0)
        {
            var dataArr = new Array();
            $('#ITEMS_ORDER_RETURN_ORT .odr-tr:visible').each(function () {
                var eachObj = {};
                var itemId = $(this).find("td:eq(0)").val();
                var quantity = $(this).find(".ord-input").val();
                var headerId = $(this).find(".ord-input").attr('data-headerId');
                var perVATRate = $(this).find(".ord-input").attr('data-linevat');
                var price = $(this).find(".ord-input").attr('data-price');
                var description = $(this).find("#description").text();
                eachObj.itemId = itemId;
                eachObj.quantity = quantity;
                eachObj.headerId = headerId;
                eachObj.perVATRate = perVATRate;
                eachObj.price = price;
                eachObj.description = description;
                if ($.trim(quantity).length != 0 && quantity > 0)
                {
                    dataArr.push(eachObj);
                }
            });
            console.log(dataArr);
            $.ajax({
                type: 'POST',
                url: url + "rest-api/OrderReturns/confirmItems?rtnRefNoParam=" +
                rtnRefNoValue,
                data: JSON.stringify({
                    'customerName': $("#SELECT_ORDERS_RETURN_CUST option:selected").text(),
                    'transactionType': $("#ORDER_RETURNS_TRANSTYPE option:selected").text(),
                    'customerSiteId': $("#SELECT_ORDERS_RETURN_CUST_SITE").val(),
                    'referenceNo': $("#SELECT_ORDERS_RETURN_REF_NO").val(),
                    'customerSite': $("#SELECT_ORDERS_RETURN_CUST_SITE option:selected").text(),
                    'paymentMethod': $('#PAYMENTS_ORDERS_RETURNS').text(),
                    'userName': localStorage.getItem("userName"), // need to be added from the cookie
                    'orgId': localStorage.getItem("organizationId"), // need to be added from the cookie
                    'custAccountId': $("#SELECT_ORDERS_RETURN_CUST").val(),
                    'transactionTypeId': $("#ORDER_RETURNS_TRANSTYPE").val(),
                    'subInvCode': null,
                    'transType': $("#ORDER_RETURNS_TRANS option:selected").text(),
                    'totalAmount': 0.0,
                    'locationId': null,
                    'items': dataArr,
                    // 'vatRate':$('#VAT_ORDERS_RETURNS').text()
                }),
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
                traditional: true,
                success: function (data) {
                    localStorage.orderReturnsInvoice = data.invoiceNum;
                    localStorage.orderReturnsCustomerSiteId = $("#SELECT_ORDERS_RETURN_CUST_SITE option:selected").text();
                    localStorage.orderReturnsCustomerSite = $("#SELECT_ORDERS_RETURN_CUST_SITE option:selected").text();
                    // successful request; do something with the data
                    localStorage.orderReturnsType = $("#ORDER_RETURNS_TRANSTYPE option:selected").text();
                    localStorage.orderReturnsRefNo = $("SELECT_ORDERS_RETURN_REF_NO").text();
                    //window.location=url+"ui/orderreturns/orderreturnsconfirmation.html";
                    if (data.result == false) {
                        SMW.ajax.hideLoader();
                        $("#orderreturnsdataError").html("Error getting data");
                    } else {
                        $.mobile.changePage('#PAGE_MOVE_ORDER_RETURNS_CONFIRMATION');
                        SMW.mobWebsite.OdrRetConfirm.init();
                    }
                },
                error: function (xhr, a, b) {
                    SMW.ajax.hideLoader();
                }
            });
        }
    },
    onOrdersReturnSelectTap: function () {
        SMW.ajax.showLoader();
        $.post(url + "rest-api/customerSites",
                {customerId: $("#SELECT_ORDERS_RETURN_CUST option:selected").text(), userName: localStorage.getItem("userName")})
                .done(function (data, textStatus, jqXHR) {
                    SMW.mobWebsite.OrdersAndReturns.getCustomerSite(data);
                    SMW.ajax.hideLoader();
                });
    },

    getCustomerSite: function (data) {
        var appenddata = '';
        appenddata += "<option value = '0'>Select</option>";

        $.each(data, function (key, value) {
            appenddata += "<option value = '" + value.locationId + "'>" + value.address + "</option>";
        });
        $('#custsite').html(appenddata);

    },

// updated 12-02-2019 - Balaji Manickam - 4i Apps
// rename from loadDrawTableData to loadDrawTableReturnData
    loadDrawTableReturnData: function () {
        SMW.ajax.showLoader();
        var customerId = $("#SELECT_ORDERS_RETURN_CUST option:selected").val();
        var locationId = $("#SELECT_ORDERS_RETURN_CUST_SITE option:selected").val();
        $.post(url + "rest-api/OrderReturns/items?userName=" + localStorage.getItem("userName") + "&customerId=" + customerId + "&partySiteId=" + locationId + "&organizationId=" + localStorage.getItem("organizationId"),
                function () {
                }).done(function (data, textStatus, jqXHR) {
            SMW.mobWebsite.OrdersAndReturns.drawReturnTable(data);
            SMW.ajax.hideLoader();
        });
    },

    loadDrawTableCancelData: function () {
        SMW.ajax.showLoader();
        var customerId = $("#SELECT_ORDERS_RETURN_CUST option:selected").val();
        var locationId = $("#SELECT_ORDERS_RETURN_CUST_SITE option:selected").val();
        $.post(url + "rest-api/OrderCancel/items?userName=" + localStorage.getItem("userName") + "&customerId=" + customerId + "&partySiteId=" + locationId,
                function () {
                }).done(function (data, textStatus, jqXHR) {
            SMW.mobWebsite.OrdersAndReturns.drawCancelTable(data);
            SMW.ajax.hideLoader();
        });
    },

    loadCustomerData: function () {
        SMW.ajax.showLoader();
        $.post(url + "rest-api/customers?userName=" + localStorage.getItem("userName"),
                function () {
                })
                .done(function (data, textStatus, jqXHR) {
                    SMW.mobWebsite.OrdersAndReturns.getCustomer(data);
                    SMW.ajax.hideLoader();
                });
    },

    getCustomer: function (data) {
        $("#SELECT_ORDERS_RETURN_CUST").empty();
        var appenddata = '';
        appenddata = ("<option value='0' selected='selected'>Select Customer Name</option>");
        $.each(data, function (key, value) {
            appenddata += "<option value = '" + value.customerId + "'>" + value.customerName + "</option>";
        });
        $('#SELECT_ORDERS_RETURN_CUST').html(appenddata);
    },

// Updated on 12-02-2019 by Balaji Manickam - 4i Apps
// added option Hiding Invoice Type, Payment Method, ITEMS_ORDER_RETURN, ITEMS_ORDER_CANCEL tables in initial time
// Updated constant data in in Trans Type select option section
    loadTransData: function () {
        SMW.ajax.showLoader();
        $("#ITEMS_ORDER_RETURN_ORT").empty();
        $("#ORDER_RETURNS_TRANSTYPE").empty();
        $("#SELECT_ORDERS_RETURN_REF_NO").val("");
        var trans = $("#ORDER_RETURNS_TRANS").empty();
        trans.append("<option value='0'>Select Trans Type</option>");
        trans.append("<option value='return'>RETURN</option>");
        // Dinesh 3-Jun-2022--hide cancel
        // trans.append("<option value='cancel'>CANCEL</option>");
        $("#ORDER_RETURNS_TRANSTYPE_ENABLE").hide();
        $("#PAYMENTS_ORDERS_RETURNS_ENABLE").hide();
        // $("#VAT_ORDERS_RETURNS_ENABLE").hide();
        $("#ITEMS_ORDER_RETURN_ORT_ENABLE").hide();
        $("#ITEMS_ORDER_CANCEL_ORT_ENABLE").hide();
    },

    trans: function (data) {
        var appenddata = "<option value = '0'>Select Trans Type</option>";
        $.each(data, function (key, value) {
            appenddata += "<option value = '" + value.rowId + "'>" + value.transName + "</option>";
        });
        $('#ORDER_RETURNS_TRANS').html(appenddata);
    },

// updated on 12-02-2019 - Balaji Manickam - 4i Apps
// rename from drawTable to drawReturnTable
    drawReturnTable: function (data) {
        $("#ITEMS_ORDER_RETURN_ORT").empty();
        $("#ITEMS_ORDER_RETURN_ORT").append("<tr><th>Description</th><th>Quantity</th></tr>");
        for (var i = 0; i < data.length; i++) {
            SMW.mobWebsite.OrdersAndReturns.drawReturnRow(data[i]);
        }
    },

// updated on 12-02-2019 - Balaji Manickam - 4i Apps
// updated on 28-01-2022 - Dinesh Kumar- 4i Apps [Disable if added]
// rename from drawRow to drawReturnRow
    drawReturnRow: function (rowData) {
        var row = $("<tr class='odr-tr'/>");
        $("#ITEMS_ORDER_RETURN_ORT").append(row); //this will append tr element to table... keep its reference for a while since we will add cels into it
        //  row.append($("<td>" + rowData.itemId + "</td>"));
        row.append($("<td id='description'>" + rowData.description + "</td>"));
        //Moshina - Req1
        if(rowData.price==0 || rowData.priceCnt==0){
            row.append($("<td> <input type='text' class='ord-input'"+
            "data-price='" + rowData.price +
             "' data-headerId='" + rowData.headerId + 
             "' data-linevat='" + rowData.perVATRate + 
             "' id='" + rowData.itemId +
              "' min='1' max='9999' disabled='disabled'></input></td>"));
        }else{
            row.append($("<td> <input type='text' class='ord-input'"+
            "data-price='" + rowData.price +
             "' data-headerId='" + rowData.headerId + 
             "' data-linevat='" + rowData.perVATRate + 
             "' id='" + rowData.itemId +
              "' min='1' max='9999' ></input></td>"));    
        }
    },

// updated on 12-02-2019 - Balaji Manickam - 4i Apps
// Added table for cancel
    drawCancelTable: function (data) {
        $("#ITEMS_ORDER_CANCEL_ORT").empty();
        var tableHeader = $("<thead></thead");
        var row = $("\
            <tr class='odr-tr'>\n\
                <th style='vertical-align: middle;'>Customer Name</th>\n\
                <th style='vertical-align: middle;'>Customer Location</th>\n\
                <th style='vertical-align: middle;'>Invoice Number</th>\n\
            </tr>");
        tableHeader.append(row);
        $("#ITEMS_ORDER_CANCEL_ORT").append(tableHeader); // this will append tr element to
        if (data.length == 0)
        {
            var dataRow = $("<tr class='mtv-tr'/>");
            $("#ITEMS_ORDER_CANCEL_ORT").append(dataRow);
            dataRow.append($("<td style='vertical-align: middle; text-align: center;' colspan='3'>No Data found</td>"));
        } else {
            row.append($("<th style='vertical-align: middle;'>Select</th>"));
            for (var i = 0; i < data.length; i++) {
                SMW.mobWebsite.OrdersAndReturns.drawCancelRow(data[i], i + 1);
            }
        }
    },

    drawCancelRow: function (rowData, index) {
        var row = $("<tr class='mtv-tr'/>");
        $("#ITEMS_ORDER_CANCEL_ORT").append(row); //this will append tr element to table... keep its reference for a while since we will add cels into it
        row.append($("<td style='vertical-align: middle;'>" + rowData.description + "</td>"));
        row.append($("<td style='vertical-align: middle;'>" + rowData.location + "</td>"));
        row.append($("<td id=\"invoiceNum\" style='vertical-align: middle;'>" + rowData.invoiceNum + "</td>"));
        row.append($("<td style='vertical-align: middle; text-align: center;'>\n\
                        <input id=\"input" + index + "\" class=\"hidden\" name=\"checkRadio\" type=\"radio\">\n\
                            <label class=\"entry\" for=\"input" + index + "\">\n\
                                <div class=\"circle\"></div>\n\
                            </label>\n\
                      </td>"));
    },

    onCancel: function () {
        $.mobile.changePage('#PAGE_HOME');
    },

// Updated on 12-02-2019 by Balaji Manickam - 4i Apps
// added option Hiding / showing Invoice Type, Payment Method tables dynamically based on trans type
    onTransSelection: function ()
    {
        $('#ORD_BTNITEMS_CONFIRM').prop('disabled', true);
        $("#RETURN_SEC_BUTTON").hide();
        $("#CANCEL_SEC_BUTTON").hide();
        if ($("#ORDER_RETURNS_TRANS").val() == 'cancel') {
            $("#ITEMS_ORDER_RETURN_ORT_ENABLE").hide();
            $("#ORDER_RETURNS_TRANSTYPE_ENABLE").hide();
            $("#PAYMENTS_ORDERS_RETURNS_ENABLE").hide();
            // $("#VAT_ORDERS_RETURNS_ENABLE").show();
            $("#CANCEL_SEC_BUTTON").show();
        } else {
            $("#ITEMS_ORDER_CANCEL_ORT_ENABLE").hide();
            $("#ORDER_RETURNS_TRANSTYPE_ENABLE").show();
            $("#PAYMENTS_ORDERS_RETURNS_ENABLE").show();
            // $("#VAT_ORDERS_RETURNS_ENABLE").show();
            $("#RETURN_SEC_BUTTON").show();
            SMW.mobWebsite.OrdersAndReturns.loadTransTypeData();
        }
    },

    loadTransTypeData: function () {
        SMW.ajax.showLoader();
        $.post(url + "rest-api/orderTransactionTypes?trans=" + $('#ORDER_RETURNS_TRANS option:selected').text())
                .done(function (data, textStatus, jqXHR) {
                    $.when(SMW.mobWebsite.OrdersAndReturns.transType(data)).done(function () {
                        SMW.ajax.hideLoader();
                    });

                });
    },

    transType: function (data) {
        appenddata = "";
        $.each(data, function (key, value) {
            appenddata += "<option value = '" + value.transactionId + "'>" + value.transactionName + "</option>";
        });
        $('#ORDER_RETURNS_TRANSTYPE').html(appenddata);
    },

    onQuantityKeyUp: function () {
        var data = $.trim($(this).val());
        var regx = /^\d{0,4}(\.\d{0,5})?$/;
        if (data != null && data != '')
        {
            if ($.isNumeric(data) == false || data.match(regx) == null)
            {
                var error = $("<div class='smw-error smw-rightalign'/>");
                var parentTr = $(this).closest('tr');
                //error.empty();
                $('.smw-error').empty();
                error.html("Invalid item quantity! ").insertBefore(parentTr).show().delay(5000).fadeOut();
                $(this).val('');
            }
        }
    }
};











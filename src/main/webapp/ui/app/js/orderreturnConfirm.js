SMW.mobWebsite.OdrRetConfirm = {

    init: function () {
        SMW.mobWebsite.OdrRetConfirm.loadData(); //SMW.mobWebsite.OdrRetConfirm
        SMW.mobWebsite.OdrRetConfirm.orderReturnInit();
    },
    orderReturnInit: function () {
        SMW.mobWebsite.OdrRetConfirm.bindEvents();
    },

    bindEvents: function () {
        this.unbindEvents();
        $('#INVOICE_ORD_CONF_BTN_PRINT').on('tap', SMW.mobWebsite.OdrRetConfirm.onInvoicePrintTap);
        $('#BTN_CANCEL_ODR_CONFRM').on('tap',SMW.mobWebsite.OdrRetConfirm.onCancel);
    },
    
    
    unbindEvents: function () {
        $('#INVOICE_ORD_CONF_BTN_PRINT').unbind();
        $("#BTN_CANCEL_ODR_CONFRM").unbind();
    },

    loadData: function () {
        var rtnRefNoValue = $("#SELECT_ORDERS_RETURN_REF_NO").val();
        SMW.ajax.showLoader();
        $.ajax({url: getBaseUrl() + "/rest-api/MoveOrder/confirmtems?userName=" + localStorage.getItem('userName') + 
        "&invoiceNum=" + localStorage.orderReturnsInvoice + 
        "&orderType=MOVE_ORDER" +
        "&moveRefNoParam=" + rtnRefNoValue,
            type: "post",
            dataType: "json",
            success: function (data, textStatus, jqXHR) {
                //console.log("success");
                SMW.mobWebsite.OdrRetConfirm.fillData(data);
                SMW.ajax.hideLoader();
            }
        })
    },

    fillData: function (data) {
        $('#transactionType').html(data.transactionType);
        $('#orderRetnTransType').html(data.transType);
        $('#orderRtnRefNo').html(data.referenceNo);
        $('#paymentMethod').html(data.paymentMethod);
        $('#customerSite').html(localStorage.orderReturnsCustomerSite);
        $('#customerName').html(data.customerName);
        // $('#vatRates').html(data.vatRate);
        $('#totalAmount').html((data.totalAmount).toFixed(3));
        $('#vatAmount').html((data.vatAmount).toFixed(3));
        $('#netAmount').html((data.netAmount).toFixed(3));
        SMW.mobWebsite.OdrRetConfirm.displayTable(data.items);
    },

    displayTable: function (data) {
        $("#itemsOrderReturns").empty();
        $("#promItemsOrderReturns").empty();
        var rowData = "<tr>"+
        "<th class=\"smw-centeralign logo-backgroundcolor-light-green\">Description</th>"+
        "<th class=\"smw-centeralign logo-backgroundcolor-light-blue\">Qty</th>"+
        "<th class=\"smw-centeralign logo-backgroundcolor-light-blue\">Amt</th>"+
        "<th class=\"smw-centeralign logo-backgroundcolor-light-blue\">Vat</th>"+
        "</tr>";
        var rowDataPro = "<tr>"+
        "<th class=\"smw-centeralign logo-backgroundcolor-light-green\">Description</th>"+
        "<th class=\"smw-centeralign logo-backgroundcolor-light-blue\">Quantity</th>"+
        "</tr>";
        $("#itemsOrderReturns").append(rowData);
        $("#promItemsOrderReturns").append(rowDataPro);
        console.log('Order returns confirm array length' + data.length);
        for (var i = 0; i < data.length; i++) {
            SMW.mobWebsite.OdrRetConfirm.displayRow(data[i]);
        }
    },

    displayRow: function (rowData) {

        var row = $("<tr class='odr-tr'/>");

        if (rowData.focFlag == "N") {
            $("#itemsOrderReturns").append(row); // this will append tr element to
            // table... keep its reference for a
            // while since we will add cels into it
            //row.append($("<td>" + rowData.itemId + "</td>"));
            row.append($("<td class=\"logo-backgroundcolor-light-green\">" + rowData.description + "</td>"));
            row.append($("<td class=\"logo-backgroundcolor-light-blue smw-centeralign logo-color-blue\">" + rowData.quantity + "</td>"));
            row.append($("<td class=\"logo-backgroundcolor-light-blue smw-centeralign logo-color-blue\">" + rowData.itemAmount + "</td>"));
            row.append($("<td class=\"logo-backgroundcolor-light-blue smw-centeralign logo-color-blue\">" + rowData.vatAmount + "</td>"));
        } else {
            $("#promItemsOrderReturns").append(row); // this will append tr element to
            // table... keep its reference for a
            // while since we will add cels into it
            row.append($("<td class=\"logo-backgroundcolor-light-green\">" + rowData.description + "</td>"));
            row.append($("<td class=\"logo-backgroundcolor-light-blue smw-centeralign logo-color-blue\">" + rowData.quantity + "</td>"));
        }

    },
    onCancel: function () {
        $.ajax({
            type: 'POST',
            url: url + "/rest-api/MoveOrder/cancelItems?invoiceNumber=" + localStorage.orderReturnsInvoice + "&orderType=MOVE_ORDER",
            dataType: "json",
            success: function (data, textStatus, jqXHR) {
                console.log("success");
            }
        });
        $.mobile.changePage('#PAGE_ORDERS_RETURN');
    },

    onInvoicePrintTap: function () {
        console.log('onInvoicePrintTap');
        SMW.ajax.showLoader();
        $.ajax({
            type: 'POST',
            url: url + "/rest-api/PrintOrderReturn/items?userName=" + localStorage.getItem("userName") + "&invoiceNum=" + localStorage.orderReturnsInvoice,
            dataType: "json",
            success: function (data, textStatus, jqXHR) {
                console.log("success");
                if (data.printInvoiceFlag == "T") {
                    window.open(SMW.ip + localStorage.orderReturnsInvoice + ".pdf", '_blank');
                } else {
                    var error = $("<div class='smw-error'/>");
                    $('.smw-error').empty();
                    error.html("Encountered error while running Invoice Print Report!").insertBefore("#INVOICE_ORD_CONF_BTN_PRINT").show().delay(3000).fadeOut();
                    $('.hide-cls').removeClass('hide-cls');
                }
                SMW.ajax.hideLoader();
            }
        });
    }
};





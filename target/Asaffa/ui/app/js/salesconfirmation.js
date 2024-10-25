
SMW.mobWebsite.salesEntry.confirmartion = {
    _serviceSaleConfirmation: '/rest-api/PrintInvoice/items',

    init: function () {
        SMW.mobWebsite.salesEntry.confirmartion.loadInvoiceData();
        SMW.mobWebsite.salesEntry.confirmartion.onSalesConfirmationInit();

    },
    onSalesConfirmationInit: function () {
        SMW.mobWebsite.salesEntry.confirmartion.bindEvents();
    },

    bindEvents: function () {
        this.unbindEvents();
        $('#INV_RCPT_CONF_CANCL').on('tap', SMW.mobWebsite.salesEntry.confirmartion.onCancel);
        $("#INV_RCPT_CONF_CANCL").show();
        $('#INVOICE_CONF_BTN_PRINT').on('tap', SMW.mobWebsite.salesEntry.confirmartion.onSalesInvoicePrintTap);
        $('#INVOICE_RECEIPT_BTN').on('tap', SMW.mobWebsite.salesEntry.confirmartion.onInvoiceReceiptTap);
    },

    unbindEvents: function () {
        $('#INV_RCPT_CONF_CANCL').unbind();
        $("#INVOICE_RECEIPT_BTN").unbind();
        $("#INVOICE_CONF_BTN_PRINT").unbind();
    },

    onInvoiceReceiptTap: function () {
        $('#INVOICE_RECEIPT_SUCCESS_MSG').empty();
        $("#chequeNum").val('');
        $.mobile.changePage('#PAGE_INVOICE_RECEIPT_CONFIRMATION');
        SMW.mobWebsite.salesEntry.invoicereceipt.init();


    },
    onInvoiceCancelTap: function () {
        $.ajax({
            type: 'POST',
            url: url + "/rest-api/MoveOrder/cancelItems?invoiceNumber=" + localStorage.orderReturnsInvoice,
            dataType: "json",
            success: function (data, textStatus, jqXHR) {
                $.mobile.changePage('#PAGE_INVOICE_ENTRY');
            }
        });
    },
    
    onSalesInvoicePrintTap: function () {
        SMW.ajax.showLoader();
        $("#INV_RCPT_CONF_CANCL").hide();
        var data = {userName: localStorage.getItem("userName"), invoiceNum: localStorage.orderReturnsInvoice};
        $.ajax({
            type: 'POST',
            url: url + "/rest-api/PrintInvoice/items?invoiceType=ORDER&userName=" + localStorage.getItem("userName") + "&invoiceNum=" + localStorage.orderReturnsInvoice,
            dataType: "json",
            traditional: true,
            success: function (data, textStatus, jqXHR) {			
                if (data.printInvoiceFlag === 'T') {
                    window.open(SMW.ip + localStorage.orderReturnsInvoice + ".pdf", '_blank');
                } else {
                    var error = $("<div class='smw-error'/>");
                    $('.smw-error').empty();
                    error.html("Encountered error while running Invoice Print Report!").insertBefore("#INV_RCPT_CONF_CANCL").show().delay(3000).fadeOut();
                    $('.hide-cls').removeClass('hide-cls');
                }
                SMW.ajax.hideLoader();
            },
            error: function (data, textStatus, jqXHR) {
                SMW.ajax.hideLoader();
            }

        });
    },

    onPrintSuccess: function (data) {
        if (data.printInvoiceFlag === 'T') {
            window.open(SMW.ip + localStorage.orderReturnsInvoice + '.pdf', '_blank');
        } else {
            alert("Failed!!!!");
        }

    },

    loadInvoiceData: function () {
        var refNoVal = $("#refNo").val();
        SMW.mobWebsite.salesEntry.confirmartion.clearConfirmForm();
        SMW.ajax.showLoader();
        $.ajax({url: url + "/rest-api/MoveOrder/confirmtems?userName=" + 
        localStorage.getItem("userName") + "&invoiceNum=" + 
        localStorage.orderReturnsInvoice + "&orderType=MOVE_ORDERINV" +
        "&moveRefNoParam=" +
        refNoVal,
            type: "post",
            dataType: "json",
            success: function (data, textStatus, jqXHR) {
                localStorage.custData = JSON.stringify(data);
                SMW.mobWebsite.salesEntry.confirmartion.fillData(data);
                SMW.ajax.hideLoader();
            }
        });
    },

    fillData: function (data) {
        $('#INVOICE_CONF_TRANS_TYPE').html(data.transactionType);
        $('#INVOICE_CONF_CUST').html(data.customerName);
        $('#INVOICE_CONF_CUST_SITE').html(localStorage.customerSite);
        $('#INVOICE_CONF_PAYMENT').html(data.paymentMethod);
        $('#INVOICE_CONF_VAT').html(data.vatRate);
        $('#INVOICE_CONF_REF_NO').html(data.referenceNo);
        displayTable(data.items);
        $('#INVOICE_CONF_TOTAL_AMOUNT').html((data.totalAmount).toFixed(3));
        $('#INVOICE_CONF_VAT_AMOUNT').html((data.vatAmount).toFixed(3));
        $('#INVOICE_CONF_NET_AMOUNT').html((data.netAmount).toFixed(3));
    },
    
    onCancel: function () {
        SMW.mobWebsite.salesEntry.confirmartion.clearConfirmForm();
        $.mobile.changePage('#PAGE_HOME');
    },
    
    clearConfirmForm: function () {
        $('#INVOICE_CONF_TRANS_TYPE').empty();
        $('#INVOICE_CONF_CUST').empty();
        $('#INVOICE_CONF_CUST_SITE').empty();
        $('#INVOICE_CONF_PAYMENT').empty();
        $('#INVOICE_CONF_VAT').empty();
        $("#INVOICE_CONF_ITEMS").empty();
        $("#INVOICE_CONF_PROM_ITEMS").empty();
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
        $("#INVOICE_CONF_ITEMS").append(rowData);
        $("#INVOICE_CONF_PROM_ITEMS").append(rowDataPro);
        $('#INVOICE_CONF_TOTAL_AMOUNT').empty();
    }

};

function displayTable(data) {
    $("#INVOICE_CONF_ITEMS").empty();
    $("#INVOICE_CONF_PROM_ITEMS").empty();
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
    $("#INVOICE_CONF_ITEMS").append(rowData);
    $("#INVOICE_CONF_PROM_ITEMS").append(rowDataPro);
    for (var i = 0; i < data.length; i++) {
        displayRow(data[i]);
    }
}

function displayRow(rowData) {
    var row = $("<tr class='odr-tr'/>");
    rowData.focFlag === 'N' ? $("#INVOICE_CONF_ITEMS").append(row) : $("#INVOICE_CONF_PROM_ITEMS").append(row);
// table... keep its reference for a
// while since we will add cels into it
    row.append($("<td class=\"logo-backgroundcolor-light-green\">" + rowData.description + "</td>"));
    row.append($("<td class=\"logo-backgroundcolor-light-blue smw-centeralign logo-color-blue\">" + rowData.quantity + "</td>"));
    row.append($("<td class=\"logo-backgroundcolor-light-blue smw-centeralign logo-color-blue\">" + rowData.itemAmount + "</td>"));
    row.append($("<td class=\"logo-backgroundcolor-light-blue smw-centeralign logo-color-blue\">" + rowData.vatAmount + "</td>"));
}

// handle back arrow on browser
$(window).on("navigate", function (event, data) {
    // Firefox 1.0+
    var isFirefox = typeof InstallTrigger !== 'undefined';
    var direction = data.state.direction;
    var pageId = document.activeElement.id;
    if (isFirefox) {
        pageId = document.baseURI.substring(document.baseURI.indexOf('#') + 1);
    }
    if (direction === 'back' && (pageId === 'PAGE_INVOICE_ENTRY_REVIEW' || pageId === 'PAGE_INVOICE_ENTRY_CONFIRMATION')) {
        $.mobile.navigate("#PAGE_HOME");
    }
});
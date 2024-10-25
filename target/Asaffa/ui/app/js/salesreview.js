
/* global SMW, error, url,*/

var toggleSwitch = true;

SMW.mobWebsite.salesEntry.review = {
    
    init: function () {
        SMW.mobWebsite.salesEntry.review.loadInvoiceData();
    },
    loadInvoiceData: function () {
        var refNoVal = $("#refNo").val();
        SMW.mobWebsite.salesEntry.review.clearReviewForm();
        SMW.ajax.showLoader();
        $.ajax({
            url: url + "/rest-api/MoveOrder/reviewitems?userName=" + 
            localStorage.getItem("userName") + 
            "&invoiceNum=" + localStorage.orderReturnsInvoice +
            "&moveRefNoParam=" +
            inputFromReview.refNoVal,
            type: "post",
            dataType: "json",
            success: function (data, textStatus, jqXHR) {
                localStorage.custData = JSON.stringify(data);
                SMW.mobWebsite.salesEntry.review.fillData(data);
                SMW.ajax.hideLoader();
            }
        });
    },
    fillData: function (data) {
        $('#INVOICE_REVIEW_TRANS_TYPE').html(data.transactionType);
        $('#INVOICE_REVIEW_CUST').html(data.customerName);
        $('#INVOICE_REVIEW_CUST_SITE').html(localStorage.customerSite);
        $('#INVOICE_REVIEW_PAYMENT').html(data.paymentMethod);
        $('#INVOICE_REFERENCE_NUMBER').html(data.referenceNo);
        
        // $('#INVOICE_REVIEW_VAT').html(data.vatRate);
        displayReviewTable(data.items);
        $('#INVOICE_REVIEW_TOTAL_AMOUNT').html((data.totalAmount).toFixed(3));
        $('#INVOICE_REVIEW_VAT_AMOUNT').html((data.vatAmount).toFixed(3));
        $('#INVOICE_REVIEW_NET_AMOUNT').html((data.netAmount).toFixed(3));
    },
    clearReviewForm: function () {
        toggleSwitch = true;
        $('#INVOICE_REVIEW_TRANS_TYPE').empty();
        $('#INVOICE_REVIEW_CUST').empty();
        $('#INVOICE_REVIEW_CUST_SITE').empty();
        $('#INVOICE_REVIEW_PAYMENT').empty();
        $('#INVOICE_REFERENCE_NUMBER').empty();
        // $('#INVOICE_REVIEW_VAT').empty();
        $("#INVOICE_REVIEW_ITEMS").empty();
        $("#INVOICE_REVIEW_PROM_ITEMS").empty();
        var rowData = "<tr>"+
        "<th class=\"smw-centeralign logo-backgroundcolor-light-green\">Description</th>"+
        "<th class=\"smw-centeralign logo-backgroundcolor-light-blue\">Qty</th>"+
        "<th class=\"smw-centeralign logo-backgroundcolor-light-blue\">Amt</th>"+
        "<th class=\"smw-centeralign logo-backgroundcolor-light-blue\">Vat</th>"+
        "</tr>";
        var rowDataProm = "<tr>"+
        "<th class=\"smw-centeralign logo-backgroundcolor-light-green\">Description</th>"+
        "<th class=\"smw-centeralign logo-backgroundcolor-light-blue\">Quantity</th>"+
        "</tr>";
        $("#INVOICE_REVIEW_ITEMS").append(rowData);
        $("#INVOICE_REVIEW_PROM_ITEMS").append(rowDataProm);
        $('#INVOICE_REVIEW_TOTAL_AMOUNT').empty();
        $('#INVOICE_REVIEW_VAT_AMOUNT').empty();
        $('#INVOICE_REVIEW_NET_AMOUNT').empty();
        $('button').filter('.show-confirm').show();
    }
};
function displayReviewTable(data) {
    $("#INVOICE_REVIEW_ITEMS").empty();
    $("#INVOICE_REVIEW_PROM_ITEMS").empty();
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
    $("#INVOICE_REVIEW_ITEMS").append(rowData);
    $("#INVOICE_REVIEW_PROM_ITEMS").append(rowDataPro);
    data.forEach(displayReviewRow);
}

function displayReviewRow(value) {
    var row = $("<tr class='odr-tr'/>");
    var rowProm = $("<tr class='odr-tr'/>");
    if (value.status != null && value.status == false) {
        row.addClass("alert alert-danger");
        row.append($("<td>" + value.description + "</td>"));
        row.append($("<td class=\"smw-centeralign\">" + value.quantity + "</td>"));
        row.append($("<td class=\"smw-centeralign\">" + value.itemAmount + "</td>"));
        row.append($("<td class=\"smw-centeralign\">" + value.vatAmount + "</td>"));
        // row Prom
        rowProm.addClass("alert alert-danger");
        rowProm.append($("<td>" + value.description + "</td>"));
        rowProm.append($("<td class=\"smw-centeralign\">" + value.quantity + "</td>"));

        errorPage();
    } else {
        row.append($("<td class=\"logo-backgroundcolor-light-green\">" + value.description + "</td>"));
        row.append($("<td class=\"logo-backgroundcolor-light-blue smw-centeralign logo-color-blue\">" + value.quantity + "</td>"));
        row.append($("<td class=\"logo-backgroundcolor-light-blue smw-centeralign logo-color-blue\">" + value.itemAmount + "</td>"));
        row.append($("<td class=\"logo-backgroundcolor-light-blue smw-centeralign logo-color-blue\">" + value.vatAmount + "</td>"));
        // rowProm
        rowProm.append($("<td class=\"logo-backgroundcolor-light-green\">" + value.description + "</td>"));
        rowProm.append($("<td class=\"logo-backgroundcolor-light-blue smw-centeralign logo-color-blue\">" + value.quantity + "</td>"));

    }
    value.focFlag === 'N' ? $("#INVOICE_REVIEW_ITEMS").append(row) : $("#INVOICE_REVIEW_PROM_ITEMS").append(rowProm);
}

function errorPage() {
    if (this.toggleSwitch) {
        var error = $("<div class='smw-error'/>");
        $('.smw-error').empty();
        error.html("Oops! Don't have sufficient material.").insertAfter("#reviewdataError").show().delay(3000).fadeOut();
        $('.hide-cls').removeClass('hide-cls');
        $('#reviewdataError .ord-input').removeClass('disabled-cls');
        $('button').filter('.show-confirm').hide();
        this.toggleSwitch = false;
    }
}
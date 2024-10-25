/* global SMW */

SMW.mobWebsite.salesEntry.invoicereceipt = {
    _serviceInvReceipt: '/rest-api/PrintInvoice/items',
    init: function () {

        SMW.mobWebsite.salesEntry.invoicereceipt.onInvoiceReceiptInit();

    },
    onInvoiceReceiptInit: function () {
        //console.log(' onInvoiceReceiptInit');
        SMW.mobWebsite.salesEntry.invoicereceipt.loadonInvoiceReceiptData();
        SMW.mobWebsite.salesEntry.invoicereceipt.bindEvents();
    },

    bindEvents: function () {
        this.unbindEvents();
        $('#INVOICE_RECEIPT_BTN_PRINT').on('tap', SMW.mobWebsite.salesEntry.invoicereceipt.onInvoiceReceiptPrintTap);
        $('#BTN_INVOICE_RECEIPT_CONFIRM').on('tap', SMW.mobWebsite.salesEntry.invoicereceipt.onInvoiceReceiptConfirmTap);
        $("#INVOICE_CANCEL").on("tap", SMW.mobWebsite.salesEntry.invoicereceipt.onInvoiceReceiptCancel);
    },

    unbindEvents: function () {
        $('#INVOICE_RECEIPT_BTN_PRINT').unbind();
        $("#BTN_INVOICE_RECEIPT_CONFIRM").unbind();
        $("#INVOICE_CANCEL").unbind();
    },

    onInvoiceReceiptConfirmTap: function () {
        var data = $.trim($('#chequeAmt').val());
        var regx = /^\b0*[0-9][0-9]{0,5}\b-?(\d?|\.\d{0,3})\s*$/;

        if (data != null && data != '')
        {
            if ($.isNumeric(data) == false || data.match(regx) == null)
            {
                var error = $("<div class='smw-error'/>");
                $('.smw-error').empty();
                error.html("Invalid Amount Format! ").insertBefore("#chequeAmt").show().delay(5000).fadeOut();
                $('#chequeAmt').val((localStorage.totalAmount).toFixed(3));
            } else {
                var chequeNum = $("#chequeNum").val();
                var chequeAmt = $("#chequeAmt").val();
                if (chequeAmt > localStorage.totalAmount) {
                    var error = $("<div class='smw-error'/>");
                    error.html("Check amount should be less than total amount:" + localStorage.totalAmount).insertAfter("#INVOICE_RECEIPT_SUCCESS_MSG").show().delay(3000).fadeOut();
                    $('.hide-cls').removeClass('hide-cls');
                    $("#INVOICE_RECEIPT_SUCCESS_MSG .ord-input").removeClass('disabled-cls');
                    $("#chequeAmt").val((localStorage.totalAmount).toFixed(3));
                } else {
                    $.ajax({
                        type: 'POST',
                        url: url + "/rest-api/SalesInvoiceReceipt/updateCustomer?chequeNum=" + chequeNum + "&chequeAmt=" + chequeAmt + "&invoiceNumber=" + localStorage.orderReturnsInvoice,
                        dataType: "json",
                        success: function (data, textStatus, jqXHR) {
                            //console.log("success onInvoiceReceiptConfirmTap");					
                            $('#INVOICE_RECEIPT_SUCCESS_MSG').html("<font color='green'>Successfully updated the Cheque Number and Amount for invoice id:" + localStorage.orderReturnsInvoice + "</font>");
                        }
                    });
                }

            }

        }


    },

    onInvoiceReceiptCancel: function () {

        $.mobile.changePage('#PAGE_HOME');
    },
    onInvoiceReceiptPrintTap: function () {
        //console.log('onInvoiceReceiptPrintTap');
        var data = {userName: localStorage.getItem("userName"), invoiceNum: localStorage.orderReturnsInvoice};
        SMW.mobWebsite.salesEntry.invoicereceipt.onInvoiceReceiptConfirmTap();
        SMW.ajax.callCType(url + SMW.mobWebsite.salesEntry.invoicereceipt._serviceInvReceipt, data, SMW.mobWebsite.salesEntry.invoicereceipt.onPrintSuccess, true, 'application/x-www-form-urlencoded');

    },

    onPrintSuccess: function (data) {
        if (data.printInvoiceFlag == "T") {
            window.open(SMW.rp + localStorage.orderReturnsInvoice + ".pdf", '_blank');
        } else {
            var error = $("<div class='smw-error'/>");
            $('.smw-error').empty();
            error.html("Encountered error while running Receipt Print Report!").insertBefore("#INVOICE_RECEIPT_BTN_PRINT").show().delay(3000).fadeOut();
            $('.hide-cls').removeClass('hide-cls');
        }
    },
    loadonInvoiceReceiptData: function () {
        //console.log("success.........######......");
        SMW.mobWebsite.salesEntry.invoicereceipt.fillData(JSON.parse(localStorage.custData));
    },

    fillData: function (data) {

        $('#INVOICE_RECEIPT_TRANS_TYPE').html(data.transactionType);
        $('#INVOICE_RECEIPT_CUST').html(data.customerName);
        $('#INVOICE_RECEIPT_CUST_SITE').html(localStorage.customerSite);
        $('#INVOICE_RECEIPT_PAYMENT').html(data.paymentMethod);
        $('#INVOICE_RECEIPT_NUMBER').html(localStorage.orderReturnsInvoice);
        $('#INVOICE_RECEIPT_CURRENCY').html("OMR");
        if (data.paymentMethod != null && (data.paymentMethod.toUpperCase() == 'CASH')) {
            $("#chequeNum").prop('disabled', true);
            $("#chequeAmt").prop('disabled', true);
        } else {
            $("#chequeNum").prop('disabled', false);
            $("#chequeAmt").prop('disabled', false);
        }
        $('#chequeAmt').val((data.totalAmount).toFixed(3));
        localStorage.totalAmount = data.totalAmount;
    },
    onQuantityKeyUp: function () {
        var data = $.trim($(this).val());
        var regx = /\b0*[0-9][0-9]{0,3}\b/;

        if (data != null && data != '')
        {
            if ($.isNumeric(data) == false || data.match(regx) == null)
            {
                var error = $("<div class='smw-error smw-rightalign'/>");
                var parentTr = $(this).closest('tr');
                //error.empty();
                $('.smw-error').empty();
                error.html("Invalid Amount! ").insertBefore(parentTr).show().delay(5000).fadeOut();
                $(this).val('');
            }

        }


    }

};
/* global SMW */

var invoiceType;
SMW.salesInvoiceBrowser = {

    _serviceLoadData: 'rest-api/SalesInvoiceBrowser/items',
    init: function () {
        SMW.salesInvoiceBrowser.loadData();
        SMW.salesInvoiceBrowser.bindEvents();
    },

    bindEvents: function () {
        this.unbindEvents();
        $("#salesInvoiceDisplay").on('tap', this.loadInvoiceData);
        $("#materialOrderPrint").on('tap', this.getOrderNumber);
        $("#invoiceType").on('change', this.onChangeMenuType);
        $("#orderNo").on('change', this.onChangeOrderNo);
    },

    unbindEvents: function () {
        $('#salesInvoiceDisplay').unbind();
        $("#materialOrderPrint").unbind();
        $('#invoiceType').unbind();
        $('#orderNo').unbind();
    },

    getOrderNumber: function () {
        let orderNumber = $("#orderNo option:selected").val();
        SMW.moveToVehicles.onPrintMaterialRequestOrder(orderNumber);
    },

    loadData: function () {
        $("#salesInvoiceDisplay").attr('disabled', "disabled");
        $("#materialOrderPrint").hide();
        $("#MATERIAL_REQUEST").hide();
        $("#invoiceType option[value=ORDER]").attr('selected', 'selected');
        $("#orderNoRow").hide();
        $("#salesInvoiceDisplay").show();
        $("#salesInvoiceDisplay").attr('disabled', "disabled");
        $("#invoiceCreateDate").val('');
        $("#invoiceCreateDate").datepicker({dateFormat: 'dd-M-yy'});
        var maxDate = new Date();
        $("#invoiceCreateDate").datepicker("option", 'maxDate', maxDate);
        $("#salesInvoiceDatePicker").text("Order Date");
    },

    onChangeMenuType: function (event) {
        SMW.salesInvoiceBrowser.changeDatePickerLabel(event.target.value);
        $("#SALES_INVOICE").hide();
        $("#MATERIAL_REQUEST").hide();
        $("#invoiceCreateDate").val('');
        if (event.target.value === "MOVE_ORDER") {
            $("#orderNoRow").show();
            $("#orderNo").empty();
            $("#salesInvoiceDisplay").hide();
            $("#materialOrderPrint").show();
            $("#materialOrderPrint").attr('disabled', "disabled");
        } else {
            $("#orderNoRow").hide();
            $("#materialOrderPrint").hide();
            $("#salesInvoiceDisplay").show();
            $("#salesInvoiceDisplay").attr('disabled', "disabled");
            $("#orderNo").empty();
        }
    },

    changeDatePickerLabel: function (menu) {
        if (menu === "ORDER") {
            $("#salesInvoiceDatePicker").text("Order Date");
        } else if (menu === "RETURN") {
            $("#salesInvoiceDatePicker").text("Return Date");
        } else if (menu === "CANCEL") {
            $("#salesInvoiceDatePicker").text("Cancel Date");
        } else if (menu === "MOVE_ORDER") {
            $("#salesInvoiceDatePicker").text("Material Request Date");
        }
    },

    onChangeInvoiceDate: function () {
        let menuType = $("#invoiceType option:selected").val();
        let orderReqDate = $("#invoiceCreateDate").val();
        if (menuType === "MOVE_ORDER") {
            SMW.ajax.showLoader();
            $.ajax({
                type: 'POST',
                url: url + "rest-api/SalesInvoiceBrowser/moveOrder?menuType=" + menuType + "&vehicleNo=" + localStorage.getItem("userName") + "&orderReqDate=" + orderReqDate,
                dataType: "json",
                success: function (data, textStatus, jqXHR) {
                    console.log(data);
                    SMW.salesInvoiceBrowser.loadOrderNo(data);
                    SMW.ajax.hideLoader();
                }
            });
        } else {
            $("#salesInvoiceDisplay").attr('disabled', false);
        }
    },

    onChangeOrderNo: function (event) {
        if (event.target.value !== "0") {
            SMW.ajax.showLoader();
            $.ajax({
                type: 'POST',
                url: url + "rest-api/SalesInvoiceBrowser/moveOrder/items?orderNo=" + event.target.value,
                dataType: "json",
                success: function (data, textStatus, jqXHR) {
                    SMW.salesInvoiceBrowser.loadMaterialRequest(data);
                    SMW.ajax.hideLoader();
                    $("#materialOrderPrint").attr('disabled', false);
                }
            });
        } else {
            $("#MATERIAL_REQUEST").hide();
            $("#materialOrderPrint").attr('disabled', "disabled");
        }
    },

    loadMaterialRequest: function (data) {
        $("#MATERIAL_REQUEST").empty();
        var row = $("<tr class='mtv-tr'><th style='vertical-align: middle;'>Description</th><th style='vertical-align: middle;'>Quantity</th></tr>");
        $("#MATERIAL_REQUEST").append(row); // this will append tr element to
        data.forEach(rowData => {
            var row = $("<tr class='mtv-tr'/>");
            $("#MATERIAL_REQUEST").append(row); //this will append tr element to table... keep its reference for a while since we will add cels into it
            row.append($("<td style='vertical-align: middle;'>" + rowData.DESCRIPTION + "</td>"));
            row.append($("<td style='vertical-align: middle;'>" + rowData.SOLD_QTY + "</td>"));
        });
        $("#MATERIAL_REQUEST").show();
    },

    loadOrderNo: function (data) {
        $("#orderNo").empty();
        var appenddata = '';
        appenddata += "<option value = '0'>Select</option>";
        data.forEach(key => {
            appenddata += "<option value = '" + key.ORDER_NUMBER + "'>"
                    + key.ORDER_NUMBER + "</option>";
        });
        $('#orderNo').html(appenddata);
    },

    loadInvoiceData: function () {
        SMW.ajax.showLoader();
        var invoiceCreateDate = $("#invoiceCreateDate").val();
        invoiceType = $("#invoiceType option:selected").val();
        if (invoiceType == 'MOVE_ORDER') {
            $("#orderNoRow").show();
        } else if (invoiceType == 'CANCEL') {
            $.ajax({
                type: 'POST',
                url: url + "rest-api/SalesInvoiceBrowser/cancelledItems?userName=" + localStorage.getItem('userName') + "&loadInvoiceDate=" + invoiceCreateDate,
                dataType: "json",
                success: function (data, textStatus, jqXHR) {
                    SMW.salesInvoiceBrowser.onLoadDataSuccess(data, invoiceType);
                    SMW.ajax.hideLoader();
                }
            });
        } else {
            $.ajax({
                type: 'POST',
                url: url + "rest-api/SalesInvoiceBrowser/items?invoiceType=" + invoiceType + "&userName=" + localStorage.getItem('userName') + "&loadInvoiceData=" + invoiceCreateDate,
                dataType: "json",
                success: function (data, textStatus, jqXHR) {
                    SMW.salesInvoiceBrowser.onLoadDataSuccess(data, invoiceType);
                    SMW.ajax.hideLoader();
                }
            });
        }
    },

    onLoadDataSuccess: function (data, invoiceType) {
        SMW.salesInvoiceBrowser.drawTable(data, invoiceType);
        $("#SALES_INVOICE").show();
    },

    drawTable: function drawTable(data, invoiceType) {
        $("#SALES_INVOICE").empty();
        var row = $("<tr class='mtv-tr'><th style='vertical-align: middle;'>Customer Name</th><th style='vertical-align: middle;'>Customer Location</th><th style='vertical-align: middle;'>Invoice Number</th></tr>");
        $("#SALES_INVOICE").append(row); // this will append tr element to
        if (data.length == 0)
        {
            var dataRow = $("<tr class='mtv-tr'/>");
            $("#SALES_INVOICE").append(dataRow);
            dataRow.append($("<td style='vertical-align: middle; text-align: center;' colspan='3'>No Data found</td>"));
        } else {
            for (var i = 0; i < data.length; i++) {
                SMW.salesInvoiceBrowser.drawRow(data[i], invoiceType);
            }
        }
    },

    drawRow: function (rowData, invoiceType) {
        var row = $("<tr class='mtv-tr'/>");
        $("#SALES_INVOICE").append(row); //this will append tr element to table... keep its reference for a while since we will add cels into it
        row.append($("<td style='vertical-align: middle;'>" + rowData.description + "</td>"));
        row.append($("<td style='vertical-align: middle;'>" + rowData.location + "</td>"));
        // updated on 13-02-2018 - Balaji Manickam - 4i apps
        // added condition to check cancel items
        if (invoiceType == 'CANCEL') {
            row.append($("<td style='vertical-align: middle;'>" + rowData.invoiceNum + "</td>"));
        } else {
            row.append($("<td><a class='salesInvoice logo-color-blue' style='padding: 0; font-weight: unset; font-size: small; justify-content: center; border-radius: 15px;' href='#' onclick=SMW.salesInvoiceBrowser.printInvoice('" + rowData.invoiceNum + "','" + rowData.receiptNum + "','" + invoiceType + "');>" + rowData.invoiceNum + "</a></td>"));
        }
    },

    printInvoice: function (invNum, receiptNum, invoiceType) {
        var OrderAndReturnfolder;
        SMW.ajax.showLoader();
        if (invoiceType == "ORDER") {
            OrderAndReturnfolder = SMW.ip;
        } else {
            OrderAndReturnfolder = SMW.rp;
        }
        $.ajax({
            type: 'POST',
            url: url + "/rest-api/PrintInvoice/items?invoiceType=" + invoiceType + "&userName=" + localStorage.getItem("userName") + "&invoiceNum=" + invNum,
            dataType: "json",
            traditional: true,
            success: function (data, textStatus, jqXHR) {
                //console.log("success");					
                if (data.printInvoiceFlag == "T") {
                    window.open(OrderAndReturnfolder + invNum + ".pdf", '_blank');
                } else {
                    var error = $("<div class='smw-error'/>");
                    $('.smw-error').empty();
                    error.html("Encountered error while running Invoice Print Report!").insertAfter("#SALES_INVOICE").show().delay(3000).fadeOut();
                    $('.hide-cls').removeClass('hide-cls');
                }
                SMW.ajax.hideLoader();
            },
            error: function (data, textStatus, jqXHR) {
                SMW.ajax.hideLoader();
            }
        });
    }
};

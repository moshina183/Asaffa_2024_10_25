/* global SMW, url */

SMW.onHandQuantity = {
    _serviceLoadData: 'rest-api/OrderToInventory/items',
    init: async function () {
        await SMW.onHandQuantity.loadData();
        await SMW.onHandQuantity.onHandsInit();
    },
    onHandsInit: function () {
        SMW.onHandQuantity.bindEvents();
    },
    bindEvents: function () {
        this.unbindEvents();
        $('#btnHandsOnPrint').on('tap', this.onHandsReport);
    },
    unbindEvents: function () {
        $('#btnHandsOnPrint').unbind();
    },
    onHandsReport: function () {
        SMW.ajax.showLoader();
        $.ajax({
            type: 'POST',
            url: url + "/rest-api/PrintOnHandsReport/items?userName=" + localStorage.getItem("userName"),
            dataType: "json",
            traditional: true,
            success: function (data, textStatus, jqXHR) {
                if (data.printInvoiceFlag == "T") {
                    window.open(SMW.vsr + localStorage.getItem("userName") + '.pdf', '_blank');
                } else {
                    var error = $("<div class='smw-error'/>");
                    $('.smw-error').empty();
                    error.html("Encountered error while running On Hands Report!").insertAfter("#btnHandsOnPrint").show().delay(3000).fadeOut();
                    $('.hide-cls').removeClass('hide-cls');
                }
                SMW.ajax.hideLoader();
            },
            error: function (data, textStatus, jqXHR) {
                SMW.ajax.hideLoader();
            }
        });
    },

    loadData: function () {
        var data = {userName: localStorage.getItem('userName')};
        SMW.ajax.call(url + "rest-api/OrderToInventory/items?userName=" + localStorage.getItem('userName'), data, SMW.onHandQuantity.onLoadDataSuccess, true);
    },

    onLoadDataSuccess: function (data) {
        SMW.onHandQuantity.drawTable(data);
    },
    drawTable: function (data) {
        $("#ON_HANDS_TABLE tr:gt(0)").remove();
        if (data.length == 0)
        {
            var row = $("<tr class='odr-tr'/>");
            $("#ON_HANDS_TABLE").append(row); // this will append tr element to
            row.append($("<td colspan='3'>No Data found</td>"));
        } else {
            for (var i = 0; i < data.length; i++) {
                SMW.onHandQuantity.drawRow(data[i]);
            }
        }
    },
    drawRow: function (rowData) {
        var row = $("<tr class='mtv-tr'/>");
        $("#ON_HANDS_TABLE").append(row); //this will append tr element to table... keep its reference for a while since we will add cels into it
        row.append($("<td style='display:none'>" + rowData.itemId + "</td>"));
        row.append($("<td>" + rowData.description + "</td>"));
        row.append($("<td> <input type='text' class='mtv-input' data-headerId='" + rowData.headerId + "' id='" + rowData.itemId + "' value='" + rowData.quantity + "'></input></td>"));
        $("#ON_HANDS_TABLE").find("input,button,textarea").attr("disabled", "disabled");
    }

};

/* global SMW */

SMW.vanSalesReport = {

    _serviceLoadData: 'rest-api/PrintVanSalesReport/items',
    init: function () {
        $("#vanSalesCreateDate").val('');
        SMW.vanSalesReport.loadData();
        SMW.vanSalesReport.bindEvents();
    },
    
    bindEvents: function() {
        this.unbindEvents();
        $("#vanSalesReport").on('tap', SMW.vanSalesReport.printVanSales);
    },
    
    unbindEvents: function() {
        $('#vanSalesReport').unbind();
    },
    
    loadData: function () {
        $("#vanSalesCreateDate").datepicker({dateFormat: 'dd-M-yy'});
        var maxDate = new Date();
        $("#vanSalesCreateDate").datepicker("option", 'maxDate', maxDate);
    },

    printVanSales: function () {
        var vanSalesCreateDate = $("#vanSalesCreateDate").val();
        SMW.ajax.showLoader();
        var arr = vanSalesCreateDate.split('-');
        var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
        var i = 0;
        for (i; i <= months.length; i++) {
            if (months[i] == arr[1])
            {
                break;
            }
        }
        if (i < 9) {
            i = i + 1;
            i = '0' + i;
        } else {
            i = i + 1;
        }
        var formatddate = i + '' + arr[0] + '' + arr[2];
        $.ajax({
            type: 'POST',
            url: url + "/rest-api/PrintVanSalesReport/items?userName=" + localStorage.getItem("userName") + "&vanSalesCreateDate=" + vanSalesCreateDate,
            dataType: "json",
            traditional: true,
            success: function (data, textStatus, jqXHR) {			
                if (data.printInvoiceFlag == "T") {
                    window.open(SMW.vsr + localStorage.getItem("userName") + formatddate + '.pdf', '_blank');
                } else {
                    var error = $("<div class='smw-error'/>");
                    $('.smw-error').empty();
                    error.html("Encountered error while running Van Sales Day Report!").insertAfter("#vanSalesCreateDate").show().delay(3000).fadeOut();
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

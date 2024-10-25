/* global SMW */

SMW.ajax = {
    httpErrors: {
        '0': 'Connection Error',
        '1': 'Response not expected',
        '2': 'Authentication',
        '3': 'Redirectional',
        '4': 'Bad Request',
        '5': 'Server'
    },
    _pickError: function (errCode) {
        var errInt = parseInt(errCode / 100);
        var errMsg = this.httpErrors[errInt];
        return errMsg;
    },
    _showError: function (xhr, status, error) {
        var cpg = $.mobile.activePage;
        var errObj = $("<div>");
        var errBtn = $('<div>')
                .attr('data-role', 'button')
                .attr('data-icon', 'delete')
                .attr('data-iconpos', 'notext')
                .addClass('ui-btn-right')
                .html('Close')
                .appendTo(errObj);
        errBtn.button();
        errBtn.on('click', function () {
            errObj.popup('close').remove();
        });
        var errObjHeader = $("<h4>")
                .appendTo(errObj);
        var errObjDesc = $("<div>")
                .appendTo(errObj);
        errObj.css('padding', '15px');
        errObjHeader.html(SMW.ajax._pickError(xhr.status) + ' error');
        errObjDesc.html(error + '<br><br> Please contact System Administrator / Check Connectivity issues.');
        errObj.appendTo(cpg);
        errObj.popup().popup("open");
    },
    call: function (url, data, sCallback, isPost) {
        this.callCType(url, data, sCallback, isPost, 'application/json; charset=utf-8');
    },
    callCType: function (url, data, sCallback, isPost, ctype) {
        this.callCTypeErrorCallBack(url, data, sCallback, isPost, ctype);
    },
    callCTypeErrorCallBack: function (url, data, sCallback, isPost, ctype, eCallback) {
        if (!url) {
            alert('Please provide a valid URL.');
            return;
        }
        var params = {
            url: url,
            dataType: 'json',
            type: isPost ? 'POST' : 'GET',
            contentType: ctype,
            beforeSend: function () {
                SMW.ajax.showLoader();
            },
            success: function (result, status, xhr) {
                if (sCallback) {
                    sCallback(result, status, xhr);
                }
                SMW.ajax.hideLoader();
            },
            error: function (xhr, status, error) {
                SMW.ajax.hideLoader();
                if (eCallback) {
                    eCallback(xhr, status, error);
                } else {
                    SMW.ajax._showError(xhr, status, error);
                }
            },
            complete: function () {
                SMW.ajax.hideLoader();
            }
        };
        if (data) {
            params.data = data;
        } else {
            params.data = {};
        }
        $.ajax(params);
    },

    showLoader: function () {
        // Start showing Loader before sending..
        $.mobile.loading('show', {
            text: 'Loading..',
            textVisible: true
        });
        $(".ui-loader").css({
            left: '60%',
            width: '77px'
        });
        // Disable UI
        $("body").append('<div class="ui-disabler"></div>');
    },

    hideLoader: function () {
        $(".ui-disabler").remove();
        $.mobile.loading('hide');
    }
};

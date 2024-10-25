/* global SMW */

SMW.moveToInventory = {
    _serviceMvToInv: 'rest-api/SalesEntry/confirmItems?orderType=MOVE_ORDERINV',
    _serviceSubInv: 'rest-api/OrderToInventory/subInventory?',
    _serviceLoc: 'rest-api/OrderToInventory/subInvLocations',
    _serviceLoadData: 'rest-api/OrderToInventory/items',
    init: function () {
        SMW.moveToInventory.loadData();
        SMW.moveToInventory.bindEvents();
        SMW.moveToInventory.isEnabled();
    },

    bindEvents: function () {
        this.unbindEvents();
        $("#BTN_MOV_INVTRY_CONFIRM").on('click', SMW.moveToInventory.onBtnInvItemsConfirmClick);
        $("#BTN_MOV_INVTRY_CANCEL").on('click', SMW.moveToInventory.onBtnItemsCancelClick);
        $("#itemsMoveTable").on('keyup', '.mtv-input', SMW.moveToInventory.onQuantityKeyUp);
        $("#smw-ddloc").on('change', SMW.moveToInventory.isEnabled);
        $("#smw-ddsi").on('change', SMW.moveToInventory.loadLocationData);

    },
    unbindEvents: function () {
        $('#BTN_MOV_INVTRY_CONFIRM').unbind();
        $("#BTN_MOV_INVTRY_CANCEL").unbind();
        $("#itemsMoveTable").unbind();
        $("#smw-ddloc").unbind();
        $("#smw-ddsi").unbind();
    },

    loadSubInventoryData: function () {
        //SMW.ajax.showLoader();
        var data = {'organizationId': localStorage.getItem("organizationId")};

        SMW.ajax.callCType(url + SMW.moveToInventory._serviceSubInv, data, SMW.moveToInventory.onSubInvSuccess, true);

    },

    onSubInvSuccess: function (resp) {
        // console.log("resp is "+resp);
        SMW.moveToInventory.fillSubInventory(resp);
    },
    loadLocationData: function () {

        var subinvCode = '';
        subinvCode = $("#smw-ddsi option:selected").val();
        var data = {'subInvCode': subinvCode, 'organizationId': localStorage.getItem("organizationId")};
        SMW.ajax.callCType(url + SMW.moveToInventory._serviceLoc, data, SMW.moveToInventory.onLoadLocSuccess, true, 'application/x-www-form-urlencoded');

    },

    onLoadLocSuccess: function (data) {
        // console.log("success loadLocationData");
        SMW.moveToInventory.fillLocation(data);
    },

    fillSubInventory: function (data) {
        var SubInventory = $("#smw-ddsi");
        $("#smw-ddsi").empty();
        SubInventory.append("<option value='Select'>Select</option>");

        $.each(data, function (index, value)
        {
            SubInventory.append($("<option />").val(value.subInvCode).text(value.subInvCode));
        });

    },
    fillLocation: function (data)
    {
        var locations = $("#smw-ddloc");
        $("#smw-ddloc").empty();
        locations.append("<option value='0'>Select</option>");
        $.each(data, function (index, value)
        {
            locations.append($("<option />").val(value.locationId).text(value.location));
        });
    },
    onBtnInvItemsConfirmClick: function () {



        if ($('#itemsMoveTable .mtv-tr:visible').length > 0)
        {
            var subinv = $('#smw-ddsi').val();
            //console.log('sub inv',subinv);
            var loc = $('#smw-ddloc').val();
            // console.log('sub loc',loc);
            var dataArr = new Array();
            $(":checkbox:checked").each(function () {
                if ($(this).val() == 'on') {
                    var eachObj = {};
                    var parentTr = $(this).closest('tr');
                    var id = $(parentTr).find(".mtv-input").attr('id');
                    var quantity = $(parentTr).find(".mtv-input").val();
                    var headerId = $(parentTr).find(".mtv-input").attr('data-headerId');
                    eachObj.itemId = id;
                    eachObj.quantity = quantity;
                    eachObj.headerId = headerId;
                    dataArr.push(eachObj);
                }
            });
            //make ajax call to server to store data.
            var data = JSON.stringify({'customerName': null,
                'transactionType': "MOVE_ORDER",
                'customerSiteId': 0,
                'customerSite': null,
                'paymentMethod': null,
                'vatRate': null,
                'userName': localStorage.getItem("userName"), // need to be added from the cookie
                'orgId': localStorage.getItem("organizationId"), // need to be added from the cookie
                'custAccountId': 0,
                'transactionTypeId': 0,
                'subInvCode': subinv,
                'transType': "MOVE_ORDERINV",
                'totalAmount': 0.0,
                'locationId': loc,
                'rentalKnockOff': null,
                'items': dataArr});

            SMW.ajax.call(url + SMW.moveToInventory._serviceMvToInv, data, SMW.moveToInventory.onConfirmSucess, true);



        }
    },

    onConfirmSucess: function (data) {

        //console.log('Success function on Move order to Inventory ');
        var confirmMsg = $("#CONFIRM_MSG");
        var msg = '';
        $('#CONFIRM_PAGE_HEADER').html("Material Transfer to Stores").show();

        if (data.Message = "success")
        {
            var orderNumber = "#";
            if (data.orderNumber != null) {
                orderNumber = data.orderNumber;
            }

            msg = "Move Order #" + orderNumber + " Created Successfully.";
            confirmMsg.html(msg).show();
        } else
        {

            msg = "Contact Sys Admin as the data not saved to Database.";
        }

        $.mobile.changePage('#PAGE_CONFIRMATION');



    },
    onQuantityKeyUp: function () {
        var data = $(this).val();
        var regx = /\b0*[0-9][0-9]{0,3}\b/;

        if (data.match(regx) == null)
        {
            alert("Only numbers from 1 to 9999 are allowed.");
        }
    },
    isEnabled: function () {
        var loc = $("#smw-ddloc").val();
        var subinv = $("#smw-ddsi").val();
        if (loc == 'Select' || subinv == 'Select') {
            $('#BTN_MOV_INVTRY_CONFIRM').prop('disabled', true);
        } else {
            $('#BTN_MOV_INVTRY_CONFIRM').prop('disabled', false);
        }
    },
    loadData: function () {

        var data = {userName: localStorage.getItem('userName')}
        SMW.ajax.call(url + "rest-api/OrderToInventory/items?userName=" + localStorage.getItem('userName'), data, SMW.moveToInventory.onLoadDataSuccess, true);

    },

    onLoadDataSuccess: function (data) {
        // console.log("success");
        if (data.length > 0) {
            SMW.moveToInventory.loadSubInventoryData();
        }
        SMW.moveToInventory.drawTable(data);
    },

    drawTable: function drawTable(data) {
        $("#itemsMoveTable tr:lt(-1)").remove();
        $("#itemsMoveTable tr:first").remove();
        $("#itemsMoveTable").append("<tr><th></th><th style='display:none'>Id</th> <th>Description</th> <th>Quantity</th></tr>");

        if (data.length > 0)
        {
            $('#ERROR_DIV').css("display", "none");
            $('#CONTENT_MOVE_TO_INV').css("display", "block");
            for (var i = 0; i < data.length; i++) {
                SMW.moveToInventory.drawRow(data[i]);
                $("#smw-ddloc").on('change', SMW.moveToInventory.isEnabled);

            }
        } else {

            $('#CONTENT_MOVE_TO_INV').css("display", "none");
            $('#ERROR_DIV').css("display", "block");
            $('#ERROR_DIV').html('<h5>No Data found</h5');
        }

    },
    drawRow: function (rowData) {
        // console.log('drawRow');
        var row = $("<tr class='mtv-tr'/>");
        $("#itemsMoveTable").append(row); //this will append tr element to table... keep its reference for a while since we will add cels into it
        row.append($("<td><input type='checkbox' id='itemcheck' name='itemcheck'></td>"));
        row.append($("<td style='display:none'>" + rowData.itemId + "</td>"));
        row.append($("<td>" + rowData.description + "</td>"));
        row.append($("<td> <input type='text' class='mtv-input' data-headerId='" + rowData.headerId + "' id='" + rowData.itemId + "' value='" + rowData.quantity + "'></input></td>"));
        $("#itemsMoveTable").find("input:text,button,textarea").attr("disabled", "disabled");
    }

};

// the maximum allowed multiplier
var MAX_MULTIPLIER = 999;

// the maximum number of page links to be displayed
var MAX_PAGE_LINKS = 5;

// global description mapping for enumerated values
var filterTypeDescription = {};
filterTypeDescription['ADJUSTMENT_FILTER'] = "Adjustments Filter";
filterTypeDescription['BRANCH_OVERRIDE_FILTER'] = "Branch Overrides Filter";
filterTypeDescription['FREIGHT_FILTER'] = "Freight Filter";
filterTypeDescription['INPUT_PRICE_FILTER'] = "Input Price Filter";
filterTypeDescription['GLOBAL_FILTER'] = "Global Search";

// JavaScript Document
$(document).ready(function(){

	if ($('#jquery_theme_link').size() > 0) {
		$('#jquery_theme_link').remove();
	}

   var ajaxTableLoader;
   $(".filter-search :text").keyup(function() {
         var val = $(this).val().toLowerCase();
         if (val == '') {
        	 $(this).closest('.column').find('.scroll li').show();
         } else {
        	 $(this).closest('.column').find('.scroll li').each(function(el) {
        		 if ($(this).hasClass('hasChildren')) {
        			 $(this).show();
        			 // ignore, is parent node
        		 } else {
        			 if ($(this).find('label').text().toLowerCase().indexOf(val) > -1) {
        				 if ($(this).closest('ul').hasClass('children')) {
        					 $(this).closest('.hasChildren').addClass("opened");
        					 $(this).closest('ul').show();
        				 }
        				 $(this).show();
        			 } else {
        				 $(this).hide();
        			 }
        		 }
        	 });
        	 // hide parents if no visible children
        	 $(this).closest('.column').find('.scroll li.hasChildren').each(function(el) {
        		 if ($(this).find("li:visible").size() == 0) {
        			 $(this).hide();
        		 }
        	 });
         }
    });

   $("#modal-create-user .search-box, #modal-update-user .search-box, #modal-add-branches .search-box").keyup(function() {
         var val = $(this).val().toLowerCase();
         if (val == '') {
        	 $(this).parent().next().find('li').show();
         } else {
        	 $(this).parent().next().find('li').each(function(el) {
        		 if ($(this).find('span').text().toLowerCase().indexOf(val) > -1) {
        			 $(this).show();
        		 } else {
        			 $(this).hide();
        		 }
        	 });
         }
         return false;
    });

    // on initial load, synchronize the checkbox status with the css
    // not sure why the search assembler prefers the css instead of the value for state checking
    // probably because some browsers like to retain checkbox state
    $(".filter-section .column .scroll li input:checked").each(function() {
    	if (!($(this).closest('li')).hasClass('selected')) {
            $(this).removeAttr('checked');
    	}
    });

	$(".select-all").click(function(){
		$(this).toggleClass("checked");
		if ($(this).hasClass("checked")){
			var type=$(this).parent().attr("class").split(" ")[1];
			$(".column."+type).find("input[type='checkbox']").attr("checked","checked");
			$(".column."+type).find("li").addClass("selected");
		}
		else{
			var type=$(this).parent().attr("class").split(" ")[1];
			$(".column."+type).find("input[type='checkbox']").removeAttr("checked");
			$(".column."+type).find("li").removeClass("selected");
		}

	    if ($(this).parent().hasClass('category')) {
        	updateCategoryFilters();
        } else if ($(this).parent().hasClass('market')) {
        	updateMarketFilters();
        }

	});


    //AJAX for My Pricing table
    if($('#my-pricings-table').length > 0){
    	doPricingSearch();
    }

    //AJAX for My Saved Filter table
    if($('#my-saved-filter-table').length > 0){
    	doSavedFilterSearch();
    }

    //Login
    $('#login').live('click',function(){
        $('#LoginAction').submit();
    });

//    $('.login-flyout a').live('click',function(){
        //$('.login-flyout').hide();
//    });

    //Login error
//    $('.login-error').live('click',function(){
        //$('.login-form .comfirm').show();
        //$('.login-form .row label').addClass('error');
        //$('.login-form .text').addClass('text-error');
//    });

    //Switch tab
    $('.switch-tab').live('click',function(){
        $('.tab-list a').removeClass('active');
        $('.tab-list a').eq(1).addClass('active');
        $('.tab-section').hide();
        $('.my-saved-filter').show();
    });

    //Get URL
    function Request(strName){
        var strHref = location.href;
        var intPos = strHref.indexOf("?");
        var strRight = strHref.substr(intPos + 1);
        var arrTmp = strRight.split("&");
        for(var i = 0; i < arrTmp.length; i++) {
            var arrTemp = arrTmp[i].split("=");
            if(arrTemp[0].toUpperCase() == strName.toUpperCase()) return arrTemp[1];
        }
        return "";
    }

    function setHeight() {
        var tableHeight = Math.max($('.table-other-type .result-table').height(), 390);
        $('.table-other-type .toggle').css({height:tableHeight-31});
        $('.table-other-type .tree-inner').css({height:tableHeight-40});
        $('.table-other-type .scroll-pane').css({ height: tableHeight - 46 });
        tableHeight = Math.max($('.table-CW .result-table').height(), 390);
        $('.table-CW .toggle').css({height:tableHeight-31});
        $('.table-CW .tree-inner').css({height:tableHeight-40});
        $('.table-CW .scroll-pane').css({ height: tableHeight - 46 });
        if($('.scroll-pane').length){
            $('.scroll-pane').jScrollPane();
        }
    }

    if(Request('filter')=="hide"){
        $('.filter-body').hide();
    }
    if(Request('tab')=="other"){
        get_other_search();
        get_other_input_prices();
        get_other_adjustments();
        get_other_freight();
        get_other_branch_overrides();
        $('.comfirm-filter').hide();
        $('.table-other-type').show();
        setHeight();
    }
    if(Request('tab')=="cw"){
        get_sw_search();
        get_sw_input_prices();
        get_sw_adjustments();
        get_sw_freight();
        get_sw_branch_overrides();
        $('.comfirm-filter').hide();
        $('.table-CW').show();
        setHeight();
    }
    if(Request('modal')=="edit-vendor"){
        closeModal();
        loadModal('#modal-update-vendor');
    }

    //List for manage filters
    $('#wrapper #header .save-filters-flyout li:even').addClass('even');
    $('.saved-filters a').live('click',function(){
        if($('#wrapper #header .save-filters-flyout').is(':hidden')){
            $('#wrapper #header .save-filters-flyout').show();
            $('#wrapper #header .save-filters-flyout').css({width:$(this).width()+2});
        }else{
            $('#wrapper #header .save-filters-flyout').hide();
        }
    });
    $('#wrapper #header .save-filters-flyout .btn-close').live('click',function(){
        $('#wrapper #header .save-filters-flyout').hide();
    });

    //Data
    function setOdd(){
        $('.data').each(function(){
            $(this).find('tbody tr:odd').addClass('odd');
        });

        $('.column table').each(function(){
            $(this).find('tbody tr:odd').addClass('odd');
        });

        $('.table-data table').each(function(){
            $(this).find('tbody tr:odd').addClass('odd');
        });
    }
    setOdd();

    //Tab
    $('.tab .tab-list li a').live('click',function(){
    	if($(this).parents('.freight-rate').length) {
    		return;
    	}
        if(!$(this).parents('ul').hasClass('disable')){
            $('.tab .tab-list li a').removeClass('active');
            $(this).addClass('active');
            var i = $('.tab .tab-list li a').index(this);
            $('.tab .tab-container .tab-section').hide();
            $('.tab .tab-container .tab-section').eq(i).show();
            $('.freight-rate .search-section div').hide();
            $('.freight-rate .search-section div').eq(i).show();
            $('.tip-for-tbody').hide();
        }
    });

    //Pagination
    $('.pagination .page-jump .text').live('focus',function(){
        if($(this).val() == 'Page #'){
            $(this).val('');
            $(this).removeClass('default');
        }
    });

    $('.pagination .page-jump .text').live('blur',function(){
        if($(this).val() == ''){
            $(this).val('Page #');
            $(this).addClass('default');
        }
    });

    //Modal
    positionModal = function(){
        var wWidth  = window.innerWidth;
        var wHeight = window.innerHeight;

        if (wWidth==undefined) {
            wWidth  = document.documentElement.clientWidth;
            wHeight = document.documentElement.clientHeight;
        }

        var boxLeft = parseInt((wWidth / 2) - ( $("#modal").width() / 2 ));
        var boxTop  = parseInt((wHeight / 2) - ( $("#modal").height() / 2 ));

        // position modal
        if(boxTop > 0){
            $('#modal').css('position','fixed');
            $("#modal").css({
                'margin': + boxTop +'px auto 0 ' + boxLeft + 'px'
            });
        }else{
            $('#modal').css('position','absolute');
            $("#modal").css({
                'margin': '230px auto 0 ' + boxLeft + 'px'
            });
        }

        $("#modal-background").css("opacity", "0.6");

        if ($("body").height() > $("#modal-background").height()){
            $("#modal-background").css("height", $("body").height() + "px");
        }

        $(window).scrollTop(0);
    };

    $(window).resize(function(){
        positionModal();
    });

    loadModal = function(itemId) {
        $('#modal-background').show();
        $(itemId).show();
        positionModal();
        $('.error-field').hide();
        $('.error-text').removeClass('error-text');
        $('.error').removeClass('error');
    };

    closeModal = function() {
        $('#modal-background').hide();
        $('.modal').hide();
    };

    $('.close-modal').live('click',function(){
        closeModal();
    });

    $('.modal-redirect').live('click',function(){
        closeModal();
        if($(this).attr('rel')){
            loadModal('#'+$(this).attr('rel'));
        }
    });

    $('#my-saved-filter-table .btn-delete').live('click',function(){
        closeModal();

        var note = 'notification-filter-' + $(this).attr("rel");
        var flyout = 'my-filters-list-' + $(this).attr("rel");
        var data = {"filterId" : $(this).attr("rel")};
        $('#modal-delete-confirmation .btn-yes').unbind('click').bind('click', function() {
        	closeModal();
        	$.ajax({
				url : datactx + "/ajax/DeleteSavedFilter.action",
				type : "POST",
				dataType : "json",
				data : data,
				success : function(json) {
					// if filter was in the notification section, remove it.
					$('#' + note).remove();
					$('#' + flyout).remove();
					if ($('.comfirm > div > p').size() == 0) {
						$('.comfirm').remove();
					}
					$('.my-saved-filter input[name="userFilterRecordCriteria.page"]').val(1);
					doSavedFilterSearch();
		        	loadModal('#modal-delete');
				},
				error : function(req) {
					alert("There was a problem processing your request. Please contact the site administrator.");
				}
			});
        });

        loadModal('#modal-delete-confirmation');
    });

    //Delete modal
    $('#searchForm .btn-delete').live('click',function(){
        closeModal();

        // no data
        if ($("#searchForm table.data tbody tr").size() == 0) {
        	return false;
        }

        var numCheckedItems = $('#searchForm input[name="deleteIds"]:checked').size();
        // check how many are selected
        if (numCheckedItems == 0) {
        	return false;
        }

        var entityName = '';
        var successMessage = '';
        var deleteURL = '';
        if ($('#resultEntityType').val() == 'Vendor') {
        	entityName = numCheckedItems > 1 ? 'Vendors' : 'Vendor';
        	successMessage = numCheckedItems > 1 ? 'Vendors have been successfully deleted.' : 'Vendor has been successfully deleted.';
        	deleteURL = datactx + "/ajax/DeleteVendors.action";
        } else if ($('#resultEntityType').val() == 'Category') {
        	entityName = numCheckedItems > 1 ? 'Categories' : 'Category';
        	successMessage = numCheckedItems > 1 ? 'Categories have been successfully deleted.' : 'Category has been successfully deleted.';
        	deleteURL = datactx + "/ajax/DeleteCategories.action";
        } else if ($('#resultEntityType').val() == 'ShipPoint') {
        	entityName = numCheckedItems > 1 ? 'Shippoints' : 'Shippoint';
        	successMessage = numCheckedItems > 1 ? 'Shippoints have been successfully deleted.' : 'Shippoint has been successfully deleted.';
        	deleteURL = datactx + "/ajax/DeleteShipPoints.action";
        } else if ($('#resultEntityType').val() == 'Product') {
        	entityName = numCheckedItems > 1 ? 'Products' : 'Product';
        	successMessage = numCheckedItems > 1 ? 'Products have been successfully deleted.' : 'Product has been successfully deleted.';
        	deleteURL = datactx + "/ajax/DeleteProducts.action";
        } else if ($('#resultEntityType').val() == 'Market') {
        	entityName = numCheckedItems > 1 ? 'Markets' : 'Market';
        	successMessage = numCheckedItems > 1 ? 'Markets have been successfully deleted.' : 'Market has been successfully deleted.';
        	deleteURL = datactx + "/ajax/DeleteMarkets.action";
        } else if ($('#resultEntityType').val() == 'Branch') {
        	entityName = numCheckedItems > 1 ? 'Branches' : 'Branch';
        	successMessage = numCheckedItems > 1 ? 'Branches have been successfully deleted.' : 'Branch has been successfully deleted.';
        	deleteURL = datactx + "/ajax/DeleteBranches.action";
        } else if ($('#resultEntityType').val() == 'User') {
        	entityName = numCheckedItems > 1 ? 'Users' : 'User';
        	successMessage = numCheckedItems > 1 ? 'Users have been successfully deleted.' : 'User has been successfully deleted.';
        	deleteURL = datactx + "/ajax/DeleteUsers.action";
        } else if ($('#resultEntityType').val() == 'Destination') {
        	entityName = numCheckedItems > 1 ? 'Destinations' : 'Destination';
        	successMessage = numCheckedItems > 1 ? 'Destinations have been successfully deleted.' : 'Destination has been successfully deleted.';
        	deleteURL = datactx + "/ajax/DeleteDestinations.action";
        } else if ($('#resultEntityType').val() == 'Origination') {
        	entityName = numCheckedItems > 1 ? 'Originations' : 'Origination';
        	successMessage = numCheckedItems > 1 ? 'Originations have been successfully deleted.' : 'Origination has been successfully deleted.';
        	deleteURL = datactx + "/ajax/DeleteOriginations.action";
        } else if ($('#resultEntityType').val() == 'OriginationToStateFreightRate') {
        	entityName = numCheckedItems > 1 ? 'OriginationToStateFreightRates' : 'OriginationToStateFreightRate';
        	successMessage = numCheckedItems > 1 ? 'OriginationToStateFreightRates have been successfully deleted.' : 'OriginationToStateFreightRate has been successfully deleted.';
        	deleteURL = datactx + "/ajax/DeleteOriginationToStateFreightRates.action";
        } else if ($('#resultEntityType').val() == 'OriginationToDestinationFreightRate') {
        	entityName = numCheckedItems > 1 ? 'OriginationToDestinationFreightRates' : 'OriginationToDestinationFreightRate';
        	successMessage = numCheckedItems > 1 ? 'OriginationToDestinationFreightRates have been successfully deleted.' : 'OriginationToDestinationFreightRate has been successfully deleted.';
        	deleteURL = datactx + "/ajax/DeleteOriginationToDestinationFreightRates.action";
        }

        var data = $(this).closest('form').serialize();

        // setup modal
        $('#modal-delete-confirmation .one-line').text('Are you sure you want to delete the selected ' + entityName + '?');
        $('#modal-delete .modal-container p').text(successMessage);
        $('#modal-delete-confirmation .btn-yes').unbind('click').bind('click', function() {
        	closeModal();
        	$.ajax({
				url : deleteURL,
				type : "POST",
				dataType : "json",
				data : data,
				success : function(json) {
					resetAdminSearchPageNumber();
					doAdminSearch();
		        	loadModal('#modal-delete');
				},
				error : function(req) {
					alert("There was a problem processing your request. Please contact the site administrator.");
				}
			});
        });
        loadModal('#modal-delete-confirmation');
    });

    //Destination modal
    $('.btn-create-destination').live('click',function(){
        closeModal();
        $('#modal-add-destination input:text').val('');
        $('#modal-add-destination select option').first().prop("selected", "selected");
        loadModal('#modal-add-destination');
    });

    $('.destinations .data a').live('click',function(){
        closeModal();
        // the checkbox selection value
        var id = $(this).parents('tr').find('td:eq(0) input').first().attr("value");
        // load selected category then show in modal
        $.ajax({
	        type: "GET",
	        url: datactx + "/ajax/GetDestination.action?retrieveId=" + id,
	        dataType: "json",
	        success: function(json) {
		        // populate the popup fields
		        var entity = json.entity;
		        var popup = $('#modal-update-destination');
		        $(popup).find('input[name="entity.id"]').val(entity.id);
		        $(popup).find('.brStrong').html(entity.br);
		        $(popup).find('input[name="entity.brLocation"]').val(entity.brLocation);
            	$(popup).find('select[name="entity.state.id"]').val(entity.state.id);
		        $(popup).find('input[name="entity.zipCode"]').val(entity.zipCode);
		        // show the modal
		        loadModal('#modal-update-destination');
	        },
	        error:function(){
	        	alert("There was a problem processing your request. Please contact the site administrator.");
	        }
        });
    });

    //Origin modal
    $('.btn-create-origin').live('click',function(){
        closeModal();
        $('#modal-add-origin input:text').val('');
        $('#modal-add-origin select option').first().prop("selected", "selected");
        loadModal('#modal-add-origin');
    });

    $('.origins .data a').live('click',function(){
        closeModal();
        // the checkbox selection value
        var id = $(this).parents('tr').find('td:eq(0) input').first().attr("value");
        // load selected category then show in modal
        $.ajax({
	        type: "GET",
	        url: datactx + "/ajax/GetOrigination.action?retrieveId=" + id,
	        dataType: "json",
	        success: function(json) {
		        // populate the popup fields
		        var entity = json.entity;
		        var popup = $('#modal-update-origin');
		        $(popup).find('input[name="entity.id"]').val(entity.id);
		        $(popup).find('input[name="entity.name"]').val(entity.name);
            	$(popup).find('select[name="entity.state.id"]').val(entity.state.id);
            	$(popup).find('select[name="entity.type.id"]').val(entity.type.id);
		        $(popup).find('input[name="entity.zipCode"]').val(entity.zipCode);
		        // show the modal
		        loadModal('#modal-update-origin');
	        },
	        error:function(){
	        	alert("There was a problem processing your request. Please contact the site administrator.");
	        }
        });
    });

    // User modal
    $('.btn-create-user').live('click',function(){
        closeModal();
        $('#modal-create-user input:text').val('');
        $('#modal-create-user .seach-branches input').val('');
        $('#modal-create-user .rolesHidden').remove();
        $('#modal-create-user .branchesHidden').remove();
        $('#modal-create-user .allBranches_id li.selected').removeClass('selected');
        $('#modal-create-user .allRoles_id input').removeAttr('checked');
        $('#modal-create-user .allBranches_id input').removeAttr('checked');
        $('#modal-create-user .allBranches_id li').show();
        $('#modal-create-user .allBranches_id').prev().find('.text').val('Search Branch').focus().blur();
        loadModal('#modal-create-user');
    });

    $('.link-edit-user').live('click',function(){
        closeModal();
        $('#modal-update-user .seach-branches input').val('');
        $('#modal-update-user .rolesHidden').remove();
        $('#modal-update-user .branchesHidden').remove();
        $('#modal-update-user .allBranches_id li.selected').removeClass('selected');
        $('#modal-update-user .allRoles_id input').removeAttr('checked');
        $('#modal-update-user .allBranches_id input').removeAttr('checked');
        $('#modal-update-user .allBranches_id li').show();
        $('#modal-update-user .allBranches_id').prev().find('.text').val('Search Branch').focus().blur();
        // the checkbox selection value
        var id = $(this).parents('tr').find('td:eq(0) input').first().attr("value");
        // load selected category then show in modal
        $.ajax({
	        type: "GET",
	        url: datactx + "/ajax/GetUser.action?retrieveId=" + id,
	        dataType: "json",
	        success: function(json) {
		        // populate the popup fields
		        var entity = json.entity;
		        var popup = $('#modal-update-user');
		        $(popup).find('input[name="entity.id"]').val(entity.id);
		        $(popup).find('input[name="entity.firstName"]').val(entity.firstName);
		        $(popup).find('input[name="entity.lastName"]').val(entity.lastName);
		        $(popup).find('input[name="entity.title"]').val(entity.title);
		        $(popup).find('input[name="entity.phone"]').val(entity.phone);
		        $(popup).find('input[name="entity.mobile"]').val(entity.mobile);
		        $(popup).find('input[name="entity.fax"]').val(entity.fax);
		        $(popup).find('input[name="entity.email"]').val(entity.email);
		        $(popup).find('input[name="entity.password"]').val(entity.password);

		        $.each(entity.roles, function(idx, role) {
		        	$('#modal-update-user .allRoles_id input').each(function() {
		        		var value = $(this).val();
		        		if(value == role.id) {
		        			$(this).attr('checked', 'checked');
		        		}
		        	});
		        });
		        $.each(entity.branches, function(idx, branch) {
		        	$('#modal-update-user .allBranches_id input').each(function() {
		        		var value = $(this).val();
		        		if(value == branch.id) {
		        			$(this).attr('checked', 'checked');
		        			$(this).parent().addClass('selected');
		        		}
		        	});
		        });

            	// split the phone number parts
                $(popup).find('.phoneNumber').each(function(){
                	var parts = $(this).find('input:eq(3)').val().split("-");
                	if (parts.length > 0) $(this).find('input:eq(0)').val(parts[0]);
                	if (parts.length > 1) $(this).find('input:eq(1)').val(parts[1]);
                	if (parts.length > 2) $(this).find('input:eq(2)').val(parts[2]);
                });
		        // show the modal
		        loadModal('#modal-update-user');
	        },
	        error:function(){
	        	alert("There was a problem processing your request. Please contact the site administrator.");
	        }
        });
    });
    $('#selectAllRoles').live('click', function() {
    	if($(this).is(':checked')) {
    		$('#modal-add-roles .allRoles_id input').attr('checked', 'checked');
    	} else {
    		$('#modal-add-roles .allRoles_id input').removeAttr('checked');
    	}
    });
    $('#modal-add-roles .allRoles_id input').live('click', function() {
    	if($('#modal-add-roles .allRoles_id input').length == $('#modal-add-roles .allRoles_id  input:checked').length) {
    		$('#selectAllRoles').attr('checked', 'checked');
    	} else {
    		$('#selectAllRoles').removeAttr('checked');
    	}
    });
    $('#selectAllBranches').live('click', function() {
    	if($(this).is(':checked')) {
    		$('#modal-add-branches .allBranches_id li').addClass('selected');
    		$('#modal-add-branches .allBranches_id input').attr('checked', 'checked');
    	} else {
    		$('#modal-add-branches .allBranches_id li').removeClass('selected');
    		$('#modal-add-branches .allBranches_id input').removeAttr('checked');
    	}
    });
    $('#modal-add-branches .allBranches_id input').live('click', function() {
    	if($('#modal-add-branches .allBranches_id input').length == $('#modal-add-branches .allBranches_id  input:checked').length) {
    		$('#selectAllBranches').attr('checked', 'checked');
    	} else {
    		$('#selectAllBranches').removeAttr('checked');
    	}
    });
    $('.link-add-roles').live('click', function() {
    	closeModal();
    	runBrowserHacks();
    	$('#selectAllRoles').removeAttr('checked');
        $('#modal-add-roles .rolesHidden').remove();
        $('#modal-add-roles .allRoles_id input').removeAttr('checked');
        // the checkbox selection value
        var id = $(this).parents('tr').find('td:eq(0) input').first().attr("value");
        // load selected category then show in modal
        $.ajax({
	        type: "GET",
	        url: datactx + "/ajax/GetUser.action?retrieveId=" + id,
	        dataType: "json",
	        success: function(json) {
		        // populate the popup fields
		        var entity = json.entity;
		        var popup = $('#modal-add-roles');
		        $(popup).find('input[name="entity.id"]').val(entity.id);
		        $(popup).find('input[name="entity.password"]').val(entity.password);

		        $.each(entity.roles, function(idx, role) {
		        	$('#modal-add-roles .allRoles_id input').each(function() {
		        		var value = $(this).val();
		        		if(value == role.id) {
		        			$(this).attr('checked', 'checked');
		        		}
		        	});
		        });
		        if(entity.roles.length == $('#modal-add-roles .allRoles_id input').length) {
		        	$('#selectAllRoles').attr('checked', 'checked');
		        }
		        // show the modal
		        loadModal('#modal-add-roles');
	        },
	        error:function(){
	        	alert("There was a problem processing your request. Please contact the site administrator.");
	        }
        });
    });
    $('#modal-add-branches .seach-branches input:text').keypress(function(event) {
    	var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == '13') {
        	return false;
        }
    });
    $('.link-add-branches').live('click', function() {
    	closeModal();
    	$('#selectAllBranches').removeAttr('checked');
        $('#modal-add-branches .seach-branches input').val('');
        $('#modal-add-branches .branchesHidden').remove();
        $('#modal-add-branches .allBranches_id li.selected').removeClass('selected');
        $('#modal-add-branches .allBranches_id input').removeAttr('checked');
        $('#modal-add-branches .allBranches_id li').show();
        $('#modal-add-branches .allBranches_id').prev().find('.text').val('Search Branch').focus().blur();
        // the checkbox selection value
        var id = $(this).parents('tr').find('td:eq(0) input').first().attr("value");
        // load selected category then show in modal
        $.ajax({
	        type: "GET",
	        url: datactx + "/ajax/GetUser.action?retrieveId=" + id,
	        dataType: "json",
	        success: function(json) {
		        // populate the popup fields
		        var entity = json.entity;
		        var popup = $('#modal-add-branches');
		        $(popup).find('input[name="entity.id"]').val(entity.id);
		        $(popup).find('input[name="entity.password"]').val(entity.password);

		        $.each(entity.branches, function(idx, branch) {
		        	$('#modal-add-branches .allBranches_id input').each(function() {
		        		var value = $(this).val();
		        		if(value == branch.id) {
		        			$(this).attr('checked', 'checked');
		        			$(this).parent().addClass('selected');
		        		}
		        	});
		        });
		        if(entity.branches.length == $('#modal-add-branches .allBranches_id input').length) {
		        	$('#selectAllBranches').attr('checked', 'checked');
		        }
		        // show the modal
		        loadModal('#modal-add-branches');
	        },
	        error:function(){
	        	alert("There was a problem processing your request. Please contact the site administrator.");
	        }
        });
    });
    //Roles modal
    $('.add-roles').live('click',function(){
        closeModal();
        loadModal('#modal-add-roles');
    });

    $('.add-branches').live('click',function(){
        closeModal();
        loadModal('#modal-add-branches');
    });

    //Branch modal
    $('.btn-create-branch').live('click',function(){
        closeModal();
        $('#modal-create-branch input:text').val('');
        $('#modal-create-branch select option').first().prop("selected", "selected");
        loadModal('#modal-create-branch');
    });

    $('.link-edit-branch').live('click',function(){
        closeModal();

        // the checkbox selection value
        var branchId = $(this).parents('tr').find('td:eq(0) input').first().attr("value");
        // load selected market then show in modal
        $.ajax({
            type: "GET",
            url: datactx + "/ajax/GetBranch.action?retrieveId=" + branchId,
            dataType: "json",
            success: function(json) {
            	// populate the popup fields
            	var branch = json.entity;
            	var popup = $('#modal-update-branch');
            	$(popup).find('input[name="entity.id"]').val(branch.id);
            	$(popup).find('input[name="entity.branchNumber"]').val(branch.branchNumber);
            	$(popup).find('input[name="entity.triLogonName"]').val(branch.triLogonName);
            	$(popup).find('input[name="entity.machineName"]').val(branch.machineName);
            	$(popup).find('input[name="entity.address1"]').val(branch.address1);
            	$(popup).find('input[name="entity.address2"]').val(branch.address2);
            	$(popup).find('input[name="entity.city"]').val(branch.city);
            	$(popup).find('select[name="entity.state.id"]').val(branch.state.id);
            	$(popup).find('input[name="entity.zipCode"]').val(branch.zipCode);
            	$(popup).find('input[name="entity.profitCenter"]').val(branch.profitCenter);
            	$(popup).find('input[name="entity.phoneNumber"]').val(branch.phoneNumber);
            	$(popup).find('input[name="entity.faxNumber"]').val(branch.faxNumber);
            	$(popup).find('input[name="entity.contactName"]').val(branch.contactName);
            	$(popup).find('select[name="entity.region.id"]').val(branch.region.id);
            	$(popup).find('select[name="entity.district.id"]').val(branch.district.id);
            	$(popup).find('input[name="entity.generalManagerName"]').val(branch.generalManagerName);
            	$(popup).find('select[name="entity.market.id"]').val(branch.market.id);

            	// split the phone number parts
                $(popup).find('.phoneNumber').each(function(){
                	var parts = $(this).find('input:eq(3)').val().split("-");
                	if (parts.length > 0) $(this).find('input:eq(0)').val(parts[0]);
                	if (parts.length > 1) $(this).find('input:eq(1)').val(parts[1]);
                	if (parts.length > 2) $(this).find('input:eq(2)').val(parts[2]);
                });

            	// show the modal
                loadModal('#modal-update-branch');
            },
            error:function(){
            	alert("There was a problem processing your request. Please contact the site administrator.");
            }
        });
    });

    //Market modal
    $('.btn-create-market').live('click',function(){
        closeModal();
        $('#modal-create-market input:text').val('');
        $('#modal-create-market textarea').val('');
        loadModal('#modal-create-market');
    });

    $('.link-edit-market').live('click',function(){
        closeModal();

        // the checkbox selection value
        var marketId = $(this).parents('tr').find('td:eq(0) input').first().attr("value");
        // load selected market then show in modal
        $.ajax({
            type: "GET",
            url: datactx + "/ajax/GetMarket.action?retrieveId=" + marketId,
            dataType: "json",
            success: function(json) {
            	// populate the popup fields
            	var market = json.entity;
            	var popup = $('#modal-update-market');
            	$(popup).find('input[name="entity.id"]').val(market.id);
            	$(popup).find('input[name="entity.name"]').val(market.name);
            	$(popup).find('textarea[name="entity.description"]').val(market.description);

            	// show the modal
                loadModal('#modal-update-market');
            },
            error:function(){
            	alert("There was a problem processing your request. Please contact the site administrator.");
            }
        });
    });

    //Category modal
    $('.btn-create-category').live('click',function(){
        closeModal();
        $('#modal-create-category input:text').val('');
        $('#modal-create-category textarea').val('');
        $('#modal-create-category select option').first().prop("selected", "selected");
        loadModal('#modal-create-category');
    });

    $('.link-edit-category').live('click',function(){
        closeModal();

        // the checkbox selection value
        var categoryId = $(this).parents('tr').find('td:eq(0) input').first().attr("value");
        // load selected category then show in modal
        $.ajax({
            type: "GET",
            url: datactx + "/ajax/GetCategory.action?retrieveId=" + categoryId,
            dataType: "json",
            success: function(json) {
            	// populate the popup fields
            	var category = json.entity;
            	var popup = $('#modal-update-category');
            	$(popup).find('input[name="entity.id"]').val(category.id);
            	$(popup).find('input[name="entity.name"]').val(category.name);
            	$(popup).find('textarea[name="entity.description"]').val(category.description);
            	// $(popup).find('select[name="entity.branch.id"]').val(category.branch.id);

            	// show the modal
                loadModal('#modal-update-category');
            },
            error:function(){
            	alert("There was a problem processing your request. Please contact the site administrator.");
            }
        });
    });

    //Product modal
    $('.btn-create-product').live('click',function(){
        closeModal();
        $('#modal-create-product input:text').val('');
        $('#modal-create-product textarea').val('');
        $('#modal-create-product select option').first().prop("selected", "selected");
        loadModal('#modal-create-product');
    });

    $('.link-edit-product').live('click',function(){
        closeModal();

        // the checkbox selection value
        var productId = $(this).parents('tr').find('td:eq(0) input').first().attr("value");
        // load selected vendor then show in modal
        $.ajax({
            type: "GET",
            url: datactx + "/ajax/GetProduct.action?retrieveId=" + productId,
            dataType: "json",
            success: function(json) {
            	// populate the popup fields
            	var product = json.entity;
            	var popup = $('#modal-update-product');
            	$(popup).find('input[name="entity.id"]').val(product.id);
            	$(popup).find('input[name="entity.productCode"]').val(product.productCode);
            	$(popup).find('input[name="entity.alternateCode1"]').val(product.alternateCode1);
            	$(popup).find('textarea[name="entity.description"]').val(product.description);
            	$(popup).find('input[name="entity.hundredWeight"]').val(product.hundredWeight);
            	$(popup).find('input[name="entity.listPrice"]').val(product.listPrice);
            	$(popup).find('select[name="entity.type.id"]').val(product.type.id);
            	$(popup).find('select[name="entity.category.id"]').val(product.category.id);
            	$(popup).find('select[name="selectedBranchIds"] option').removeAttr('selected');
            	$.each(json.selectedBranchIds, function(idx,item) {
            		$(popup).find('select[name="selectedBranchIds"] option[value="'+ item + '"]').prop("selected", true);
            	});

            	// show the modal
                loadModal('#modal-update-product');
            },
            error:function(){
            	alert("There was a problem processing your request. Please contact the site administrator.");
            }
        });

    });

    //Vendor modal
    $('.btn-create-vendor').live('click',function(){
        closeModal();
        $('#modal-create-vendor input:text').val('');
        $('#modal-create-vendor select option').first().prop("selected", "selected");
        loadModal('#modal-create-vendor');
    });

    $('.link-edit-vendor').live('click',function(){
        closeModal();

        // the checkbox selection value
        var vendorId = $(this).parents('tr').find('td:eq(0) input').first().attr("value");
        // load selected vendor then show in modal
        $.ajax({
            type: "GET",
            url: datactx + "/ajax/GetVendor.action?retrieveId=" + vendorId,
            dataType: "json",
            success: function(json) {
            	// populate the popup fields
            	var vendor = json.entity;
            	var popup = $('#modal-update-vendor');
            	$(popup).find('.form-title h3').text(vendor.name);
            	$(popup).find('.form-title a').attr("href", datactx + '/ShipPointSearch.action?criteria.vendorId=' + vendor.id);
            	$(popup).find('input[name="entity.id"]').val(vendor.id);
            	$(popup).find('input[name="entity.vendorNumber"]').val(vendor.vendorNumber);
            	$(popup).find('input[name="entity.name"]').val(vendor.name);
            	$(popup).find('input[name="entity.address1"]').val(vendor.address1);
            	$(popup).find('input[name="entity.address2"]').val(vendor.address2);
            	$(popup).find('input[name="entity.city"]').val(vendor.city);
            	$(popup).find('select[name="entity.state.id"]').val(vendor.state.id);
            	$(popup).find('input[name="entity.zipCode"]').val(vendor.zipCode);
            	$(popup).find('input[name="entity.phoneNumber1"]').val(vendor.phoneNumber1);
            	$(popup).find('input[name="entity.phoneNumber2"]').val(vendor.phoneNumber2);
            	$(popup).find('input[name="entity.faxNumber"]').val(vendor.faxNumber);
            	$(popup).find('input[name="entity.contact1Name"]').val(vendor.contact1Name);
            	$(popup).find('input[name="entity.contact1EmailAddress"]').val(vendor.contact1EmailAddress);
            	$(popup).find('input[name="entity.contact2Name"]').val(vendor.contact2Name);
            	$(popup).find('input[name="entity.contact2EmailAddress"]').val(vendor.contact2EmailAddress);

            	// split the phone number parts
                $(popup).find('.phoneNumber').each(function(){
                	var parts = $(this).find('input:eq(3)').val().split("-");
                	if (parts.length > 0) $(this).find('input:eq(0)').val(parts[0]);
                	if (parts.length > 1) $(this).find('input:eq(1)').val(parts[1]);
                	if (parts.length > 2) $(this).find('input:eq(2)').val(parts[2]);
                });

            	// show the modal
            	loadModal('#modal-update-vendor');
            },
            error:function(){
            	alert("There was a problem processing your request. Please contact the site administrator.");
            }
        });
    });

    //Shipping point modal
    $('.btn-create-shipping-point').live('click',function(){
        closeModal();
        $('#modal-create-shipping-point input:text').val('');
        $('#modal-create-shipping-point input:checked').removeAttr('checked');
        $('#modal-create-shipping-point select option').first().prop("selected", "selected");
        loadModal('#modal-create-shipping-point');
    });

    $('.link-edit-shipping-point').live('click',function(){
        closeModal();

        closeModal();

        // the checkbox selection value
        var shipPointId = $(this).parents('tr').find('td:eq(0) input').first().attr("value");
        // load selected ship point then show in modal
        $.ajax({
            type: "GET",
            url: datactx + "/ajax/GetShipPoint.action?retrieveId=" + shipPointId,
            dataType: "json",
            success: function(json) {
            	// populate the popup fields
            	var shippoint = json.entity;
            	var popup = $('#modal-update-shipping-point');
            	$(popup).find('input[name="entity.id"]').val(shippoint.id);
            	$(popup).find('input[name="entity.name"]').val(shippoint.name);
            	$(popup).find('input[name="entity.description"]').val(shippoint.description);
            	$(popup).find('input[name="entity.zipCode"]').val(shippoint.zipCode);
            	$(popup).find('input[name="entity.phoneNumber1"]').val(shippoint.phoneNumber1);
            	$(popup).find('input[name="entity.phoneNumber2"]').val(shippoint.phoneNumber2);
            	if (shippoint.isADC) {
            		$(popup).find('input[name="entity.isADC"]').attr("checked", "checked");
            	} else {
            		$(popup).find('input[name="entity.isADC"]').removeAttr("checked");
            	}
            	$(popup).find('input[name="entity.faxNumber"]').val(shippoint.faxNumber);
            	$(popup).find('input[name="entity.contact1Name"]').val(shippoint.contact1Name);
            	$(popup).find('input[name="entity.contact1EmailAddress"]').val(shippoint.contact1EmailAddress);
            	$(popup).find('input[name="entity.contact2Name"]').val(shippoint.contact2Name);
            	$(popup).find('input[name="entity.contact2EmailAddress"]').val(shippoint.contact2EmailAddress);
            	$(popup).find('select[name="entity.typeId"]').val(shippoint.typeId);

            	// split the phone number parts
                $(popup).find('.phoneNumber').each(function(){
                	var parts = $(this).find('input:eq(3)').val().split("-");
                	if (parts.length > 0) $(this).find('input:eq(0)').val(parts[0]);
                	if (parts.length > 1) $(this).find('input:eq(1)').val(parts[1]);
                	if (parts.length > 2) $(this).find('input:eq(2)').val(parts[2]);
                });

            	// show the modal
                loadModal('#modal-update-shipping-point');
            },
            error:function(){
            	alert("There was a problem processing your request. Please contact the site administrator.");
            }
        });

    });

    /**
    // Verification
    $('#modal-create-user .btn-save').live('click',function(){
    	var popupId = '#modal-create-user'
        var flag = true;
        var flagRole = false;
        var flagBranches =false;
        $('#modal-create-user .text').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        $('#modal-create-user .row-role input').each(function(){
            if($(this).attr('checked')){
                flagRole = true;
            }
        });
        if(!flagRole){
            $('#modal-create-user .row-role label,#modal-create-user .row-role span').addClass('error');
        }else{
            $('#modal-create-user .row-role label,#modal-create-user .row-role span').removeClass('error');
        }
        $('#modal-create-user .branches input').each(function(){
            if($(this).attr('checked')){
                flagBranches = true;
            }
        });
        if(!flagBranches){
            $('#modal-create-user .seach-branches,#modal-create-user .branches-check').addClass('error');
        }else{
            $('#modal-create-user .seach-branches,#modal-create-user .branches-check').removeClass('error');
        }
        if(flag&&flagRole&&flagBranches){
            closeModal();
            $('#modal-create-user .error-field').hide();
        }else{
            $('#modal-create-user .error-field').show();
        }
    });

    $('#modal-update-user .btn-save').live('click',function(){
        var flag = true;
        var flagRole = false;
        var flagBranches =false;
        $('#modal-update-user .text').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        $('#modal-update-user .row-role input').each(function(){
            if($(this).attr('checked')){
                flagRole = true;
            }
        });
        if(!flagRole){
            $('#modal-update-user .row-role label,#modal-update-user .row-role span').addClass('error');
        }else{
            $('#modal-update-user .row-role label,#modal-update-user .row-role span').removeClass('error');
        }
        $('#modal-update-user .branches input').each(function(){
            if($(this).attr('checked')){
                flagBranches = true;
            }
        });
        if(!flagBranches){
            $('#modal-update-user .seach-branches,#modal-update-user .branches-check').addClass('error');
        }else{
            $('#modal-update-user .seach-branches,#modal-update-user .branches-check').removeClass('error');
        }
        if(flag&&flagRole&&flagBranches){
            closeModal();
            $('#modal-update-user .error-field').hide();
        }else{
            $('#modal-update-user .error-field').show();
        }
    });

    $('#modal-create-branch .btn-save').live('click',function(){
        var flag = true;
        $('#modal-create-branch .text').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        $('#modal-create-branch select').each(function(){
            if($(this).get(0).selectedIndex == 0){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        if(flag){
            closeModal();
            $('#modal-create-branch .error-field').hide();
        }else{
            $('#modal-create-branch .error-field').show();
        }
    });

    $('#modal-update-branch .btn-save').live('click',function(){
        var flag = true;
        $('#modal-update-branch .text').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        $('#modal-update-branch select').each(function(){
            if($(this).get(0).selectedIndex == 0){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        if(flag){
            closeModal();
            $('#modal-update-branch .error-field').hide();
        }else{
            $('#modal-update-branch .error-field').show();
        }
    });
    */
    /*
    $('#modal-create-market .btn-save').live('click',function(){
        var flag = true;
        $('#modal-create-market .text').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        $('#modal-create-market .textarea').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row-description').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row-description').find('label').removeClass('error');
            }
        });
        if(flag){
            closeModal();
            $('#modal-create-market .error-field').hide();
        }else{
            $('#modal-create-market .error-field').show();
        }
    });

    $('#modal-update-market .btn-save').live('click',function(){
        var flag = true;
        $('#modal-update-market .text').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        $('#modal-update-market .textarea').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row-description').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row-description').find('label').removeClass('error');
            }
        });
        if(flag){
            closeModal();
            $('#modal-update-market .error-field').hide();
        }else{
            $('#modal-update-market .error-field').show();
        }
    });
    */

    /*
    $('#modal-create-category .btn-save').live('click',function(){
        var flag = true;
        $('#modal-create-category .text').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        $('#modal-create-category .textarea').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row-description').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row-description').find('label').removeClass('error');
            }
        });
        if(flag){
            closeModal();
            $('#modal-create-category .error-field').hide();
        }else{
            $('#modal-create-category .error-field').show();
        }
    });

    $('#modal-update-category .btn-save').live('click',function(){
        var flag = true;
        $('#modal-update-category .text').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        $('#modal-update-category .textarea').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row-description').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row-description').find('label').removeClass('error');
            }
        });
        if(flag){
            closeModal();
            $('#modal-update-category .error-field').hide();
        }else{
            $('#modal-update-category .error-field').show();
        }
    });

    $('#modal-create-product .btn-save').live('click',function(){
        var flag = true;
        $('#modal-create-product .text').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        $('#modal-create-product .textarea').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row-description').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row-description').find('label').removeClass('error');
            }
        });
        if(flag){
            closeModal();
            $('#modal-create-product .error-field').hide();
        }else{
            $('#modal-create-product .error-field').show();
        }
    });

    $('#modal-update-product .btn-save').live('click',function(){
        var flag = true;
        $('#modal-update-product .text').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        $('#modal-update-product .textarea').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row-description').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row-description').find('label').removeClass('error');
            }
        });
        if(flag){
            closeModal();
            $('#modal-update-product .error-field').hide();
        }else{
            $('#modal-update-product .error-field').show();
        }
    });
    */
    $('.zip-rates .tip-for-tbody .btn-submit').live('click', function() {
    	var popup = $(this).parents('.tip-for-tbody');
    	var rate = Number(popup.find('input[name="entity.rate"]').val());
        var fsc = Number(popup.find('input[name="entity.fsc"]').val());
        var mileage = Number(popup.find('input[name="entity.mileage"]').val());
        $('.zip-rates .tip-for-tbody input[name="entity.freightRate"]').val(((rate + fsc) * mileage) /20);
    });
    /**
     * This is called when the create/update popup 'save' button is clicked.
     */
    $('#modal-create-shipping-point .btn-save, #modal-update-shipping-point .btn-save, #modal-create-vendor .btn-save,'
    	+ ' #modal-update-vendor .btn-save, #modal-create-category .btn-save, #modal-update-category .btn-save,'
    	+ ' #modal-update-product .btn-save, #modal-create-product .btn-save,'
    	+ ' #modal-update-branch .btn-save, #modal-create-branch .btn-save,'
    	+ ' #modal-create-market .btn-save, #modal-update-market .btn-save,'
    	+ ' #modal-create-user .btn-save, #modal-update-user .btn-save, #modal-add-branches .btn-submit, #modal-add-roles .btn-submit,'
    	+ ' #modal-add-destination .btn-add, #modal-update-destination .btn-update,'
    	+ ' #modal-add-origin .btn-add, #modal-update-origin .btn-update,'
    	+ ' .state-rates .tip-for-tbody .btn-update, .zip-rates .tip-for-tbody .btn-submit').live('click',function(){
    	var popup = $(this).closest('.modal');

    	// clear all markers
    	$(popup).find('.error').removeClass('error');
    	$(popup).find('.error-text').removeClass('error-text');
    	$(popup).find('.error-field p').remove();

        var flag = true;
        var hasRequiredFieldError = false;
        var hasInvalidEmailError = false;
        var hasInvalidNumberFieldError = false;
        var invalidUsername = false;

        // validate text fields
        $(popup).find('.text').each(function(){
        	if($(this).hasClass('search-box')) {
        		return;
        	}
            if($.trim($(this).val()) == ''){
                flag = false;
                hasRequiredFieldError = true;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            } else if ($(this).hasClass('email')){
            	if(!(/(.+)@(.+){1,}\.(.+){2,}/.test($(this).val()))){
                    flag = false;
                    hasInvalidEmailError = true;
                    $(this).addClass('error-text');
                    $(this).parents('.row').find('label').addClass('error');
                }
            } else if ($(this).hasClass('bigdecimal')){
            	if(!(/^\d*\.?\d*$/.test($(this).val()))){
            		flag = false;
            		$(this).addClass('error-text');
            		$(this).parents('.row').find('label').addClass('error');
            		$(popup).find('.error-field').append('<p>' + $(this).attr('title') + ' must be a decimal value.</p>');
            	}
            } else if ($(this).hasClass('biginteger')){
            	if(!(/^\d*$/.test($(this).val()))){
            		flag = false;
            		$(this).addClass('error-text');
            		$(this).parents('.row').find('label').addClass('error');
            		$(popup).find('.error-field').append('<p>' + $(this).attr('title') + ' must be a numeric value.</p>');
            	}
            }
        });

        // validate text areas
        $(popup).find('.textarea').each(function(){
            if($.trim($(this).val()) == ''){
                flag = false;
                hasRequiredFieldError = true;
                $(this).addClass('error-text');
                $(this).parents('.row-description').find('label').addClass('error');
            } else if($(this).hasClass('max200') && $(this).val().length > 200) {
            	flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row-description').find('label').addClass('error');
            	$(popup).find('.error-field').append('<p>Only 200 characters are allowed for ' + $(this).attr('title') + '.</p>');
            }
        });
        if($(popup).find('.allRoles_id').length && $(popup).find('.allRoles_id input:checked').length == 0) {
            flag = false;
            hasRequiredFieldError = true;
            $(popup).find('.row-role label, .row-role span').addClass('error');
        }
        if($(popup).find('.allBranches_id').length && $(popup).find('.allBranches_id input:checked').length == 0) {
            flag = false;
            hasRequiredFieldError = true;
            $(popup).find('.seach-branches, .branches-check').addClass('error');
        }
        if($('#resultEntityType').val() == 'User') {
        	var username = $(popup).find('input[name="entity.firstName"]').val() + ' ' + $(popup).find('input[name="entity.lastName"]').val();
        	if(username.length > 50) {
        		flag = false;
        		invalidUsername = true;
        	}
        }
        if($('#resultEntityType').val() == 'OriginationToStateFreightRate') {
    		var val = $(popup).find('input[name="entity.freightRate"]').val();
    		if(isNaN(Number(val))) {
    			flag = false;
    			hasInvalidNumberFieldError = true;
    			$(popup).find('input[name="entity.freightRate"]').addClass('error-text');
    		}
    		val = $(popup).find('input[name="entity.fsc"]').val();
    		if(isNaN(Number(val))) {
    			flag = false;
    			hasInvalidNumberFieldError = true;
    			$(popup).find('input[name="entity.fsc"]').addClass('error-text');
    		}
    	}
    	if($('#resultEntityType').val() == 'OriginationToDestinationFreightRate') {
    		var val = $(popup).find('input[name="entity.mileage"]').val();
    		if(isNaN(Number(val))) {
    			flag = false;
                hasInvalidNumberFieldError = true;
    			$(popup).find('input[name="entity.mileage"]').addClass('error-text');
    		}
    		val = $(popup).find('input[name="entity.fsc"]').val();
    		if(isNaN(Number(val))) {
    			flag = false;
    			hasInvalidNumberFieldError = true;
    			$(popup).find('input[name="entity.fsc"]').addClass('error-text');
    		}
    		val = $(popup).find('input[name="entity.rate"]').val();
    		if(isNaN(Number(val))) {
    			flag = false;
    			hasInvalidNumberFieldError = true;
    			$(popup).find('input[name="entity.rate"]').addClass('error-text');
    		}
    	}
        if (flag) { // passed validations
            if($('#resultEntityType').val() == 'User') {
            	$(popup).find('form .rolesHidden').remove();
            	$(popup).find('form .branchesHidden').remove();
            	var i = 0;
            	$(popup).find('.allRoles_id input').each(function(ind, ele){
                    var selected = $(this).attr('checked');
                    if (selected == 'checked') {
                        var value = $(this).val();
                        var param = '<input type="hidden" class="rolesHidden" name="entity.roles[' + (i++) + '].id" value="' + value + '"/>';
                        $(popup).find('form').append(param);
                    };
                });
            	i = 0;
            	$(popup).find('.allBranches_id input').each(function(ind, ele){
                    var selected = $(this).attr('checked');
                    if (selected == 'checked') {
                        var value = $(this).val();
                        var param = '<input type="hidden" class="branchesHidden" name="entity.branches[' + (i++) + '].id" value="' + value + '"/>';
                        $(popup).find('form').append(param);
                    };
                });

            	$(popup).find('input[name="entity.username"]').val($(popup).find('input[name="entity.firstName"]').val() + ' ' + $(popup).find('input[name="entity.lastName"]').val());
            }

        	var isUpdate = $(popup).find('input[name="entity.id"]').size() > 0 && Number($(popup).find('input[name="entity.id"]').val()) > 0;

        	// merge phone number field rows
            $(popup).find('.phoneNumber').each(function(){
            	var p1 = $(this).find('input:eq(0)').val();
            	var p2 = $(this).find('input:eq(1)').val();
            	var p3 = $(this).find('input:eq(2)').val();
            	$(this).find('input:eq(3)').val(p1 + '-' + p2 + '-' + p3);
            });

        	$(popup).find('.error-field').hide();
        	var form = $(popup).find('form');
        	if(!isUpdate && $('#resultEntityType').val() == 'OriginationToStateFreightRate') {
        		form = $('#CreateOriginationToStateFreightRateForm');
        		form.find('input[name="entity.freightRate"]').val($(popup).find('input[name="entity.freightRate"]').val());
        		form.find('input[name="entity.fsc"]').val($(popup).find('input[name="entity.fsc"]').val());
        		form.find('input[name="entity.origination.id"]').val($(popup).find('input[name="origination"]').val());
        		form.find('input[name="entity.state.id"]').val($(popup).find('input[name="state"]').val());
        	}
        	if(!isUpdate && $('#resultEntityType').val() == 'OriginationToDestinationFreightRate') {
        		form = $('#CreateOriginationToDestinationFreightRateForm');
        		form.find('input[name="entity.freightRate"]').val($(popup).find('input[name="entity.freightRate"]').val());
        		form.find('input[name="entity.rate"]').val($(popup).find('input[name="entity.rate"]').val());
        		form.find('input[name="entity.fsc"]').val($(popup).find('input[name="entity.fsc"]').val());
        		form.find('input[name="entity.mileage"]').val($(popup).find('input[name="entity.mileage"]').val());
        		form.find('input[name="entity.origination.id"]').val($(popup).find('input[name="origination"]').val());
        		form.find('input[name="entity.destination.id"]').val($(popup).find('input[name="destination"]').val());
        	}
        	form.ajaxSubmit({
        		dataType: 'json',
        		success : function() {
        			closeModal();

        			if (!isUpdate) {
        				// remove any filter if this is a new record
            			resetAdminSearchPageNumber();
            			$('#searchForm input[name="criteria.username"]').val('');
            			$('#searchForm input[name="criteria.searchTerm"]').val('');
        			}

        			// refresh screen
        			doAdminSearch();
        		},
        		error : function(req) {
    				alert("There was a problem processing your request. Please contact the site administrator.");
    			}
        	});
        } else { // failed validations
        	if (hasRequiredFieldError) {
        		$(popup).find('.error-field').append('<p>The fields in red are required.</p>');
        	}
        	if (hasInvalidEmailError) {
        		$(popup).find('.error-field').append('<p>The expected email format is "email@domain.com"</p>');
        	}
        	if(hasInvalidNumberFieldError) {
        		$(popup).find('.error-field').append('<p>The fields in red should be number.</p>');
        	}
        	if(invalidUsername) {
        		$(popup).find('.error-field').append('<p>"firstName lastName" is too long for username (at most 50 chars).</p>');
        	}
            $(popup).find('.error-field').show();
        }
    });

    /*
    $('#modal-update-vendor .btn-save').live('click',function(){
        var flag = true;
        $('#modal-update-vendor .text').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        if(flag){
            closeModal();
            $('#modal-update-vendor .error-field').hide();
        }else{
            $('#modal-update-vendor .error-field').show();
        }
    });
    */

    /*
    $('#modal-create-shipping-point .btn-save').live('click',function(){
        var flag = true;
        $('#modal-create-shipping-point .text').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        if(flag){
            closeModal();
            $('#modal-create-shipping-point .error-field').hide();
        }else{
            $('#modal-create-shipping-point .error-field').show();
        }
    });

    $('#modal-update-shipping-point .btn-save').live('click',function(){
        var flag = true;
        $('#modal-update-shipping-point .text').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        if(flag){
            closeModal();
            $('#modal-update-shipping-point .error-field').hide();
        }else{
            $('#modal-update-shipping-point .error-field').show();
        }
    });

    $('#modal-add-origin .btn-add').live('click',function(){
        var flag = true;
        $('#modal-add-origin .text').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        $('#modal-add-origin select').each(function(){
            if($(this).get(0).selectedIndex == 0){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        if(flag){
            closeModal();
            $('#modal-add-origin .error-field').hide();
        }else{
            $('#modal-add-origin .error-field').show();
        }
    });

    $('#modal-update-origin .btn-update').live('click',function(){
        var flag = true;
        $('#modal-update-origin .text').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        $('#modal-update-origin select').each(function(){
            if($(this).get(0).selectedIndex == 0){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        if(flag){
            closeModal();
            $('#modal-update-origin .error-field').hide();
        }else{
            $('#modal-update-origin .error-field').show();
        }
    });

    $('#modal-add-destination .btn-add').live('click',function(){
        var flag = true;
        $('#modal-add-destination .text').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        $('#modal-add-destination select').each(function(){
            if($(this).get(0).selectedIndex == 0){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        if(flag){
            closeModal();
            $('#modal-add-destination .error-field').hide();
        }else{
            $('#modal-add-destination .error-field').show();
        }
    });

    $('#modal-update-destination .btn-update').live('click',function(){
        var flag = true;
        $('#modal-update-destination .text').each(function(){
            if($(this).val() == ''){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        $('#modal-update-destination select').each(function(){
            if($(this).get(0).selectedIndex == 0){
                flag = false;
                $(this).addClass('error-text');
                $(this).parents('.row').find('label').addClass('error');
            }else{
                $(this).removeClass('error-text');
                $(this).parents('.row').find('label').removeClass('error');
            }
        });
        if(flag){
            closeModal();
            $('#modal-update-destination .error-field').hide();
        }else{
            $('#modal-update-destination .error-field').show();
        }
    });

    */
    //Hover on table
    $('.state-rates .scroll-data td').live('click',function(){
        var id = $(this).data('id');
        var rate = $(this).data('rate');
        var fsc = $(this).data('fsc');
        var origination = $(this).data('origination');
        var state = $(this).data('state');
        $('.state-rates .tip-for-tbody input[name="entity.id"]').val(id);
        $('.state-rates .tip-for-tbody input[name="entity.freightRate"]').val(rate);
        $('.state-rates .tip-for-tbody input[name="entity.fsc"]').val(fsc);
        $('.state-rates .tip-for-tbody input[name="origination"]').val(origination);
        $('.state-rates .tip-for-tbody input[name="state"]').val(state);
        $('.state-rates .tip-for-tbody .error-field').hide();
        $('.state-rates .tip-for-tbody .text').removeClass('error-text');
        $('.state-rates .tip-for-tbody').show();
        $('.state-rates .scroll-data td').removeClass('selected');
        $('.state-rates .column tbody td').removeClass('selected');
        $('.state-rates .scroll-data thead th').removeClass('selected');
        var i = $('.state-rates .scroll-data tbody tr').index($(this).parents('tr').eq(0));
        var j = $('.state-rates .scroll-data tbody td').index(this)%20;
        $(this).addClass('selected');
        $('.state-rates .column tbody td').eq(i).addClass('selected');
        $('.state-rates .scroll-data thead th').eq(j).addClass('selected');
        if($(this).position().left+442 > 960){
            $('.state-rates .tip-for-tbody').addClass('tip-for-tbody-other');
            $('.state-rates .tip-for-tbody').css({top:$(this).position().top-60,left:$(this).position().left-355});
        }else{
            $('.state-rates .tip-for-tbody').removeClass('tip-for-tbody-other');
            $('.state-rates .tip-for-tbody').css({top:$(this).position().top-60,left:$(this).position().left+88});
        }
    });

    $('.details-link').live('mouseover',function(){
    	$('.tip-for-filter-hover').hide();
    	$(this).parent().find('.tip-for-filter-hover').show();
        var w = 60;
        if($.browser.mozilla){
        	$(this).parent().find('.tip-for-filter-hover').css({top:$(this).position().top,left:$(this).position().left+w});
        }else{
        	$(this).parent().find('.tip-for-filter-hover').css({top:$(this).position().top+1,left:$(this).position().left+w});
        }
    });

    $('.details-link').live('mouseout',function(){
    	$('.tip-for-filter-hover').hide();
    });

    $('.state-rates .scroll-data td').live('mouseover',function(){
        $('.state-rates .tip-for-hover').show();
        var i = $('.state-rates .scroll-data tbody tr').index($(this).parents('tr').eq(0));
        var j = $('.state-rates .scroll-data tbody td').index(this)%20;
        $(this).addClass('highline');
        $('.state-rates .column tbody td').eq(i).addClass('highline');
        $('.state-rates .scroll-data thead th').eq(j).addClass('highline');
        var w = $(this).width();
        $('.state-rates .tip-for-hover').width(w).find('p').html('FSC: $ ' + $(this).data('fsc'));
        if($(this).position().left+w*2 > 960){
            if($.browser.mozilla){
                $('.state-rates .tip-for-hover').css({top:$(this).position().top,left:$(this).position().left-w});
            }else{
                $('.state-rates .tip-for-hover').css({top:$(this).position().top+1,left:$(this).position().left-w});
            }
        }else{
            if($.browser.mozilla){
                $('.state-rates .tip-for-hover').css({top:$(this).position().top,left:$(this).position().left+w});
            }else{
                $('.state-rates .tip-for-hover').css({top:$(this).position().top+1,left:$(this).position().left+w});
            }
        }
    });
    $('.state-rates .scroll-data td').live('mouseout',function(){
        $('.state-rates .tip-for-hover').hide();
        $(this).removeClass('highline');
        $('.state-rates .column tbody td').removeClass('highline');
        $('.state-rates .scroll-data thead th').removeClass('highline');
    });

    $('.state-rates .tip-for-tbody .tip-buttons a').live('click',function(){
    	if($(this).hasClass('btn-update')) {
    		return;
    	}
        $('.state-rates .tip-for-tbody').hide();
    });

    $('.state-rates .table-data th').live('mouseover',function(){
    	$('.state-rates .tip-for-thead dd:eq(0)').html($(this).data('name'));
    	$('.state-rates .tip-for-thead dd:eq(1)').html($(this).data('state'));
    	$('.state-rates .tip-for-thead dd:eq(2)').html($(this).data('type'));
    	$('.state-rates .tip-for-thead dd:eq(3)').html($(this).data('zipcode'));
        $('.state-rates .tip-for-thead').show();
        if($(this).position().left+122 > 960){
            $('.state-rates .tip-for-thead').css({top:$(this).position().top-110,left:761});
        }else{
            $('.state-rates .tip-for-thead').css({top:$(this).position().top-110,left:$(this).position().left-60});
        }
        $('.state-rates .tip-for-thead dd:first').text($(this).text());
    });
    $('.state-rates .table-data th').live('mouseout',function(){
        $('.state-rates .tip-for-thead').hide();
    });

    $('.zip-rates .scroll-data td').live('mouseover',function(){
        $('.zip-rates .tip-for-hover').show();
        var i = $('.zip-rates .scroll-data tbody tr').index($(this).parents('tr').eq(0));
        var j = $('.zip-rates .scroll-data tbody td').index(this)%20;
        $(this).addClass('highline');
        $('.zip-rates .column tbody td').eq(i).addClass('highline');
        $('.zip-rates .scroll-data thead th').eq(j).addClass('highline');
        var w = $(this).width();
        $('.zip-rates .tip-for-hover:eq(0)').width(w).find('p').html('Mileage: ' + $(this).data('fsc'));
        $('.zip-rates .tip-for-hover:eq(1)').width(w).find('p').html('Dollar: $ ' + $(this).data('freightrate'));
        if($(this).position().left+w*2 > 960){
            if($.browser.mozilla){
                $('.zip-rates .tip-for-hover').css({top:$(this).position().top,left:$(this).position().left-w});
            }else{
                $('.zip-rates .tip-for-hover').css({top:$(this).position().top+1,left:$(this).position().left-w});
            }
        }else{
            if($.browser.mozilla){
                $('.zip-rates .tip-for-hover').css({top:$(this).position().top,left:$(this).position().left+w});
            }else{
                $('.zip-rates .tip-for-hover').css({top:$(this).position().top+1,left:$(this).position().left+w});
            }
        }
    });
    $('.zip-rates .scroll-data td').live('mouseout',function(){
        $('.zip-rates .tip-for-hover').hide();
        $(this).removeClass('highline');
        $('.zip-rates .column tbody td').removeClass('highline');
        $('.zip-rates .scroll-data thead th').removeClass('highline');
    });

    $('.zip-rates #dollar .scroll-data td').live('click',function(){
        var id = $(this).data('id');
        var rate = Number($(this).data('rate'));
        var fsc = Number($(this).data('fsc'));
        var mileage = Number($(this).data('mileage'));
        var freightRate = Number($(this).data('freightrate'));
        var destination = $(this).data('destination');
        var origination = $(this).data('origination');
        $('.zip-rates #dollar .tip-for-tbody input[name="entity.id"]').val(id);
        $('.zip-rates #dollar .tip-for-tbody input[name="entity.rate"]').val(rate);
        $('.zip-rates #dollar .tip-for-tbody input[name="entity.fsc"]').val(fsc);
        $('.zip-rates #dollar .tip-for-tbody input[name="destination"]').val(destination);
        $('.zip-rates #dollar .tip-for-tbody input[name="origination"]').val(origination);
        $('.zip-rates #dollar .tip-for-tbody input[name="entity.mileage"]').val(mileage);
        $('.zip-rates #dollar .tip-for-tbody span.freightRate').html(freightRate);

        $('.zip-rates #dollar .tip-for-tbody .error-field').hide();
        $('.zip-rates #dollar .tip-for-tbody .text').removeClass('error-text');
        $('.zip-rates #dollar .tip-for-tbody').show();
        $('.zip-rates #dollar .scroll-data td').removeClass('selected');
        $('.zip-rates #dollar .column tbody td').removeClass('selected');
        $('.zip-rates #dollar .scroll-data thead th').removeClass('selected');
        var i = $('.zip-rates #dollar .scroll-data tbody tr').index($(this).parents('tr').eq(0));
        var j = $('.zip-rates #dollar .scroll-data tbody td').index(this)%20;
        $(this).addClass('selected');
        $('.zip-rates #dollar .column tbody td').eq(i).addClass('selected');
        $('.zip-rates #dollar .scroll-data thead th').eq(j).addClass('selected');
        if($(this).position().left+504 > 960){
            $('.zip-rates #dollar .tip-for-tbody').addClass('tip-for-tbody-other');
            $('.zip-rates #dollar .tip-for-tbody').css({top:$(this).position().top-60,left:$(this).position().left-488});
        }else{
            $('.zip-rates #dollar .tip-for-tbody').removeClass('tip-for-tbody-other');
            $('.zip-rates #dollar .tip-for-tbody').css({top:$(this).position().top-60,left:$(this).position().left+88});
        }
    });
    $('.zip-rates #mileage .scroll-data td').live('click',function(){
        var id = $(this).data('id');
        var rate = Number($(this).data('rate'));
        var fsc = Number($(this).data('fsc'));
        var mileage = Number($(this).data('mileage'));
        var freightRate = Number($(this).data('freightrate'));
        var destination = $(this).data('destination');
        var origination = $(this).data('origination');
        $('.zip-rates #mileage .tip-for-tbody input[name="entity.id"]').val(id);
        $('.zip-rates #mileage .tip-for-tbody input[name="entity.rate"]').val(rate);
        $('.zip-rates #mileage .tip-for-tbody input[name="entity.fsc"]').val(fsc);
        $('.zip-rates #mileage .tip-for-tbody input[name="destination"]').val(destination);
        $('.zip-rates #mileage .tip-for-tbody input[name="origination"]').val(origination);
        $('.zip-rates #mileage .tip-for-tbody input[name="entity.mileage"]').val(mileage);
        $('.zip-rates #mileage .tip-for-tbody span.freightRate').html(freightRate);
        $('.zip-rates #mileage .tip-for-tbody .error-field').hide();
        $('.zip-rates #mileage .tip-for-tbody .text').removeClass('error-text');
        $('.zip-rates #mileage .tip-for-tbody').show();
        $('.zip-rates #mileage .scroll-data td').removeClass('selected');
        $('.zip-rates #mileage .column tbody td').removeClass('selected');
        $('.zip-rates #mileage .scroll-data thead th').removeClass('selected');
        var i = $('.zip-rates #mileage .scroll-data tbody tr').index($(this).parents('tr').eq(0));
        var j = $('.zip-rates #mileage .scroll-data tbody td').index(this)%20;
        $(this).addClass('selected');
        $('.zip-rates #mileage .column tbody td').eq(i).addClass('selected');
        $('.zip-rates #mileage .scroll-data thead th').eq(j).addClass('selected');
        if($(this).position().left+504 > 960){
            $('.zip-rates #mileage .tip-for-tbody').addClass('tip-for-tbody-other');
            $('.zip-rates #mileage .tip-for-tbody').css({top:$(this).position().top-60,left:$(this).position().left-488});
        }else{
            $('.zip-rates #mileage .tip-for-tbody').removeClass('tip-for-tbody-other');
            $('.zip-rates #mileage .tip-for-tbody').css({top:$(this).position().top-60,left:$(this).position().left+88});
        }
    });

    $('.zip-rates .tip-for-tbody .tip-buttons a').live('click',function(){
    	if($(this).hasClass('btn-submit')) {
    		return;
    	}
        $('.zip-rates .tip-for-tbody').hide();
    });
    $('.updateFreightRate').live('click', function() {
    	var rate = Number($(this).parents('.tip-for-tbody').find('input[name="entity.rate"]').val());
        var fsc = Number($(this).parents('.tip-for-tbody').find('input[name="entity.fsc"]').val());
        var mileage = Number($(this).parents('.tip-for-tbody').find('input[name="entity.mileage"]').val());
        $(this).parents('.tip-for-tbody').find('span.freightRate').html(((rate + fsc) * mileage) / 20);
    });
    $('.zip-rates .table-data th').live('mouseover',function(){
    	$('.zip-rates .tip-for-thead dd:eq(0)').html($(this).data('name'));
    	$('.zip-rates .tip-for-thead dd:eq(1)').html($(this).data('state'));
    	$('.zip-rates .tip-for-thead dd:eq(2)').html($(this).data('type'));
    	$('.zip-rates .tip-for-thead dd:eq(3)').html($(this).data('zipcode'));
        $('.zip-rates .tip-for-thead').show();
        if($(this).position().left+122 > 960){
            $('.zip-rates .tip-for-thead').css({top:$(this).position().top-110,left:761});
        }else{
            $('.zip-rates .tip-for-thead').css({top:$(this).position().top-110,left:$(this).position().left-60});
        }
        $('.zip-rates .tip-for-thead dd:first').text($(this).text());
    });
    $('.zip-rates .table-data th').live('mouseout',function(){
        $('.zip-rates .tip-for-thead').hide();
    });
    $('#fscUpdate').live('click',function(){
    	var val = $('input[name="bulkChangeValue"]').val();
    	if($.trim(val) == '' || !(/^\d*\.?\d*$/.test(val))){
    		alert("Please enter a valid decimal value.");
    		return;
    	}

    	$(this).closest('form').ajaxSubmit({
    		success: function(data){
    			doAdminSearch();
    		},
    		error:function(){
    			alert("There was a problem processing your request. Please contact the site administrator.");
    		}
    	});
    });

    $('#mfcUpdate').live('click',function(){
    	var val = $('input[name="bulkChangeValue"]').val();
    	if($.trim(val) == '' || !(/^\d*\.?\d*$/.test(val))){
    		alert("Please enter a valid decimal value.");
    		return;
    	}

    	$(this).closest('form').ajaxSubmit({
    		success: function(data){
    			doAdminSearch();
    		},
    		error:function(){
    			alert("There was a problem processing your request. Please contact the site administrator.");
    		}
    	});
    });

    $('.scroll-data').scroll(function(){
        $('.tip-for-tbody').hide();
    });

    //Scroller
    if($('.scroll-pane').length){
        $('.scroll-pane').jScrollPane();
    }

    //Tree
    $('.tree a').live('click',function(){
        if($(this).parent().has('li').length){
            if($(this).hasClass('unfolder')){
                $(this).removeClass('unfolder');
                $(this).parents('li').find('ul').removeClass('hidden');
            }else{
                $(this).addClass('unfolder');
                $(this).parents('li').find('ul').addClass('hidden');
            }
        }
        if($('.scroll-pane').length){
            $('.scroll-pane').jScrollPane();
        }
    });

    //Toggle
    $('.toggle a').live('click',function(){
        if($(this).hasClass('unfolder')){
            $(this).removeClass('unfolder');
            $(this).parents('.table-section').find('.tree-panel').show();
            $(this).parents('.table-section').find('.result-tree').css('width','192px');
            $(this).parents('.table-section').find('.result-tree-title h4').show();
        }else{
            $(this).addClass('unfolder');
            $(this).parents('.table-section').find('.tree-panel').hide();
            $(this).parents('.table-section').find('.result-tree').css('width','6px');
            $(this).parents('.table-section').find('.result-tree-title h4').hide();
        }
    });

    $('#input_prices .toggle a', '#search_products .toggle a').live('click',function(){
        if($(this).hasClass('unfolder')){
            $(this).parents('.result-section').css('height',$(this).parents('.result-section').find('.scroll-section').height());
        }else{
            $(this).parents('.result-section').css('height',$(this).parents('.result-section').find('.scroll-section').height()+17);
        }
    });


    var resultTable;
    //Search Select All
    $('.filter-body .filter-search .checkbox').live('click', function () {
        if($(this).attr('checked')){
            //$(this).parents('.column').find('.scroll li').addClass('selected');
            //$(this).parents('.column').find('.scroll .checkbox').attr('checked',true);
            var idx = $(".result-section .result-table #" + resultTable + " th:contains('" + $(this).prev().text() + "')").index();
            $(".result-section .result-table #" + resultTable + " col:eq(" + idx + ")").show();
            $(".result-section .result-table #" + resultTable + " th:eq(" + idx + ")").show();
            $(".result-section .result-table #" + resultTable + " tr").each(function () { $(this).find("td:eq(" + idx + ")").show(); });
        } else {
            var idx = $(".result-section .result-table #" + resultTable + " th:contains('" + $(this).prev().text() + "')").index();
            $(".result-section .result-table #" + resultTable + " col:eq(" + idx + ")").hide();
            $(".result-section .result-table #" + resultTable + " th:eq(" + idx + ")").hide();
            $(".result-section .result-table #" + resultTable + " tr").each(function () { $(this).find("td:eq(" + idx + ")").hide(); });
            //$(this).parents('.column').find('.scroll li').removeClass('selected');
            //$(this).parents('.column').find('.scroll .checkbox').attr('checked',false);
        }
    });

    $('.filter-body .scroll input[type=checkbox]').live('click',function(){
    	var checkedFlag = $(this).attr('checked')?true:false;
        if(checkedFlag){
            $(this).parent('li').addClass('selected');
        }else{
            $(this).parent('li').removeClass('selected');
            $(this).attr('checked', false);
            $(this).parent('li.hasChildren').removeClass('selected');
        }

        var parentLi = $(this).parent('li');
        if($(parentLi).hasClass('hasChildren')){
        	$(parentLi).find('.children input[type="checkbox"]').attr('checked', checkedFlag);
			if($(this).attr('checked')){
				$(parentLi).find('.children li').addClass('selected');
			}else{
				$(parentLi).find('.children li').removeClass('selected');
			}
		}else{
			var parentUl = $(this).parents('ul.children');
			var totalLen= $(parentUl).find('input[name="vendor"]').length;
			var checkedLen = $(parentUl).find('input[name="vendor"]:checked').length;
			var parentInput = $(parentUl).parents('li.hasChildren').find('input[name="vendorParent"]');
			if(totalLen == checkedLen){
				$(parentInput).attr('checked',true);
				$(parentInput).parent('li.hasChildren').addClass('selected');
			}else if(checkedLen == 0){
				$(parentInput).attr('checked',false);
				$(parentInput).parent('li.hasChildren').removeClass('selected');
			}else{
				$(parentInput).attr('checked',true);
				$(parentInput).parent('li.hasChildren').removeClass('selected');
			}
		}

        if ($(this).parents('#allVendors_id').size() > 0) {
        	updateVendorFilters();
        } else if ($(this).parents('#allTypes_id').size() > 0) {
        	updateProductTypeFilters();
        } else if ($(this).parents('#allCategories_id').size() > 0) {
        	updateCategoryFilters();
        } else if ($(this).parents('#allMarkets_id').size() > 0) {
        	updateMarketFilters();
        }
    });

	$('.hasChildren label, .hasChildren .arrow').live('click',function(){
		$(this).parent().find('.children').toggle();
		$(this).parent().toggleClass("opened");
	});
	$('.children input[type="checkbox"]').live('click',function(){
		var mainLi = $(this).parents('.hasChildren');
		if($(this).parents('.children').find('input:checked').length>0 && !mainLi.hasClass('selected')){
			$(this).parent().addClass('selected');
			if($(this).parents('.children').find('input:checked').length == $(this).parents('.children').find('input').length){
				$(this).parents(".hasChildren").addClass('selected');
			}
			mainLi.find('.checkbox').attr("checked", true);
		}
		if ($(this).parents('.children').find('input:checked').length<1 && mainLi.hasClass('selected')){
			$(this).parent().removeClass('selected');
			$(this).parents(".hasChildren").removeClass('selected');
			$(this).parents(".hasChildren").find('.children li').removeClass('selected');
			mainLi.find('.checkbox').attr("checked", false);
		}
	});

    //Save search
    $('.btn-save-search').live('click',function(){
        if($('.tip-save-search').is(':hidden')){
            $('.tip-save-search').show();
            $('.tip-save-search').css({top:$(this).offset().top+20,left:$(this).offset().left-78});
        }else{
            $('.tip-save-search').hide();
        }
    });

    $('.tip-save-search a').live('click',function(){
    	if ($(this).html() == 'Cancel') {
    		$('.tip-save-search').hide();
    	} else {
    		if ($.trim($('.tip-save-search').find('input.text').val()) == "") {
    			alert("Search Name should not be empty");
    		} else {
    			saveFilters(false);
    		}
    	}
    });

    //Search checking
    $('#allTypes_id li').live('click',function(){
        var cwFlag = false;
        var otherFlag = false;
        $('#allTypes_id').find("label").each(function(index,element) {
            if ($(this).siblings().attr('checked')) {
                if ($(this).text().indexOf(" CW") != -1) {
                    if (otherFlag) {
                        alert("CW and other types should not be selected together");
                        $(this).siblings().attr('checked',false);
                        $(this).parents('li').removeClass('selected');
                        updateProductTypeFilters();
                        return;
                    } else {
                        cwFlag = true;
                    }
                } else {
                  if (cwFlag) {
                        alert("CW and other types should not be selected together");
                        $(this).siblings().attr('checked',false);
                        $(this).parents('li').removeClass('selected');
                        updateProductTypeFilters();
                        return;
                    } else {
                        otherFlag = true;
                    }
                }
            }
        });
    });

    /**
     * This triggers the admin pages search.
     */
    $('#searchForm .btn-search').live('click',function(){
    	resetAdminSearchPageNumber();
    	doAdminSearch();
    });
    /**
     * Do search on 'enter' as suggested by arylio.
     */
    $('input[name="criteria.searchTerm"], input[name="criteria.username"]').keypress(function(event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == '13') {
        	resetAdminSearchPageNumber();
        	doAdminSearch();
        }
    });


    //Search flyout
    $('.filter-button .btn-search').live('click',function(){
        var isCW = false;
        var hasTypeSelection = false;
        var hasCWOption = false;
        $('#allTypes_id').find("label").each(function(index,element) {
            if ($(this).siblings().attr('checked')) {
            	hasTypeSelection = true;
            // check the cw is checked or not.
                if ($(this).text().indexOf(" CW") != -1) {
                    isCW = true;
                }
                return;
            }

            if ($(this).text().indexOf(" CW") != -1) {
            	hasCWOption = true;
            }
        });

        if (!hasTypeSelection && hasCWOption) { // multiple types will be used by default
        	alert("Your search could return both CW and non-CW products, please select a product type filter to continue.");
        	return;
        }

        var pageSize = $('#searchForm input[name="criteria.pageSize"]').val();
        var sortColumn = $('#searchForm input[name="criteria.sortingColumn"]').val();
        var sortDir = $('#searchForm input[name="criteria.sortingType"]').val();
        var onlyLowestNetPrice = $('#searchForm input[name="onlyLowestNetPrice"]').val();

        $('#searchForm input[type="hidden"]').remove();
        $('#searchForm').append('<input type="hidden" name="onlyLowestNetPrice" value="' + onlyLowestNetPrice + '"/>');
        $('#searchForm').append('<input type="hidden" name="newSearch" value="true"/>');
        $('#searchForm').append('<input type="hidden" name="criteria.page" value="1"/>');
        $('#searchForm').append('<input type="hidden" name="criteria.pageSize" value="' + pageSize + '"/>');
    	$('#searchForm').append('<input type="hidden" name="criteria.sortingColumn" value="' + sortColumn + '"/>');
    	$('#searchForm').append('<input type="hidden" name="criteria.sortingType" value="' + sortDir + '"/>');


        // setup form with the vendor selections
        var counter = 0;
        $('#allVendors_id').each(function(ind, ele){
        	$(ele).find('.children input:checked').each(function(j, shipPoint){
                var param = '<input type="hidden" name="userFilterRecord.shipPoints[' + counter + '].id" value="' + $(shipPoint).val() + '"/>';
                $('#searchForm').append(param);
        		counter++;
        	});
        });

        /*
        $('#allVendors_id .selected').each(function(ind, ele){
            var selected = $(this).find('input').attr('checked');
            if (selected == 'checked')
            {
                var value = $(this).find('label').attr('id');
                var param = '<input type="hidden" name="userFilterRecord.shipPoints[' + ind + '].id" value="' + value + '"/>';
                $('#searchForm').append(param);
            }
        });
        */

        $('#allTypes_id .selected').each(function(ind, ele){
            var selected = $(this).find('input').attr('checked');
            if (selected == 'checked')
            {
                var value = $(this).find('label').attr('id');
                var param = '<input type="hidden" name="userFilterRecord.productTypes[' + ind + '].id" value="' + value + '"/>';
                $('#searchForm').append(param);
            }
        });

        $('#allMarkets_id .selected').each(function(ind, ele){
            var selected = $(this).find('input').attr('checked');
            if (selected == 'checked')
            {
                var value = $(this).find('label').attr('id');
                var param = '<input type="hidden" name="userFilterRecord.markets[' + ind + '].id" value="' + value + '"/>';
                $('#searchForm').append(param);
            }
        });

        $('#allBranches_id .selected').each(function(ind, ele){
            var selected = $(this).find('input').attr('checked');
            if (selected == 'checked')
            {
                var value = $(this).find('label').attr('id');
                var param = '<input type="hidden" name="userFilterRecord.branches[' + ind + '].id" value="' + value + '"/>';
                $('#searchForm').append(param);
            }
        });

        $('#allCategories_id .selected').each(function(ind, ele){
            var selected = $(this).find('input').attr('checked');
            if (selected == 'checked')
            {
                var value = $(this).find('label').attr('id');
                var param = '<input type="hidden" name="userFilterRecord.categories[' + ind + '].id" value="' + value + '"/>';
                $('#searchForm').append(param);
            }
        });

        $('#allProducts_id .selected').each(function(ind, ele){
            var selected = $(ele).find('input').attr('checked');
            if (selected == 'checked')
            {
                var value = $(ele).find('label').attr('id');
                var param = '<input type="hidden" name="userFilterRecord.products[' + ind + '].id" value="' + value + '"/>';
                $('#searchForm').append(param);
            }
        });

        if (isCW) {
            $('#whichType').remove();
            $('#searchForm').append('<input type="hidden" id="whichType" name="whichType" value="cwtype" />');
        } else {
            $('#whichType').remove();
            $('#searchForm').append('<input type="hidden" id="whichType" name="whichType" value="othertype" />');
        }

        doUserSearch();
    });

//  $('.show-CW-table').live('click', function () {
//      $('.table-section').hide();
//      $('.table-CW').show();
//      $('.comfirm-filter').hide();
//      $('.search-flyout').hide();
//      $('.tip-for-prices-click').hide();
//      $('.result-section .scroll-section').addClass('hidden-section');
//      $('.result-section .loading').show();
//      //get_sw_search();
//      //get_sw_input_prices();
//      //get_sw_adjustments();
//      //get_sw_freight();
//      //get_sw_branch_overrides();
//  });

//  $('.show-other-type-table').live('click', function () {
//      $('.table-section').hide();
//      $('.table-other-type').show();
//      $('.comfirm-filter').hide();
//      $('.search-flyout').hide();
//      $('.tip-for-prices-click').hide();
//      $('.result-section .scroll-section').addClass('hidden-section');
//      $('.result-section .loading').show();
//      get_other_search();
//      get_other_input_prices();
//      get_other_adjustments();
//      get_other_freight();
//      get_other_branch_overrides();
//  });

    if($(".date-picker").length > 0){

        $(".date-picker").datepicker({
        	minDate: new Date(),
            showOn: "button",
            buttonImage: "i/icon-date.png",
            buttonImageOnly: true
        });
    }

    //Tip
    $('.result-table td.tip').live('click',function(){
        var i = $('.result-table td.tip').index(this);
        var modificationType = $(this).hasClass('adjusted-list-price-col') ? 'REPLACE' : 'MULTIPLIER';
        var pricingRecordId = $(this).parent().find('td:eq(0) input').val();

        // requirement as interpreted by arylio
        var adjustmentValue = $(this).find('input[name="adjustmentValue"]').val();
        var adjustmentEndDate = $(this).find('input[name="adjustmentEndDate"]').val();
        var adjustmentComment = $(this).find('input[name="adjustmentComment"]').val();

        if(!$(this).hasClass('no-click-tip')){
        	$('.tip-for-prices-click input').val('');
        	$('.tip-for-prices-click textarea').val('');
        	$('.tip-for-prices-click .error').removeClass('error');

        	$('.tip-for-prices-click input[name="modificationType"]').val(modificationType);
        	$('.tip-for-prices-click input[name="pricingRecordId"]').val(pricingRecordId);

        	// per arylio this is required
        	if (adjustmentValue != '') {
        		$('.tip-for-prices-click input[name="adjustmentValue"]').val(adjustmentValue);
        		$('.tip-for-prices-click input[name="adjustmentEndDate"]').val(adjustmentEndDate);
        		$('.tip-for-prices-click textarea[name="adjustmentComment"]').val(adjustmentComment);
        	}

            $('.tip-for-prices-click').show();
            $('.tip-for-prices-click').attr('id',i);
            if($(this).position().left+$('.tip-for-prices-click').width()+100 > 960){
                $('.tip-for-prices-click').addClass('tip-for-prices-click-other');
                $('.tip-for-prices-click').css({top:$(this).offset().top-100,left:$(this).offset().left-$('.tip-for-prices-click').width()+6});
            }else{
                $('.tip-for-prices-click').removeClass('tip-for-prices-click-other');
                $('.tip-for-prices-click').css({top:$(this).offset().top-100,left:$(this).offset().left+$(this).width()-8});
            }
        }
    });

    /*
    $('.tip-for-prices-click .btn-modify').live('click',function(){
        if($('.tip-for-prices-click textarea').val() != ''){
            $('.tip-for-prices-click').hide();
            $('.tip-for-prices-click textarea').val('');
            if($('.result-table td.tip').eq(parseInt($('.tip-for-prices-click').attr('id'))).find('.text').val() != ''){
                var iText = $('.result-table td.tip').eq(parseInt($('.tip-for-prices-click').attr('id'))).find('.text').val();
                $('.result-table td.tip').eq(parseInt($('.tip-for-prices-click').attr('id'))).addClass('modify').empty().html(iText);
            }
        }else{
            $('.tip-for-prices-click textarea').addClass('error');
            $('.tip-for-prices-click .comment label').addClass('error');
        }
    });
    */

    $('.tip-for-prices-click textarea').live('focus',function(){
        $('.tip-for-prices-click textarea').removeClass('error');
        $('.tip-for-prices-click .comment label').removeClass('error');
    });

    $('.result-table td.tip').live('mouseover',function(){
    	var el = $(this);
    	if ($(el).find('input[name="adjustmentStartDate"]').val() != '') {
    		$('.tip-for-prices-hover .start-date span').text($(el).find('input[name="adjustmentStartDate"]').val());
    		$('.tip-for-prices-hover .end-date span').text($(el).find('input[name="adjustmentEndDate"]').val());
    		$('.tip-for-prices-hover .comment span').text($(el).find('input[name="adjustmentComment"]').val());
    		$('.tip-for-prices-hover .comment strong').text('Comment by ' + ($(el).find('input[name="adjustmentCreatedBy"]').val()) + ':');
    		$('.tip-for-prices-hover').show();
    		$('.tip-for-prices-hover').css({top:$(this).offset().top+6,left:$(this).offset().left-$('.tip-for-prices-hover').width()-22});
    	}
    });

    $('.result-table td.tip').live('mouseout',function(){
        $('.tip-for-prices-hover').hide();
    });

    $('.result-table td.new-PPT').live('click',function(){
        if(!$(this).children('input').length){
            $(this).removeClass('modify');
            var iText = $(this).text();
            $(this).empty().append('<input type="text" class="text" value="'+iText+'" />');
            $(this).children('input').focus();
        }
    });

    /*
    $('.result-table td.new-list').live('click',function(){
        if(!$(this).children('input').length){
            $(this).removeClass('modify');
            var iText = $(this).text();
            $(this).empty().append('<input type="text" class="text" value="'+iText+'" />');
            $(this).children('input').focus();
        }
    });

    $('.result-table td.new-multiplier').live('click',function(){
        if(!$(this).children('input').length){
            $(this).removeClass('modify');
            var iText = $(this).text();
            $(this).empty().append('<input type="text" class="text" value="'+iText+'" />');
            $(this).children('input').focus();
        }
    });
    */

    /*
    $('.result-table td.new-adjusment').live('click',function(){
        if(!$(this).children('input').length){
            $(this).removeClass('modify');
            var iText = $(this).text();
            $(this).empty().append('<input type="text" class="text" value="'+iText+'" />');
            $(this).children('input').focus();
        }
    });
    */

    /*
    $('.result-table td.new-net').live('click',function(){
        if(!$(this).children('input').length){
            $(this).removeClass('modify');
            var iText = $(this).text();
            $(this).empty().append('<input type="text" class="text" value="'+iText+'" />');
            $(this).children('input').focus();
        }
    });
    */

    $('.result-table td.new-freight').live('click',function(){
        if(!$(this).children('input').length){
            $(this).removeClass('modify');
            var iText = $(this).text();
            $(this).empty().append('<input type="text" class="text" value="'+iText+'" />');
            $(this).children('input').focus();
        }
    });

    $('.result-table td.new-branch').live('click',function(){
        if(!$(this).children('input').length){
            $(this).removeClass('modify');
            var iText = $(this).text();
            $(this).empty().append('<input type="text" class="text" value="'+iText+'" />');
            $(this).children('input').focus();
        }
    });

    $('.tip-for-prices-click .button a:not(".btn-modify")').live('click',function(){
        $('.tip-for-prices-click').hide();
    });

    //Button switch
    $('.prices .radio').live('click',function(){
        if($(this).val() == 'MULTIPLIER'){
            $(this).parents('.input-prices').find('.btn-calculate').show();
            $(this).parents('.input-prices').find('.btn-modify').hide();
        }else{
            $(this).parents('.input-prices').find('.btn-calculate').hide();
            $(this).parents('.input-prices').find('.btn-modify').show();
        }
    });

    //Switch Panel
    $('.zip-rates .switch-panel a').live('click',function(){
        $('.zip-rates .switch-panel a').removeClass('active');
        $(this).addClass('active');
        $('.zip-rates .table-wrapper').hide();
        var i = $('.switch-panel a').index(this);
        $('.zip-rates .table-wrapper').eq(i).show();
        $('.tip-for-tbody').hide();
    });

    //Select All
    $('.data th .checkbox').live('click',function(){
        if($(this).attr('checked')){
            $(this).parents('.data').find('td').find('input:checkbox').attr('checked',true);
        }else{
            $(this).parents('.data').find('td').find('input:checkbox').attr('checked',false);
        }
    });

    $('.data td input:checkbox').live('click',function(){
        var flag = true;
        $(this).parents('.data').find('tbody').find('input:checkbox').each(function(){
            if(!$(this).attr('checked')){
                flag = false;
            }
        });
        if(flag){
            $(this).parents('.data').find('th').find('input:checkbox').attr('checked',true);
        }else{
            $(this).parents('.data').find('th').find('input:checkbox').attr('checked',false);
        }
    });

    $('.column .scroll li input:checkbox').live('click',function(){
        var flag = true;
        $(this).parents('.scroll').find('input:checkbox').each(function(){
            if(!$(this).attr('checked')){
                flag = false;
            }
        });
        if(flag){
            $(this).parents('.column').find('.filter-search').find('input:checkbox').attr('checked',true);
        }else{
            $(this).parents('.column').find('.filter-search').find('input:checkbox').attr('checked',false);
        }
    });

    //Calculate
    /*
    $('.btn-calculate').live('click',function(){
        $('.table-sw-type-prices #sw-input-prices-table tbody tr').each(function(){
            $(this).find('td:eq(6)').addClass('modify').html('1710.22');
        });
    });
    */

    //Filter toggle
    $('.filter .filter-header a').live('click',function(){
        if($('.filter .filter-body').is(':hidden')){
            $('.filter .filter-body').show();
        }else{
            $('.filter .filter-body').hide();
        }
    });

    $('.filter .filter-search .text').live('focus',function(){
        if($(this).val() == 'Search'){
            $(this).val('');
            $(this).removeClass('defalut');
        }
    });

    $('.filter .filter-search .text').live('blur',function(){
        if($(this).val() == ''){
            $(this).val('Search');
            $(this).addClass('defalut');
        }
    });

    //Branch modal
    $('.branches .seach-branches .text').live('focus',function(){
        if($(this).val() == 'Search Branch'){
            $(this).val('');
            $(this).removeClass('defalut');
        }
    });

    $('.branches .seach-branches .text').live('blur',function(){
        if($(this).val() == ''){
            $(this).val('Search Branch');
            $(this).addClass('defalut');
        }
    });

    $('.branches-check li input').live('click',function(){
        if($(this).attr('checked')){
            $(this).parents('li').addClass('selected');
        }else{
            $(this).parents('li').removeClass('selected');
        }
    });

    //Sorter
    $('.data th a').live('click',function(){
        $(this).parents('th').find('a').removeClass('current');
        $(this).addClass('current');
    });

    //Browse Hack
    if($.browser.mozilla){
        $('.pagination select').css('position','relative');
        $('.pagination select').css('top','2px');
    }
    if($.browser.msie){
        $('#wrapper #main-container.admin .filter li.column .filter-search .checkbox').css('margin-top','6px');
        $('#wrapper #main-container.admin .filter li.column .filter-search .checkbox').css('margin-right','4px');
        $('#wrapper #main-container.admin .filter li.column .scroll li .checkbox').css('margin-top','0px');
        $('#wrapper #main-container.admin .filter li.column .scroll li .checkbox').css('margin-right','2px');
        $('#modal-create-user .form .row-role input, #modal-update-user .form .row-role input').css('margin-top','0px');
        $('.pagination select').css('margin-top','3px');
        $('.remember .checkbox').css('margin-top','-3px');
        $('.input-prices .radio').css('position','relative');
        $('.input-prices .radio').css('top','-3px');
        $('.input-prices .radio').css('margin-right','0px');
    }
    if($.browser.msie&&($.browser.version == "7.0")){
        $('#wrapper #main-container.admin .filter li.column .filter-search .checkbox').css('margin-top','9px');
        $('#wrapper #main-container.admin .filter li.column .scroll li .checkbox').css('margin-top','1px');
        $('.data input:checkbox').css('margin-top','4px');
        $('.pagination select').css('margin-top','1px');
        $('.input-prices .radio').css('top','-1px');
        $('.remember .checkbox').css('margin-top','-1px');
    }
    if($.browser.msie&&($.browser.version == "9.0")){
        $('#wrapper #main-container.admin .result-section').css('height','339px');
    }

//  //Vendor Name
//  $('.filter-scroll div.scroll:eq(0) li').each(function(index){
//      if(index%3 == 0){
//          $(this).find('input').live('click',function(){
//              $('.filter-scroll div.scroll:eq(1) li').show();
//              $('.filter-scroll div.scroll:eq(1) li:lt(5)').hide();
//          });
//      }else if(index%3 == 1){
//          $(this).find('input').live('click',function(){
//              $('.filter-scroll div.scroll:eq(1) li').show();
//              $('.filter-scroll div.scroll:eq(1) li:eq(0)').hide();
//              $('.filter-scroll div.scroll:eq(1) li:gt(4)').hide();
//          });
//      }else{
//          $(this).find('input').live('click',function(){
//              $('.filter-scroll div.scroll:eq(1) li').show();
//          });
//      }
//  });
//
//  //Type
//  $('.filter-scroll div.scroll:eq(1) li').each(function(index){
//      if(index%3 == 0){
//          $(this).find('input').live('click',function(){
//              $('.filter-scroll div.scroll:eq(2) li').show();
//              $('.filter-scroll div.scroll:eq(2) li:lt(3)').hide();
//          });
//      }else if(index%3 == 1){
//          $(this).find('input').live('click',function(){
//              $('.filter-scroll div.scroll:eq(2) li').show();
//          });
//      }else{
//          $(this).find('input').live('click',function(){
//              $('.filter-scroll div.scroll:eq(2) li').show();
//              $('.filter-scroll div.scroll:eq(2) li:eq(0)').hide();
//              $('.filter-scroll div.scroll:eq(2) li:gt(2)').hide();
//          });
//      }
//  });
//
//  //Market
//  $('.filter-scroll div.scroll:eq(2) li').each(function(index){
//      if(index%3 == 0){
//          $(this).find('input').live('click',function(){
//              $('.filter-scroll div.scroll:eq(3) li.hidden').hide();
//              $('.filter-scroll div.scroll:eq(3) li:not(".hidden")').show();
//          });
//      }else if(index%3 == 1){
//          $(this).find('input').live('click',function(){
//              $('.filter-scroll div.scroll:eq(3) li').hide();
//              $('.filter-scroll div.scroll:eq(3) li.hidden').show();
//          });
//      }else{
//          $(this).find('input').live('click',function(){
//              $('.filter-scroll div.scroll:eq(3) li.hidden').hide();
//              $('.filter-scroll div.scroll:eq(3) li:not(".hidden")').show();
//              $('.filter-scroll div.scroll:eq(3) li:lt(16)').hide();
//          });
//      }
//  });
//
//  //Branch
//  $('.filter-scroll div.scroll:eq(3) li').each(function(index){
//      if(index%3 == 0){
//          $(this).find('input').live('click',function(){
//              $('.filter-scroll div.scroll:eq(4) li').show();
//              for(i=0;i<$('.filter-scroll div.scroll:eq(4) li').length;i++){
//                  if(i%2 == 0){
//                      $('.filter-scroll div.scroll:eq(4) li').eq(i).hide();
//                  }
//              }
//          });
//      }else if(index%3 == 1){
//          $(this).find('input').live('click',function(){
//              $('.filter-scroll div.scroll:eq(4) li').show();
//              for(i=0;i<$('.filter-scroll div.scroll:eq(4) li').length;i++){
//                  if(i%2 == 1){
//                      $('.filter-scroll div.scroll:eq(4) li').eq(i).hide();
//                  }
//              }
//          });
//      }else{
//          $(this).find('input').live('click',function(){
//              $('.filter-scroll div.scroll:eq(4) li').show();
//          });
//      }
//  });
//
//  //Category
//  $('.filter-scroll div.scroll:eq(4) li').each(function(index){
//      if(index%3 == 0){
//          $(this).find('input').live('click',function(){
//              $('.filter-scroll div.scroll:eq(5) li').show();
//              $('.filter-scroll div.scroll:eq(5) li:not(".hidden")').hide();
//              $('.filter-scroll div.scroll:eq(5) li.hidden:lt(2)').hide();
//          });
//      }else if(index%3 == 1){
//          $(this).find('input').live('click',function(){
//              $('.filter-scroll div.scroll:eq(5) li').show();
//              $('.filter-scroll div.scroll:eq(5) li:not(".hidden")').hide();
//              $('.filter-scroll div.scroll:eq(5) li.hidden:gt(1)').hide();
//          });
//      }else{
//          $(this).find('input').live('click',function(){
//              $('.filter-scroll div.scroll:eq(5) li').show();
//              $('.filter-scroll div.scroll:eq(5) li.hidden').hide();
//          });
//      }
//  });

    // @since Pricing Calculation - do apply data
    $('.input-prices .btn-modify, .input-prices .btn-calculate').live('click', function() {
    	var typeContext = $(this).parents('.input-prices');

    	// clear validation errors
    	$(typeContext).find('.error').removeClass('error');

    	copyFilters(); // include existing filters to the request
        $('#searchForm input[name="delta.type"]').remove();
        $('#searchForm input[name="delta.value"]').remove();
        $('#searchForm input[name="delta.endDate"]').remove();
        $('#searchForm input[name="delta.comments"]').remove();

    	// verify an input type is selected
    	var modificationType = $(typeContext).find('input[name="modificationType"]:checked').val();
    	$('#searchForm').append('<input type="hidden" name="delta.type" value="' + modificationType + '"/>');

    	var modificationValue = $(typeContext).find('input[name="incrementValue"]').val();
    	if (modificationType == 'PERCENTAGE') {
    		modificationValue = $(typeContext).find('input[name="percentValue"]').val();
    	}

    	if (modificationType == 'MULTIPLIER') {
    		modificationValue = $(typeContext).find('input[name="multiplierValue"]').val();
    	}

    	var endDate = $(typeContext).find('input[name="endDate"]').val();
    	var comments = $(typeContext).find('input[name="comments"]').val();
    	$('#searchForm').append('<input type="hidden" name="delta.comments" value="' + comments + '"/>');

    	if ($.trim(modificationValue) == '') {
    		alert('Please enter a value for the change.');
    		return;
    	}

    	var valid = (modificationValue.match(/^-?\d*(\.\d+)?$/));
    	if (!valid) {
    		alert('Change value must be a number.');
    		return;
    	} else {
        	$('#searchForm').append('<input type="hidden" name="delta.value" value="' + modificationValue + '"/>');
    	}

    	if($('.input-prices-tip-for-prices-click').length > 0){
	    	if (modificationType == 'MULTIPLIER' && parseFloat(modificationValue) > MAX_MULTIPLIER) {
	    		alert('Maximum multiplier supported is ' + MAX_MULTIPLIER + '.');
	    		return;
	    	}
    	}

    	if ($.trim(endDate) == '') {
    		alert('Please enter an end date.');
    		return;
    	} else {
        	$('#searchForm').append('<input type="hidden" name="delta.endDate" value="' + endDate + '"/>');
    	}

    	if ($.trim(comments) == '') {
    		alert('Please enter a comment.');
    		return;
    	}

    	if (comments.length > 200) {
    		alert('The comment length is too large, please ensure it is less than 200.');
    		return;
    	}

    	 if($('.input-prices-tip-for-prices-click').length > 0){
         	doApplyChangesToSelectedRecords();
         }else if($('.adjustment-tip-for-prices-click').length > 0){
         	doApplyChangesToAdjustmentRecords();
         }else if($('.freight-tip-for-prices-click').length > 0){
         	doApplyChangesToFreightRecords();
         }else if($('.branch-overrides-tip-for-prices-click').length > 0){
         	doApplyChangesToBranchOverrideRecords();
         }
    });

    // apply inline data
    $('.tip-for-prices-click .btn-modify').live('click',function(){
    	// hide tip hover
    	$('.tip-for-prices-hover').hide();
    	// clear validation errors
    	$('.tip-for-prices-click error').removeClass('error');

    	copyFilters(); // include existing filters to the request
        $('#searchForm input[name="delta.type"]').remove();
        $('#searchForm input[name="delta.value"]').remove();
        $('#searchForm input[name="delta.endDate"]').remove();
        $('#searchForm input[name="delta.comments"]').remove();

    	// verify an input type is selected
    	var modificationType = $('.tip-for-prices-click input[name="modificationType"]').val();
    	var pricingRecordId = $('.tip-for-prices-click input[name="pricingRecordId"]').val();
    	$('#searchForm').append('<input type="hidden" name="delta.type" value="' + modificationType + '"/>');

    	var modificationValue = $('.tip-for-prices-click input[name="adjustmentValue"]').val();

    	var endDate = $('.tip-for-prices-click input[name="adjustmentEndDate"]').val();
    	var comments = $('.tip-for-prices-click textarea[name="adjustmentComment"]').val();
    	$('#searchForm').append('<input type="hidden" name="delta.comments" value="' + comments + '"/>');

    	if ($.trim(modificationValue) == '') {
    		alert('Please enter a value for the change.');
    		return;
    	}

    	var valid = (modificationValue.match(/^-?\d*(\.\d+)?$/));
    	if (!valid) {
    		alert('Change value must be a number.');
    		return;
    	} else {
        	$('#searchForm').append('<input type="hidden" name="delta.value" value="' + modificationValue + '"/>');
    	}

    	if (modificationType == 'MULTIPLIER' && parseFloat(modificationValue) > MAX_MULTIPLIER) {
    		alert('Maximum multiplier supported is ' + MAX_MULTIPLIER + '.');
    		return;
    	}


    	if ($.trim(endDate) == '') {
    		alert('Please enter an end date.');
    		return;
    	} else {
        	$('#searchForm').append('<input type="hidden" name="delta.endDate" value="' + endDate + '"/>');
    	}

    	if ($.trim(comments) == '') {
    		alert('Please enter a comment.');
    		return;
    	}

    	if (comments.length > 200) {
    		alert('The comment length is too large, please ensure it is less than 200.');
    		return;
    	}

        if($('.tip-for-prices-click textarea').val() == ''){
            $('.tip-for-prices-click textarea').addClass('error');
            $('.tip-for-prices-click .comment label').addClass('error');
            return;
        }

        if($('.input-prices-tip-for-prices-click').length > 0){
        	doApplyChangesToSelectedRecords(pricingRecordId);
        }else if($('.adjustment-tip-for-prices-click').length > 0){
        	doApplyChangesToAdjustmentRecords(pricingRecordId);
        }else if($('.freight-tip-for-prices-click').length > 0){
        	doApplyChangesToFreightRecords(pricingRecordId);
        }else if($('.branch-overrides-tip-for-prices-click').length > 0){
        	doApplyChangesToBranchOverrideRecords(pricingRecordId);
        }
    });

    // some additional requirements by arylio
    $('.column-percentage input[name="percentValue"]').live('focus', function() {
    	$(this).closest('div').find('input:eq(0)').click(); // trigger on click event
    });
    $('.column-increase input[name="incrementValue"]').live('focus', function() {
    	$(this).closest('div').find('input:eq(0)').click(); // trigger on click event
    });
    $('.column-multiplier input[name="multiplierValue"]').live('focus', function() {
    	$('.column-multiplier input:eq(0)').click();
    });

    $('.btn-increase').live('click', function() {
    	$(this).closest('.column').find('input:eq(0)').click(); // trigger on click event
    	var el = $(this).closest('.column').find('input:eq(1)');

    	if (!(/^-?\d+$/.test($(el).val()))) {
    		alert("Value must contain an integer.");
    		return;
    	}

    	var currentVal = parseFloat($(el).val());
    	if (isNaN(currentVal)) {
    		currentVal = 0;
    	}

    	if (currentVal == 99999999) {
    		alert("Value has reached the maximum allowed.");
    		return;
    	}

    	$(el).val(currentVal + 1);
    });

    $('.btn-decrease').live('click', function() {
    	$(this).closest('.column').find('input:eq(0)').click(); // trigger on click event
    	var el = $(this).closest('.column').find('input:eq(1)');

    	if (!(/^-?\d+$/.test($(el).val()))) {
    		alert("Value must contain an integer.");
    		return;
    	}

    	var currentVal = parseFloat($(el).val());
    	if (isNaN(currentVal)) {
    		currentVal = 0;
    	}

    	if (currentVal == -9999999) {
    		alert("Value has reached the minimum allowed.");
    		return;
    	}
    	$(el).val(currentVal - 1);
    });

    $('.table-data .btn-export').live('click', function() {
    	if ($(this).hasClass('disabled')) {
    		return false;
    	}

    	if (!$('.no-data').is(':visible')) {
    		doExportResults();
    	} else {
    		alert('No data to export');
    	}
    });

    $('#switchToInputPrice').live('click', function() {
    	$('.main-nav .nav-link .active').removeClass('active');;
    	$('.main-nav .nav-link a.input_prices').parent().addClass('active');;
    	var pageSize = $('#search_products .per-page-show select').val();
    	$('#search_products > h3').text('INPUT PRICES');
    	$('#search_products').attr("id", 'input_prices');
    	copyFilters();

    	$('#searchForm input[name="newSearch"]').val(true);
        $.ajax({
            type: "GET",
            url: datactx + "/SwitchSearchTabAction.action?selectedTab=inputPrices",
            success: function(data) {
                $('#main-container .table-section').remove();
                $('#main-container .tip-save-search').before(data);
                $('#switchToInputPrice').remove();

                // retain page size
                $('#input_prices .per-page-show .last select').val(pageSize);

                $("#main-container .date-picker").datepicker({
                	minDate: new Date(),
                    showOn: "button",
                    buttonImage: "i/icon-date.png",
                    buttonImageOnly: true
                });

                doUserSearch();

                // Adjust the tip-for-prices-click's class name
                $('.tip-for-prices-click').removeClass('input-prices-tip-for-prices-click adjustment-tip-for-prices-click freight-tip-for-prices-click branch-overrides-tip-for-prices-click').addClass('input-prices-tip-for-prices-click');
            },
            error:function(){
            	alert("There was a problem processing your request. Please contact the site administrator.");
            }
        });
        return false;
    });

    $('#switchToAdjustment').live('click', function() {
    	$('.main-nav .nav-link .active').removeClass('active');;
    	$('.main-nav .nav-link a.adjustments').parent().addClass('active');;
    	var pageSize = $('#search_products .per-page-show select').val();
    	if(pageSize == null || typeof(pageSize) == "undefined"){
    		pageSize = $('#input_prices .per-page-show select').val();
    	}
    	$('#search_products > h3').text('ADJUSTMENTS');
    	$('#input_prices > h3').text('ADJUSTMENTS');
    	$('#search_products').attr("id", 'input_prices');
    	copyFilters();

    	$('#searchForm input[name="newSearch"]').val(true);
    	$.ajax({
    		type: "GET",
    		url: datactx + "/SwitchSearchTabAction.action?selectedTab=adjustment",
    		success: function(data) {
    			$('#main-container .table-section').remove();
    			$('#main-container .tip-save-search').before(data);
    			$('#switchToAdjustment').remove();

    			// retain page size
    			$('#input_prices .per-page-show .last select').val(pageSize);

    			$("#main-container .date-picker").datepicker({
    				minDate: new Date(),
    				showOn: "button",
    				buttonImage: "i/icon-date.png",
    				buttonImageOnly: true
    			});

    			doUserSearch();

                // Adjust the tip-for-prices-click's class name
                $('.tip-for-prices-click').removeClass('input-prices-tip-for-prices-click adjustment-tip-for-prices-click freight-tip-for-prices-click branch-overrides-tip-for-prices-click').addClass('adjustment-tip-for-prices-click');
    		},
    		error:function(){
    			alert("There was a problem processing your request. Please contact the site administrator.");
    		}
    	});
    	return false;
    });

    $('#switchToFreight').live('click', function() {
    	$('.main-nav .nav-link .active').removeClass('active');;
    	$('.main-nav .nav-link a.freight').parent().addClass('active');;
    	var pageSize = $('#search_products .per-page-show select').val();
    	if(pageSize == null || typeof(pageSize) == "undefined"){
    		pageSize = $('#input_prices .per-page-show select').val();
    	}
    	$('#search_products > h3').text('FREIGHT');
    	$('#input_prices > h3').text('FREIGHT');
    	$('#search_products').attr("id", 'input_prices');
    	copyFilters();

    	$('#searchForm input[name="newSearch"]').val(true);
    	$.ajax({
    		type: "GET",
    		url: datactx + "/SwitchSearchTabAction.action?selectedTab=freight",
    		success: function(data) {
    			$('#main-container .table-section').remove();
    			$('#main-container .tip-save-search').before(data);
    			$('#switchToAdjustment').remove();

    			// retain page size
    			$('#input_prices .per-page-show .last select').val(pageSize);

    			$("#main-container .date-picker").datepicker({
    				minDate: new Date(),
    				showOn: "button",
    				buttonImage: "i/icon-date.png",
    				buttonImageOnly: true
    			});

    			doUserSearch();

                // Adjust the tip-for-prices-click's class name
                $('.tip-for-prices-click').removeClass('input-prices-tip-for-prices-click adjustment-tip-for-prices-click freight-tip-for-prices-click branch-overrides-tip-for-prices-click').addClass('freight-tip-for-prices-click');
    		},
    		error:function(){
    			alert("There was a problem processing your request. Please contact the site administrator.");
    		}
    	});
    	return false;
    });

    $('#switchToBranchOverrides').live('click', function() {
    	$('.main-nav .nav-link .active').removeClass('active');;
    	$('.main-nav .nav-link a.branch_overrides').parent().addClass('active');;
    	var pageSize = $('#search_products .per-page-show select').val();
    	if(pageSize == null || typeof(pageSize) == "undefined"){
    		pageSize = $('#input_prices .per-page-show select').val();
    	}
    	$('#search_products > h3').text('BRANCH OVERRIDES');
    	$('#input_prices > h3').text('BRANCH OVERRIDES');
    	$('#search_products').attr("id", 'input_prices');
    	copyFilters();

    	$('#searchForm input[name="newSearch"]').val(true);
    	$.ajax({
    		type: "GET",
    		url: datactx + "/SwitchSearchTabAction.action?selectedTab=branchOverride",
    		success: function(data) {
    			$('#main-container .table-section').remove();
    			$('#main-container .tip-save-search').before(data);
    			$('#switchToAdjustment').remove();

    			// retain page size
    			$('#input_prices .per-page-show .last select').val(pageSize);

    			$("#main-container .date-picker").datepicker({
    				minDate: new Date(),
    				showOn: "button",
    				buttonImage: "i/icon-date.png",
    				buttonImageOnly: true
    			});

    			doUserSearch();

                // Adjust the tip-for-prices-click's class name
                $('.tip-for-prices-click').removeClass('input-prices-tip-for-prices-click adjustment-tip-for-prices-click freight-tip-for-prices-click branch-overrides-tip-for-prices-click').addClass('branch-overrides-tip-for-prices-click');
    		},
    		error:function(){
    			alert("There was a problem processing your request. Please contact the site administrator.");
    		}
    	});
    	return false;
    });

    // admin paging scripts
    /**
     * Limit format for phone/fax numbers.
     */
    if ($.mask) {
    	$('.phoneNumber .phone-small').mask('999');
    	$('.phoneNumber .phone').mask('9999');
    	$('.zip-text').mask('99999');
    }
    /**
     * Jump to specific page.
     */
    $('.page-jump input:text').live('keyup', function(event) {
    	var keycode = (event.keyCode ? event.keyCode : event.which);
    	if(keycode != 13) {
    		return;
    	}
    	var page = parseInt($(this).val());
    	var totalPages = parseInt($(this).parent().find('em').text());
    	if (!(/^\d+$/.test(page)) || isNaN(page) || page > totalPages || page < 1) {
    		alert("Invalid page number.");
    		return;
    	}

    	$('#searchForm input[name="criteria.page"]').val(page);
    	$('#searchForm input[name="page"]').val(page);
    	if ($('#searchForm input[name="criteria.searchTerm"]').length > 0 ||
    			$('#searchForm input[name="adminSearch"]').length > 0) {
    		doAdminSearch();
    	} else {
            if($(this).parents('.my-saved-filter').length > 0){
                $('.my-saved-filter input[name="userFilterRecordCriteria.page"]').val(page);
    			doSavedFilterSearch();
            } else if($(this).parents('.my-pricings').length > 0){
                $('.my-pricings input[name="myPricingSearchCriteria.page"]').val(page);
    			doPricingSearch();
    		} else {
    			$('#searchForm input[name="newSearch"]').val(false);
    			doUserSearch();
    		}
    	}
    });
    /**
     * Jump to specific page.
     */
    $('.page-jump .btn-go').live('click', function() {
    	var page = parseInt($(this).parent().find('input').first().val());
    	var totalPages = parseInt($(this).parent().find('em').text());
    	if (!(/^\d+$/.test($(this).parent().find('input').first().val())) || isNaN(page) || page > totalPages || page < 1) {
    		alert("Invalid page number.");
    		return;
    	}

    	$('#searchForm input[name="criteria.page"]').val(page);
    	$('#searchForm input[name="page"]').val(page);
    	if ($('#searchForm input[name="criteria.searchTerm"]').length > 0 ||
    			$('#searchForm input[name="adminSearch"]').length > 0) {
    		doAdminSearch();
    	} else {
            if($(this).parents('.my-saved-filter').length > 0){
                $('.my-saved-filter input[name="userFilterRecordCriteria.page"]').val(page);
    			doSavedFilterSearch();
            } else if($(this).parents('.my-pricings').length > 0){
                $('.my-pricings input[name="myPricingSearchCriteria.page"]').val(page);
    			doPricingSearch();
    		} else {
    			$('#searchForm input[name="newSearch"]').val(false);
    			doUserSearch();
    		}
    	}
    });

    /**
     * User clicks a page number.
     */
    $('.pageLinks a.page').live('click', function() {
    	var page = $(this).text();
    	$('#searchForm input[name="criteria.page"]').val(page);
    	$('#searchForm input[name="page"]').val(page);
    	if ($('#searchForm input[name="criteria.searchTerm"]').length > 0 ||
    			$('#searchForm input[name="adminSearch"]').length > 0) {
    		doAdminSearch();
    	} else {
            if($(this).parents('.my-saved-filter').length > 0){
                $('.my-saved-filter input[name="userFilterRecordCriteria.page"]').val(page);
    			doSavedFilterSearch();
            } else if($(this).parents('.my-pricings').length > 0){
                $('.my-pricings input[name="myPricingSearchCriteria.page"]').val(page);
    			doPricingSearch();
            } else {
    			$('#searchForm input[name="newSearch"]').val(false);
    			doUserSearch();
    		}
    	}
    });

    /**
     * User changes the page size.
     */
    $('#searchForm select.pageSize, .zip-rates select.pageSize, .state-rates select.pageSize').live('change', function() {
    	var value = $(this).val();
    	$('#searchForm select.pageSize').val(value);
    	$('.state-rates select.pageSize').val(value);
    	$('.zip-rates select.pageSize').val(value);
    	resetAdminSearchPageNumber();
    	doAdminSearch();
    });

    /**
     * User changes the page size.
     */
    $('#input_prices .per-page-show .last select, #search_products .per-page-show .last select').live('change', function() {
    	var value = $(this).val();
    	$('#input_prices .per-page-show select').val(value);
    	$('#search_products .per-page-show select').val(value);
    	resetAdminSearchPageNumber();
    	$('#searchForm input[name="criteria.pageSize"]').val(value);
    	$('#searchForm input[name="pageSize"]').val(value);
    	$('#searchForm input[name="newSearch"]').val(false);
    	doUserSearch();
    });

    /**
     * User changes the page size.
     */
    $('.my-saved-filter .per-page-show .last select').live('change', function() {
    	var value = $(this).val();
    	$('.my-saved-filter .per-page-show select').val(value);
    	$('.my-saved-filter input[name="userFilterRecordCriteria.page"]').val(1);
    	$('.my-saved-filter input[name="userFilterRecordCriteria.pageSize"]').val(value);
        doSavedFilterSearch();
    });

    /**
     * User changes the page size.
     */
    $('.my-pricings .per-page-show .last select').live('change', function() {
    	var value = $(this).val();
    	$('.my-pricings .per-page-show select').val(value);
    	$('.my-pricings input[name="myPricingSearchCriteria.page"]').val(1);
    	$('.my-pricings input[name="myPricingSearchCriteria.pageSize"]').val(value);
        doPricingSearch();
    });

    /**
     * User clicks on a sortable on the war room page.
     */
    $('.my-saved-filter .sortable-header').live('click', function() {
    	var column = $(this).find('a').first().attr("rel");
    	var currentSortCol = $('.my-saved-filter input[name="userFilterRecordCriteria.sortingColumn"]').val();
    	var currentSortDir = $('.my-saved-filter input[name="userFilterRecordCriteria.sortingType"]').val();
    	if (column == currentSortCol) {
    		// reverse direction
    		if (currentSortDir == 'ASC') {
    			$('.my-saved-filter input[name="userFilterRecordCriteria.sortingType"]').val('DESC');
    		} else {
    			$('.my-saved-filter input[name="userFilterRecordCriteria.sortingType"]').val('ASC');
    		}
    	} else {
			$('.my-saved-filter input[name="userFilterRecordCriteria.sortingColumn"]').val(column);
			$('.my-saved-filter input[name="userFilterRecordCriteria.sortingType"]').val('ASC');
    	}

    	$('.my-saved-filter input[name="userFilterRecordCriteria.page"]').val(1);
    	doSavedFilterSearch();
    });

    /**
     * User clicks on a sortable on the war room page.
     */
    $('.my-pricings .sortable-header').live('click', function() {
    	var column = $(this).find('a').first().attr("rel");
    	var currentSortCol = $('.my-pricings input[name="myPricingSearchCriteria.sortingColumn"]').val();
    	var currentSortDir = $('.my-pricings input[name="myPricingSearchCriteria.sortingType"]').val();
    	if (column == currentSortCol) {
    		// reverse direction
    		if (currentSortDir == 'ASC') {
    			$('.my-pricings input[name="myPricingSearchCriteria.sortingType"]').val('DESC');
    		} else {
    			$('.my-pricings input[name="myPricingSearchCriteria.sortingType"]').val('ASC');
    		}
    	} else {
    		$('.my-pricings input[name="myPricingSearchCriteria.sortingColumn"]').val(column);
    		$('.my-pricings input[name="myPricingSearchCriteria.sortingType"]').val('ASC');
    	}

    	$('.my-pricings input[name="myPricingSearchCriteria.page"]').val(1);
    	doPricingSearch();
    });

    /**
     * User clicks on a sortable header.
     */
    $('#searchForm .sortable-header').live('click', function() {
    	var column = $(this).find('a').first().attr("rel");
    	var currentSortCol = $('#searchForm input[name="criteria.sortingColumn"]').val();
    	var currentSortDir = $('#searchForm input[name="criteria.sortingType"]').val();
    	if (column == currentSortCol) {
    		// reverse direction
    		if (currentSortDir == 'ASC') {
    			$('#searchForm input[name="criteria.sortingType"]').val('DESC');
    		} else {
    			$('#searchForm input[name="criteria.sortingType"]').val('ASC');
    		}
    	} else {
			$('#searchForm input[name="criteria.sortingColumn"]').val(column);
			$('#searchForm input[name="criteria.sortingType"]').val('ASC');

    	}

    	resetAdminSearchPageNumber();
    	doAdminSearch();
    });

    /**
     * User clicks on lowestNetPrice checkbox.
     */
    $('#lowestNetPrice').live('click', function() {
    	var onlyLowestNetPrice = $(this).attr('checked') == 'checked';
    	$('#searchForm input[name="onlyLowestNetPrice"]').val(onlyLowestNetPrice);
        if(onlyLowestNetPrice){
            $('.lowestNetPrice').attr('checked', true);
        }else{
            $('.lowestNetPrice').removeAttr('checked');
        }

    	resetAdminSearchPageNumber();
    	$('#searchForm input[name="newSearch"]').val(false);
    	doUserSearch();
    });

    /**
     * User clicks on a sortable header.
     */
    $('#other-input-prices-table .sortable-header, #sw-input-prices-table .sortable-header').live('click', function() {
    	var column = $(this).find('a').first().attr("rel");
    	var currentSortCol = $('#searchForm input[name="criteria.sortingColumn"]').val();
    	var currentSortDir = $('#searchForm input[name="criteria.sortingType"]').val();
    	if (column == currentSortCol) {
    		// reverse direction
    		if (currentSortDir == 'ASC') {
    			$('#searchForm input[name="criteria.sortingType"]').val('DESC');
    		} else {
    			$('#searchForm input[name="criteria.sortingType"]').val('ASC');
    		}
    	} else {
    		$('#searchForm input[name="criteria.sortingColumn"]').val(column);
    		$('#searchForm input[name="criteria.sortingType"]').val('ASC');

    	}
    	resetAdminSearchPageNumber();
    	$('#searchForm input[name="newSearch"]').val(false);
    	doUserSearch();
    });

    if ($('#resultEntityType').size() > 0) {
    	doAdminSearch();
    }

    $('.drilldownItem a').live('click', function() {
    	$('.drilldownItem').removeClass('selected');
    	if ($(this).find('span.remove').size() > 0) {
    		doDrillDown("", "");
    		$('.drilldownItem span.remove').remove();
    	} else {
    		$('.drilldownItem span.remove').remove();
    		if ($(this).parents('.vendorDrillDown').size() > 0) {
    			doDrillDown("ShipPoint", $(this).attr("rel"));
    		} else if ($(this).parents('.productTypeDrillDown').size() > 0) {
    			doDrillDown("ProductType", $(this).attr("rel"));
    		} else if ($(this).parents('.marketDrillDown').size() > 0) {
    			doDrillDown("Market", $(this).attr("rel"));
    		} else if ($(this).parents('.branchDrillDown').size() > 0) {
    			doDrillDown("Branch", $(this).attr("rel"));
    		} else if ($(this).parents('.categoryDrillDown').size() > 0) {
    			doDrillDown("Category", $(this).attr("rel"));
    		} else if ($(this).parents('.productDrillDown').size() > 0) {
    			doDrillDown("Product", $(this).attr("rel"));
    		}
    		$(this).closest('.drilldownItem').addClass('selected');
    		$(this).append(' <span class="remove">[ X ]</span>');
    	}
    });

    $('#btn_save_all_input_freight').live('click', function() {
    	$.ajax({
    		type: "POST",
    		url: datactx + "/ajax/SaveAllInputPriceChangesAction.action",
    		success: function(data) {
    			if(data.returnMessage != null){
    				alert(data.returnMessage);
    				return;
    			}
    			// If number is zero, it means no data select, no need do anything
    			if(data.numSavedEntities != 0) {
    				saveCriteriaAndMyPricing();
	    			alert(data.numSavedEntities + ' data have been saved.');

	    			resetAdminSearchPageNumber();
	    			$('#searchForm input[name="newSearch"]').val(true);
	    			doUserSearch();
    			}
    		},
    		error:function(){
    			alert("There was a problem processing your request. Please contact the site administrator.");
    		}
    	});
    });

    $('#btn_undo_input_freight').live('click', function() {
    	$.ajax({
    		type: "POST",
    		url: datactx + "/ajax/UndoInputPriceChangesAction.action",
    		success: function(data) {
    			// If number is zero, it means no data select, no need do anything
    			if(data.numSavedEntities != 0) {
    				alert(data.numSavedEntities + ' data have been restored.');

	    			resetAdminSearchPageNumber();
	    			$('#searchForm input[name="newSearch"]').val(true);
	    			doUserSearch();
    			}
    		},
    		error:function(){
    			alert("There was a problem processing your request. Please contact the site administrator.");
    		}
    	});
    });

    $('#btn_save_all_adjustment').live('click', function() {
    	$.ajax({
    		type: "POST",
    		url: datactx + "/ajax/SaveAllAdjustmentChangesAction.action",
    		success: function(data) {
    			// If number is zero, it means no data select, no need do anything
    			if(data.numSavedEntities != 0) {
    				saveCriteriaAndMyPricing();
    				alert(data.numSavedEntities + ' data have been saved.');

    				resetAdminSearchPageNumber();
    				$('#searchForm input[name="newSearch"]').val(true);
    				doUserSearch();
    			}
    		},
    		error:function(){
    			alert("There was a problem processing your request. Please contact the site administrator.");
    		}
    	});
    });

    $('#btn_undo_adjustment').live('click', function() {
    	$.ajax({
    		type: "POST",
    		url: datactx + "/ajax/UndoAdjustmentChangesAction.action",
    		success: function(data) {
    			// If number is zero, it means no data select, no need do anything
    			if(data.numSavedEntities != 0) {
    				alert(data.numSavedEntities + ' data have been restored.');

	    	    	resetAdminSearchPageNumber();
	    	    	$('#searchForm input[name="newSearch"]').val(true);
	    	    	doUserSearch();
    			}
    		},
    		error:function(){
    			alert("There was a problem processing your request. Please contact the site administrator.");
    		}
    	});
    });

    $('#btn_save_all_freight').live('click', function() {
    	$.ajax({
    		type: "POST",
    		url: datactx + "/ajax/SaveAllFreightChangesAction.action",
    		success: function(data) {
    			// If number is zero, it means no data select, no need do anything
    			if(data.numSavedEntities != 0) {
    				saveCriteriaAndMyPricing();
	    			alert(data.numSavedEntities + ' data have been saved.');

	    			resetAdminSearchPageNumber();
	    			$('#searchForm input[name="newSearch"]').val(true);
	    			doUserSearch();
    			}
    		},
    		error:function(){
    			alert("There was a problem processing your request. Please contact the site administrator.");
    		}
    	});
    });

    $('#btn_undo_freight').live('click', function() {
    	$.ajax({
    		type: "POST",
    		url: datactx + "/ajax/UndoFreightChangesAction.action",
    		success: function(data) {
    			// If number is zero, it means no data select, no need do anything
    			if(data.numSavedEntities != 0) {
    				alert(data.numSavedEntities + ' data have been restored.');

	    			resetAdminSearchPageNumber();
	    			$('#searchForm input[name="newSearch"]').val(true);
	    			doUserSearch();
    			}
    		},
    		error:function(){
    			alert("There was a problem processing your request. Please contact the site administrator.");
    		}
    	});
    });

    $('#btn_save_all_branch_override').live('click', function() {
    	$.ajax({
    		type: "POST",
    		url: datactx + "/ajax/SaveAllBranchOverrideChangesAction.action",
    		success: function(data) {
    			// If number is zero, it means no data select, no need do anything
    			if(data.numSavedEntities != 0) {
    				saveCriteriaAndMyPricing();
	    			alert(data.numSavedEntities + ' data have been saved.');

	    			resetAdminSearchPageNumber();
	    			$('#searchForm input[name="newSearch"]').val(true);
	    			doUserSearch();
    			}
    		},
    		error:function(){
    			alert("There was a problem processing your request. Please contact the site administrator.");
    		}
    	});
    });

    $('#btn_undo_branch_override').live('click', function() {
    	$.ajax({
    		type: "POST",
    		url: datactx + "/ajax/UndoBranchOverrideChangesAction.action",
    		success: function(data) {
    			// If number is zero, it means no data select, no need do anything
    			if(data.numSavedEntities != 0) {
    				alert(data.numSavedEntities + ' data have been restored.');

	    			resetAdminSearchPageNumber();
	    			$('#searchForm input[name="newSearch"]').val(true);
	    			doUserSearch();
    			}
    		},
    		error:function(){
    			alert("There was a problem processing your request. Please contact the site administrator.");
    		}
    	});
    });

    if($('#search-on-startup').length > 0) {
    	$('#allVendors_id .hasChildren').each(function(i, el) {
    		if ($(this).find('.children input:checked').size() > 0) {
    			$(this).find('> input[type="checkbox"]').attr('checked', true);
    			$(this).addClass('selected');
    			$(this).addClass("opened");
    		}
    	});

    	// hide filters
    	$('.filter .filter-header a').click();
    	$('.comfirm-filter p').text('Please wait, initializing your filters...');
    	updateVendorFilters(function() {
    		updateMarketFilters(function() {
    			// begin search
    			$('.filter-button .btn-search').click();
    		});
    	});
    }

    $('.activate-on-startup').each(function () {
    	$(this).click();
    });
});


/**
 * Applies the modification to the selected records.
 * @param recordId the specific record ID if doing inline edit
 */
function doApplyChangesToSelectedRecords(recordId) {
	var isCW = $('#searchForm input[name="whichType"]').val() == 'cwtype';

	if (recordId) {
		$('#searchForm').append('<input type="hidden" name="pricingRecordIds" value="' + recordId + '"/>');
	} else {
		if (isCW) {
			if ($('.table-sw-type-prices .result-table input[name="pricingRecordId"]:checked').size() < 1) {
				alert("Please select records to be affected.");
				return;
			}

			$('.table-sw-type-prices .result-table input[name="pricingRecordId"]:checked').each(function(index,element) {
				var value = $(this).val();
				if (value != '') {
					$('#searchForm').append('<input type="hidden" name="pricingRecordIds" value="' + value + '"/>');
				}
			});
		} else {
			if ($('.table-other-type .result-table input[name="pricingRecordId"]:checked').size() < 1) {
				alert("Please select records to be affected.");
				return;
			}
			$('.table-other-type .result-table input[name="pricingRecordId"]:checked').each(function(index,element) {
				var value = $(this).val();
				if (value != '') {
					$('#searchForm').append('<input type="hidden" name="pricingRecordIds" value="' + value + '"/>');
				}
			});
		}
	}

	$('#searchForm').ajaxSubmit({
		url : datactx + "/ajax/ApplyInputPriceChangesAction.action",
        success: function(data){
    		// update the UI
    		if (data.modifiedRecords && data.modifiedRecords.length > 0) {
    			$(data.modifiedRecords).each(function (index, element) {
    				var row;
    				if (isCW) {
    					row = $('#cw_price_record_' + element.pricingRecordId);
    				} else {
    					row = $('#price_record_' + element.pricingRecordId);
    				}
    				if (element.adjustedListPrice && element.adjustedListPrice != '') {
    					$(row).find('.adjusted-list-price-col .adjustment-text').text(element.adjustedListPrice || '');
    					$(row).find('.adjusted-list-price-col input[name="adjustmentValue"]').val(element.adjustedListPrice || '');
    					$(row).find('.adjusted-list-price-col input[name="adjustmentComment"]').val(element.adjustedListPriceComment || '');
    					$(row).find('.adjusted-list-price-col input[name="adjustmentEndDate"]').val(element.adjustedListPriceEndDate || '');
    					$(row).find('.adjusted-list-price-col input[name="adjustmentStartDate"]').val(element.adjustedListPriceStartDate || '');
    					$(row).find('.adjusted-list-price-col input[name="adjustmentCreatedBy"]').val(element.adjustedListPriceCreatedBy || '');
    				}

    				if (element.adjustedMultiplier && element.adjustedMultiplier != '') {
    					$(row).find('.adjusted-multiplier-col .adjustment-text').text(element.adjustedMultiplier || '');
    					$(row).find('.adjusted-multiplier-col input[name="adjustmentValue"]').val(element.adjustedMultiplier || '');
    					$(row).find('.adjusted-multiplier-col input[name="adjustmentComment"]').val(element.adjustedMultiplierComment || '');
    					$(row).find('.adjusted-multiplier-col input[name="adjustmentEndDate"]').val(element.adjustedMultiplierEndDate || '');
    					$(row).find('.adjusted-multiplier-col input[name="adjustmentStartDate"]').val(element.adjustedMultiplierStartDate || '');
    					$(row).find('.adjusted-multiplier-col input[name="adjustmentCreatedBy"]').val(element.adjustedMultiplierCreatedBy || '');
    				}
    				$(row).find('.new-net-price-col').text(element.adjustedNetPrice || '');
    			});
    		}

    		// close popup (inline mode)
            $('.tip-for-prices-click').hide();
            $('.tip-for-prices-click input').val('');
            $('.tip-for-prices-click textarea').val('');
        },
        error:function(){
        	alert("There was a problem processing your request. Please contact the site administrator.");
       	}
    });
}

/**
 * Applies the modification to the selected records.
 * @param recordId the specific record ID if doing inline edit
 */
function doApplyChangesToAdjustmentRecords(recordId) {
	var isCW = $('#searchForm input[name="whichType"]').val() == 'cwtype';

	if (recordId) {
		$('#searchForm').append('<input type="hidden" name="productIds" value="' + recordId + '"/>');
	} else {
		if (isCW) {
			if ($('.table-sw-type-adjustments .result-table input[name="productId"]:checked').size() < 1) {
				alert("Please select records to be affected.");
				return;
			}

			$('.table-sw-type-adjustments .result-table input[name="productId"]:checked').each(function(index,element) {
				var value = $(this).val();
				if (value != '') {
					$('#searchForm').append('<input type="hidden" name="productIds" value="' + value + '"/>');
				}
			});
		} else {
			if ($('.table-other-type-adjustments .result-table input[name="productId"]:checked').size() < 1) {
				alert("Please select records to be affected.");
				return;
			}
			$('.table-other-type-adjustments .result-table input[name="productId"]:checked').each(function(index,element) {
				var value = $(this).val();
				if (value != '') {
					$('#searchForm').append('<input type="hidden" name="productIds" value="' + value + '"/>');
				}
			});
		}
	}

	$('#searchForm').ajaxSubmit({
		url : datactx + "/ajax/ApplyAdjustmentChangesAction.action",
		success: function(data){
			// update the UI
			if (data.ajaxPricingSheets && data.ajaxPricingSheets.length > 0) {
				$(data.ajaxPricingSheets).each(function (index, element) {
					var row;
					if (isCW) {
						row = $('#cw_adjustment_record_' + element.pricingRecordId + '_' + element.branchId);
					} else {
						row = $('#adjustment_record_' + element.pricingRecordId + '_' + element.branchId);
					}
					$(row).find('.new-adjusment .adjustment-text').text(element.newPricingAdjustment || '');
					$(row).find('.new-adjusment input[name="adjustmentValue"]').val(element.newPricingAdjustment || '');
					$(row).find('.new-adjusment input[name="adjustmentEndDate"]').val(formatDateString(element.newPricingAdjustmentEndDate) || '');
					$(row).find('.new-adjusment input[name="adjustmentStartDate"]').val(formatDateString(element.newPricingAdjustmentStartDate) || '');
					$(row).find('.new-adjusment input[name="adjustmentComment"]').val(element.newPricingAdjustmentComment || '');
					$(row).find('.new-adjusment input[name="adjustmentCreatedBy"]').val(element.newPricingAdjustmentCreatedBy || '');
					$(row).find('#before-freight-price').text(element.beforeFreightPrice || '');
					$(row).find('#adjusted-freight-cost').text(element.adjustedFreightCost || '');
					$(row).find('#total-price').text(element.totalPrice || '');
					$(row).find('#branch-override').text(element.branchOverride || '');
					$(row).find('#final-price').text(element.finalPrice || '');
				});
			}

			// close popup (inline mode)
			$('.tip-for-prices-click').hide();
			$('.tip-for-prices-click input').val('');
			$('.tip-for-prices-click textarea').val('');
		},
		error:function(){
			alert("There was a problem processing your request. Please contact the site administrator.");
		}
	});
}

/**
 * Applies the modification to the selected records.
 * @param recordId the specific record ID if doing inline edit
 */
function doApplyChangesToFreightRecords(recordId) {
	var isCW = $('#searchForm input[name="whichType"]').val() == 'cwtype';

	if (recordId) {
		$('#searchForm').append('<input type="hidden" name="productIds" value="' + recordId + '"/>');
	} else {
		if (isCW) {
			if ($('.table-sw-type-freights .result-table input[name="productId"]:checked').size() < 1) {
				alert("Please select records to be affected.");
				return;
			}

			$('.table-sw-type-freights .result-table input[name="productId"]:checked').each(function(index,element) {
				var value = $(this).val();
				if (value != '') {
					$('#searchForm').append('<input type="hidden" name="productIds" value="' + value + '"/>');
				}
			});
		} else {
			if ($('.table-other-type-freights .result-table input[name="productId"]:checked').size() < 1) {
				alert("Please select records to be affected.");
				return;
			}
			$('.table-other-type-freights .result-table input[name="productId"]:checked').each(function(index,element) {
				var value = $(this).val();
				if (value != '') {
					$('#searchForm').append('<input type="hidden" name="productIds" value="' + value + '"/>');
				}
			});
		}
	}

	$('#searchForm').ajaxSubmit({
		url : datactx + "/ajax/ApplyFreightChangesAction.action",
		success: function(data){
			// update the UI
			if (data.ajaxPricingSheets && data.ajaxPricingSheets.length > 0) {
				$(data.ajaxPricingSheets).each(function (index, element) {
					var row;
					if (isCW) {
						row = $('#cw_freight_record_' + element.pricingRecordId + '_' + element.branchId);
					} else {
						row = $('#freight_record_' + element.pricingRecordId + '_' + element.branchId);
					}
					$(row).find('.new-freight .adjustment-text').text(element.newFreightCost || '');
					$(row).find('.new-freight input[name="adjustmentValue"]').val(element.newFreightCost || '');
					$(row).find('.new-freight input[name="adjustmentEndDate"]').val(formatDateString(element.newFreightCostEndDate) || '');
					$(row).find('.new-freight input[name="adjustmentStartDate"]').val(formatDateString(element.newFreightCostStartDate) || '');
					$(row).find('.new-freight input[name="adjustmentComment"]').val(element.newFreightCostComment || '');
					$(row).find('.new-freight input[name="adjustmentCreatedBy"]').val(element.newFreightCostCreatedBy || '');
					$(row).find('#total-price').text(element.totalPrice || '');
					$(row).find('#branch-override').text(element.branchOverride || '');
					$(row).find('#final-price').text(element.finalPrice || '');
				});
			}

			// close popup (inline mode)
			$('.tip-for-prices-click').hide();
			$('.tip-for-prices-click input').val('');
			$('.tip-for-prices-click textarea').val('');
		},
		error:function(){
			alert("There was a problem processing your request. Please contact the site administrator.");
		}
	});
}

/**
 * Applies the modification to the selected records.
 * @param recordId the specific record ID if doing inline edit
 */
function doApplyChangesToBranchOverrideRecords(recordId) {
	var isCW = $('#searchForm input[name="whichType"]').val() == 'cwtype';

	if (recordId) {
		$('#searchForm').append('<input type="hidden" name="productIds" value="' + recordId + '"/>');
	} else {
		if (isCW) {
			if ($('.table-sw-type-branch-overrides .result-table input[name="productId"]:checked').size() < 1) {
				alert("Please select records to be affected.");
				return;
			}

			$('.table-sw-type-branch-overrides .result-table input[name="productId"]:checked').each(function(index,element) {
				var value = $(this).val();
				if (value != '') {
					$('#searchForm').append('<input type="hidden" name="productIds" value="' + value + '"/>');
				}
			});
		} else {
			if ($('.table-other-type-branch-overrides .result-table input[name="productId"]:checked').size() < 1) {
				alert("Please select records to be affected.");
				return;
			}
			$('.table-other-type-branch-overrides .result-table input[name="productId"]:checked').each(function(index,element) {
				var value = $(this).val();
				if (value != '') {
					$('#searchForm').append('<input type="hidden" name="productIds" value="' + value + '"/>');
				}
			});
		}
	}

	$('#searchForm').ajaxSubmit({
		url : datactx + "/ajax/ApplyBranchOverrideChangesAction.action",
		success: function(data){
			// update the UI
			if (data.ajaxPricingSheets && data.ajaxPricingSheets.length > 0) {
				$(data.ajaxPricingSheets).each(function (index, element) {
					var row;
					if (isCW) {
						row = $('#cw_branch_override_record_' + element.pricingRecordId + '_' + element.branchId);
					} else {
						row = $('#branch_override_record_' + element.pricingRecordId + '_' + element.branchId);
					}
					$(row).find('.new-branch-overrides .adjustment-text').text(element.newBranchOverride || '');
					$(row).find('.new-branch-overrides input[name="adjustmentValue"]').val(element.newBranchOverride || '');
					$(row).find('.new-branch-overrides input[name="adjustmentEndDate"]').val(formatDateString(element.newBranchOverrideEndDate) || '');
					$(row).find('.new-branch-overrides input[name="adjustmentStartDate"]').val(formatDateString(element.newBranchOverrideStartDate) || '');
					$(row).find('.new-branch-overrides input[name="adjustmentComment"]').val(element.newBranchOverrideComment || '');
					$(row).find('.new-branch-overrides input[name="adjustmentCreatedBy"]').val(element.newBranchOverrideCreatedBy || '');
					$(row).find('#final-price').text(element.finalPrice || '');
				});
			}

			// close popup (inline mode)
			$('.tip-for-prices-click').hide();
			$('.tip-for-prices-click input').val('');
			$('.tip-for-prices-click textarea').val('');
		},
		error:function(){
			alert("There was a problem processing your request. Please contact the site administrator.");
		}
	});
}

/**
 * This function copies the selected filters into the search form, replacing the current hidden filters.
 * @since Pricing Calculation
 */
function copyFilters() {
	var el = $('.filter-button .btn-search').first();
    var isCW = false;
    $('#allTypes_id').find("label").each(function(index,element) {
        if ($(element).siblings().attr('checked')) {
        // check the cw is checked or not.
            if ($(element).text().indexOf(" CW") != -1) {
                isCW = true;
            }
            return;
        }
    });

    var pageNumber = $('#searchForm input[name="criteria.page"]').val();
    var pageSize = $('#searchForm input[name="criteria.pageSize"]').val();
    var sortColumn = $('#searchForm input[name="criteria.sortingColumn"]').val();
    var sortDir = $('#searchForm input[name="criteria.sortingType"]').val();
    var onlyLowestNetPrice = $('#searchForm input[name="onlyLowestNetPrice"]').val();

    $('#searchForm input[type="hidden"]').remove();

    $('#searchForm').append('<input type="hidden" name="newSearch" value="false"/>');
    $('#searchForm').append('<input type="hidden" name="onlyLowestNetPrice" value="' + onlyLowestNetPrice + '"/>');
    $('#searchForm').append('<input type="hidden" name="criteria.page" value="' + pageNumber + '"/>');
    $('#searchForm').append('<input type="hidden" name="criteria.pageSize" value="' + pageSize + '"/>');
	$('#searchForm').append('<input type="hidden" name="criteria.sortingColumn" value="' + sortColumn + '"/>');
	$('#searchForm').append('<input type="hidden" name="criteria.sortingType" value="' + sortDir + '"/>');


    var counter = 0;
    $('#allVendors_id').each(function(ind, ele){
    	$(ele).find('.children input:checked').each(function(j, shipPoint){
            var param = '<input type="hidden" name="userFilterRecord.shipPoints[' + counter + '].id" value="' + $(shipPoint).val() + '"/>';
            $('#searchForm').append(param);
    		counter++;
    	});
    });

    /*
    $('#allVendors_id .selected').each(function(ind, ele){
        var selected = $(ele).find('input').attr('checked');
        if (selected == 'checked')
        {
            var value = $(ele).find('label').attr('id');
            var param = '<input type="hidden" name="userFilterRecord.shipPoints[' + ind + '].id" value="' + value + '"/>';
            $('#searchForm').append(param);
        }
    });
    */

    $('#allTypes_id .selected').each(function(ind, ele){
        var selected = $(ele).find('input').attr('checked');
        if (selected == 'checked')
        {
            var value = $(ele).find('label').attr('id');
            var param = '<input type="hidden" name="userFilterRecord.productTypes[' + ind + '].id" value="' + value + '"/>';
            $('#searchForm').append(param);
        }
    });

    $('#allMarkets_id .selected').each(function(ind, ele){
        var selected = $(ele).find('input').attr('checked');
        if (selected == 'checked')
        {
            var value = $(ele).find('label').attr('id');
            var param = '<input type="hidden" name="userFilterRecord.markets[' + ind + '].id" value="' + value + '"/>';
            $('#searchForm').append(param);
        }
    });

    $('#allBranches_id .selected').each(function(ind, ele){
        var selected = $(ele).find('input').attr('checked');
        if (selected == 'checked')
        {
            var value = $(ele).find('label').attr('id');
            var param = '<input type="hidden" name="userFilterRecord.branches[' + ind + '].id" value="' + value + '"/>';
            $('#searchForm').append(param);
        }
    });

    $('#allCategories_id .selected').each(function(ind, ele){
        var selected = $(ele).find('input').attr('checked');
        if (selected == 'checked')
        {
            var value = $(ele).find('label').attr('id');
            var param = '<input type="hidden" name="userFilterRecord.categories[' + ind + '].id" value="' + value + '"/>';
            $('#searchForm').append(param);
        }
    });

    $('#allProducts_id .selected').each(function(ind, ele){
        var selected = $(ele).find('input').attr('checked');
        if (selected == 'checked')
        {
            var value = $(ele).find('label').attr('id');
            var param = '<input type="hidden" name="userFilterRecord.products[' + ind + '].id" value="' + value + '"/>';
            $('#searchForm').append(param);
        }
    });

    if (isCW) {
        $('#searchForm').append('<input type="hidden" id="whichType" name="whichType" value="cwtype" />');
    } else {
        $('#searchForm').append('<input type="hidden" id="whichType" name="whichType" value="othertype" />');
    }
}

/**
 * This method returns the appropriate renderer for the admin search result paginated table.
 */
function getAdminResultTableRenderer() {
	if ($('#resultEntityType').val() == 'Vendor') {
		return (function(json) {
			var strData = '';
            $.each(json.result.values,function(idx,item){
                strData += '<tr>';
                strData += '<td>';
                strData += '<input type="checkbox" name="deleteIds" value="' + item.id + '" />';
                strData += '</td>';
                strData += '<td class="vendorNumber">';
                strData += escapeText(item.vendorNumber);
                strData += '</td>';
                strData += '<td class="vendorName">';
                strData += '<a href="javascript:;" class="link-edit-vendor">';
                strData += escapeText(item.name);
                strData += '</a>';
                strData += '</td>';
                strData += '<td class="vendorAddress">';
                strData += escapeText(item.address1 + ' ' + item.address2 + ', ' + item.city + ', ' + item.state.name + ' ' + item.zipCode);
                strData += '</td>';
                strData += '<td class="vendorPhone">';
                strData += escapeText(item.phoneNumber1 + ' / ' + item.phoneNumber2);
                strData += '</td>';
                strData += '<td class="vendorContact1">';
                strData += escapeText(item.contact1Name + ' / ' + item.contact1EmailAddress);
                strData += '</td>';
                strData += '<td class="vendorContact2">';
                strData += escapeText(item.contact2Name + ' / ' + item.contact2EmailAddress);
                strData += '</td>';
                strData += '<td>';
                strData += '<a href="'+ datactx + '/ShipPointSearch.action?criteria.vendorId=' + item.id + '">';
                strData += 'View (' + item.shipPoints.length + ')';
                strData += '</a>';
                strData += '</td>';
                strData += '</tr>';
                $("#searchForm table.data tbody").empty().append(strData);
            });

            updateAdminSearchPaginationRendering(json);
            updateAdminSearchSortingHeader(json);
		});
	} else if ($('#resultEntityType').val() == 'Category') {
		return (function(json) {
			var strData = '';
            $.each(json.result.values,function(idx,item){
                strData += '<tr>';
                strData += '<td>';
                strData += '<input type="checkbox" name="deleteIds" value="' + item.id + '" />';
                strData += '</td>';
				strData += '<td class="categName">';
				strData += '<a href="javascript:;" class="link-edit-category">';
				strData += escapeText(item.name);
				strData += '</a>';
				strData += '</td>';
				strData += '<td class="long-text categDesc">';
				strData += escapeText(item.description);
				strData += '</td>';
                strData += '</tr>';
                $("#searchForm table.data tbody").empty().append(strData);
            });
            updateAdminSearchPaginationRendering(json);
            updateAdminSearchSortingHeader(json);
		});
	} else if ($('#resultEntityType').val() == 'ShipPoint') {
		return (function(json) {
			var strData = '';
			$.each(json.result.values,function(idx,item){
				strData += '<tr>';
				strData += '<td>';
				strData += '<input type="checkbox" name="deleteIds" value="' + item.id + '" />';
				strData += '</td>';
                strData += '<td class="spName">';
                strData += '<a href="javascript:;" class="link-edit-shipping-point">';
                strData += escapeText(item.name);
                strData += '</a>';
                strData += '</td>';
                strData += '<td class="spDesc">';
                strData += escapeText(item.description);
                strData += '</td>';
                strData += '<td class="spZip">';
                strData += escapeText(item.zipCode);
                strData += '</td>';
                strData += '<td class="spContact1">';
                strData += escapeText(item.phoneNumber1);
                strData += '</td>';
                strData += '<td class="spContact1Email">';
                strData += escapeText(item.contact1EmailAddress);
                strData += '</td>';
                strData += '<td class="spContact2">';
                strData += escapeText(item.phoneNumber2);
                strData += '</td>';
                strData += '<td class="spContact2Email">';
                strData += escapeText(item.contact2EmailAddress);
                strData += '</td>';
				strData += '</tr>';
				$("#searchForm table.data tbody").empty().append(strData);
			});
			updateAdminSearchPaginationRendering(json);
			updateAdminSearchSortingHeader(json);
		});
	} else if ($('#resultEntityType').val() == 'Product') {
		return (function(json) {
			var strData = '';
            $.each(json.result.values,function(idx,item){
				strData += '<tr>';
				strData += '<td>';
                strData += '<input type="checkbox" name="deleteIds" value="' + item.id + '" />';
				strData += '</td>';
				strData += '<td class="productCode">';
				strData += '<a href="javascript:;" class="link-edit-product">';
				strData += escapeText(item.productCode);
				strData += '</a>';
				strData += '</td>';
				strData += '<td class="productAlt">';
				strData += '<a href="javascript:;" class="link-edit-product">';
				strData += escapeText(item.alternateCode1);
				strData += '</a>';
				strData += '</td>';
				strData += '<td class="productDescr">';
				strData += escapeText(item.description);
				strData += '</td>';
				strData += '<td class="productWeight">';
				strData += escapeText(item.hundredWeight);
				strData += '</td>';
				strData += '<td class="productListPrice">';
				strData += escapeText(item.listPrice);
				strData += '</td>';
				strData += '</tr>';
            });
            if (strData != '') {
            	$("#searchForm table.data tbody").empty().append(strData);
            }
            updateAdminSearchPaginationRendering(json);
            updateAdminSearchSortingHeader(json);
		});
	} else if ($('#resultEntityType').val() == 'Market') {
		return (function(json) {
			var strData = '';
			$.each(json.result.values,function(idx,item){
				strData += '<tr>';
				strData += '<td>';
				strData += '<input type="checkbox" name="deleteIds" value="' + item.id + '" />';
				strData += '</td>';
				strData += '<td class="marketName">';
				strData += '<a href="javascript:;" class="link-edit-market">';
				strData += escapeText(item.name);
				strData += '</a>';
				strData += '</td>';
				strData += '<td class="long-text marketDesc">';
				strData += escapeText(item.description);
				strData += '</td>';
				strData += '</tr>';
			});
			if (strData != '') {
				$("#searchForm table.data tbody").empty().append(strData);
			}
			updateAdminSearchPaginationRendering(json);
			updateAdminSearchSortingHeader(json);
		});
	} else if ($('#resultEntityType').val() == 'Branch') {
		return (function(json) {
			var strData = '';
			$.each(json.result.values,function(idx,item){
				strData += '<tr>';
				strData += '<td>';
				strData += '<input type="checkbox" name="deleteIds" value="' + item.id + '" />';
				strData += '</td>';
				strData += '<td class="branchNumber">';
				strData += '<a href="javascript:;" class="link-edit-branch">';
				strData += escapeText(item.branchNumber);
				strData += '</a>';
				strData += '</td>';
				strData += '<td class="branchLogonName">';
				strData += escapeText(item.triLogonName);
				strData += '</td>';
				strData += '<td class="branchMachineName">';
				strData += escapeText(item.machineName);
				strData += '</td>';
				strData += '<td class="branchAddress">';
                strData += escapeText(item.address1 + ' ' + item.address2 + ', ' + item.city + ', ' + item.state.name + ' ' + item.zipCode);
				strData += '</td>';
				strData += '<td class="branchProfitCenter">';
				strData += escapeText(item.profitCenter);
				strData += '</td>';
				strData += '<td class="branchPhone">';
				strData += escapeText(item.phoneNumber);
				strData += '</td>';
				strData += '<td class="branchRegion">';
				strData += escapeText(item.region.name);
				strData += '</td>';
				strData += '<td class="branchDistrict">';
				strData += escapeText(item.district.name);
				strData += '</td>';
				strData += '<td class="branchManager">';
				strData += escapeText(item.generalManagerName);
				strData += '</td>';
				strData += '<td class="branchPriceColumn">';
				strData += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
				strData += '</td>';
				strData += '</tr>';
			});
			if (strData != '') {
				$("#searchForm table.data tbody").empty().append(strData);
			}
			updateAdminSearchPaginationRendering(json);
			updateAdminSearchSortingHeader(json);
		});
	} else if ($('#resultEntityType').val() == 'User') {
		return (function(json) {
			var strData = '';
            $.each(json.result.values,function(idx,item){
                strData += '<tr>';
				strData += '<td>';
                strData += '<input type="checkbox" name="deleteIds" value="' + item.id + '" />';
				strData += '</td>';
				strData += '<td>';
				strData += '<a href="javascript:;" class="link-edit-user">';
				strData += item.firstName + ' ' + item.lastName;
				strData += '</a>';
				strData += '</td>';
				strData += '<td>';
				strData += item.title;
				strData += '</td>';
				strData += '<td>';
				strData += item.phone + ' / ' + item.mobile;
				strData += '</td>';
				strData += '<td>';
				strData += item.fax + ' / ' + item.email;
				strData += '</td>';
				strData += '<td>';
				strData += '<a href="javascript:;" class="link-add-roles">';
				var roles = [];
				$.each(item.roles, function(idx, role) {
					roles.push(role.name);
				});
				strData += roles.join(', ');
				strData += '</a>';
				strData += '</td>';
				strData += '<td>';
				strData += '<a href="javascript:;" class="link-add-branches">';
				var branches = [];
				$.each(item.branches, function(idx, branch) {
					branches.push(branch.triLogonName);
				});
				strData += branches.join(', ');
				strData += '</a>';
				strData += '</td>';
				strData += '</tr>';
            });
            $("#searchForm table.data tbody").empty().append(strData);
            updateAdminSearchPaginationRendering(json);
            updateAdminSearchSortingHeader(json);
		});
	} else if ($('#resultEntityType').val() == 'Destination') {
		return (function(json) {
			var strData = '';
            $.each(json.result.values,function(idx,item){
            	strData += '<tr>';
				strData += '<td>';
                strData += '<input type="checkbox" name="deleteIds" value="' + item.id + '" />';
				strData += '</td>';
				strData += '<td>';
				strData += item.br;
				strData += '</td>';
				strData += '<td>';
				strData += '<a href="javascript:;">';
				strData += item.brLocation;
				strData += '</a>';
				strData += '</td>';
				strData += '<td>';
				strData += item.state.name;
				strData += '</td>';
				strData += '<td>';
				strData += item.zipCode;
				strData += '</td>';
				strData += '</tr>';
            });
            $("#searchForm table.data tbody").empty().append(strData);
            updateAdminSearchPaginationRendering(json);
            updateAdminSearchSortingHeader(json);
		});
	} else if ($('#resultEntityType').val() == 'Origination') {
		return (function(json) {
			var strData = '';
            $.each(json.result.values,function(idx,item){
            	strData += '<tr>';
				strData += '<td>';
                strData += '<input type="checkbox" name="deleteIds" value="' + item.id + '" />';
				strData += '</td>';
				strData += '<td>';
				strData += '<a href="javascript:;">';
				strData += item.name;
				strData += '</a>';
				strData += '</td>';
				strData += '<td>';
				strData += item.state.name;
				strData += '</td>';
				strData += '<td>';
				strData += item.zipCode;
				strData += '</td>';
				strData += '</tr>';
            });
            $("#searchForm table.data tbody").empty().append(strData);
            updateAdminSearchPaginationRendering(json);
            updateAdminSearchSortingHeader(json);
		});
	} else if ($('#resultEntityType').val() == 'OriginationToStateFreightRate') {
		return (function(json) {
			var strData = '', strData1 = '', headerData='', colGroupData = '';
			if(json.result.length) {
				$.each(json.result[0], function(idx, item) {
					var data = ' data-id="' + item.origination.id + '" data-name="' + item.origination.name
					+ '" data-state="' + item.origination.state.name + '" data-type="' + item.origination.type.name
					+ '" data-zipCode="' + item.origination.zipCode + '"';
					headerData += ((json.result[0].length == idx + 1) ? '<th class="last" ' + data + '>' : '<th' + data + '>');
					headerData += '<a href="javascript:;">' + item.origination.name + '</a></th>';
					colGroupData += '<col width="92" />';
				});
			}
			$.each(json.result,function(idx,items){
				strData += '<tr>';
				strData += '<td>';
				strData += '<strong>';
				strData += items[0].state.name;
				strData += '</strong>';
				strData += '<a href="javascript:;">';
				strData += json.allDestinationsByState[items[0].state.id] ? json.allDestinationsByState[items[0].state.id] : 0;
				strData += ' destinations</a>';
				strData += '</td>';
				strData += '</tr>';
			});
			$.each(json.result,function(idx,items){
				strData1 += '<tr>';
				$.each(items, function(idx1, item) {
					strData1 += '<td data-id="' + item.id + '" data-rate="' + item.freightRate
					+ '" data-state="' + item.state.id + '" data-origination="' + item.origination.id + '" data-fsc="' + item.fsc + '">';
					strData1 += item.freightRate + '/' + (item.freightRate + item.fsc);
					strData1 += '</td>';
				});
				strData1 += '</tr>';
			});
			$("#state-table tbody").empty().append(strData);
			$("#state-rate-table thead tr").empty().append(headerData);
			$("#state-rate-table colgroup").empty().append(colGroupData);
			$("#state-rate-table tbody").empty().append(strData1);
            updateAdminSearchPaginationRendering(json);
            updateAdminSearchSortingHeader(json);
		});
	} else if ($('#resultEntityType').val() == 'OriginationToDestinationFreightRate') {
		return (function(json) {
			var strData = '', strData1 = '', strData2 = '', headerData='', colGroupData = '';
			if(json.result.length) {
				$.each(json.result[0], function(idx, item) {
					var data = ' data-id="' + item.origination.id + '" data-name="' + item.origination.name
					+ '" data-state="' + item.origination.state.name + '" data-type="' + item.origination.type.name
					+ '" data-zipCode="' + item.origination.zipCode + '"';
					headerData += ((json.result[0].length == idx + 1) ? '<th class="last" ' + data + '>' : '<th' + data + '>');
					headerData += '<a href="javascript:;">' + item.origination.name + '</a></th>';
					colGroupData += '<col width="92" />';
				});
			}
			$.each(json.result,function(idx,items){
				strData += '<tr>';
				strData += '<td>';
				strData += '<a href="javascript:;">';
				strData += items[0].destination.brLocation;
				strData += '</a>';
				strData += '</td>';
				strData += '</tr>';
			});
			$.each(json.result,function(idx,items){
				strData1 += '<tr>';
				strData2 += '<tr>';
				$.each(items, function(idx1, item) {
					strData1 += '<td data-id="' + item.id + '" data-freightRate="' + item.freightRate
						+ '" data-rate="' + item.rate + '" data-destination="' + item.destination.id
						+ '" data-origination="' + item.origination.id + '" data-mileage="' + item.mileage + '"  data-fsc="' + item.fsc + '">$';
					strData1 += item.freightRate;
					strData1 += '</td>';
					strData2 += '<td data-id="' + item.id + '" data-freightRate="' + item.freightRate
						+ '" data-rate="' + item.rate + '" data-destination="' + item.destination.id
						+ '" data-origination="' + item.origination.id + '" data-mileage="' + item.mileage + '"  data-fsc="' + item.fsc + '">';
					strData2 += item.mileage;
					strData2 += '</td>';
				});
				strData1 += '</tr>';
				strData2 += '</tr>';
			});
			$("#destination-dollar-table tbody").empty().append(strData);
			$("#zip-dollar-table thead tr").empty().append(headerData);
			$("#zip-dollar-table colgroup").empty().append(colGroupData);
			$("#zip-dollar-table tbody").empty().append(strData1);

			$("#destination-mileage-table tbody").empty().append(strData);
			$("#zip-mileage-table thead tr").empty().append(headerData);
			$("#zip-mileage-table colgroup").empty().append(colGroupData);
			$("#zip-mileage-table tbody").empty().append(strData2);

            updateAdminSearchPaginationRendering(json);
            updateAdminSearchSortingHeader(json);
		});
	}
}

/**
 * Updates the rendered table sort header.
 */
function updateAdminSearchSortingHeader(json) {
	if(!json.criteria) {
		return;
	}
	var sortColumn = json.criteria.sortingColumn;
	var sortDir = json.criteria.sortingType;

	$('#searchForm .sortable-header a').removeClass('current');
	if (sortDir == 'ASC') {
		$('#searchForm .sortable-header a[rel="'+ sortColumn + '"]:eq(0)').addClass("current");
	} else {
		$('#searchForm .sortable-header a[rel="'+ sortColumn + '"]:eq(1)').addClass("current");
	}
}

/**
 * Updates the rendered table sort header.
 */
function updateSearchSortingHeader(container, criteria) {
	if(!criteria) {
		return;
	}
	var sortColumn = criteria.sortingColumn;
	var sortDir = criteria.sortingType;

	$(container).find('.sortable-header a').removeClass('current');
	$(container).find('.sortable-header').closest('th').removeClass('sortedUp');
	$(container).find('.sortable-header').closest('th').removeClass('sortedDown');

	if (sortDir == 'ASC') {
		$(container).find('.sortable-header a[rel="'+ sortColumn + '"]:eq(0)').addClass("current");
		$(container).find('.sortable-header a[rel="'+ sortColumn + '"]:eq(0)').closest('th').addClass("sortedUp");
	} else {
		$(container).find('.sortable-header a[rel="'+ sortColumn + '"]:eq(1)').addClass("current");
		$(container).find('.sortable-header a[rel="'+ sortColumn + '"]:eq(1)').closest('th').addClass("sortedDown");
	}

	var tbl = $(container).find('.sortable-header a[rel="'+ sortColumn + '"]:eq(0)').closest('table');
	$(container).find('table.data tbody tr td.sorted').removeClass('sorted');
	var sortTH = $(container).find('.sortable-header a[rel="'+ sortColumn + '"]:eq(1)').closest('th');
	var colIndex = $(sortTH).closest('tr').find('th').index($(sortTH));
	var trs = $(tbl).find('tbody tr');
	trs.each(function(){
		var td = $(this).find('td:eq('+colIndex+')');
		td.addClass('sorted');
	});
}

/**
 * Updates the rendered table sort header.
 */
function updateUserSearchSortingHeader() {
	var sortColumn = $('#ajax-fragment-container .current-sort-column').text();
	var sortDir = $('#ajax-fragment-container .current-sort-direction').text();

	$('.sortable-header a').removeClass('current');
	$('.sortable-header').closest('th').removeClass('sortedUp');
	$('.sortable-header').closest('th').removeClass('sortedDown');

	var tbl = $('.sortable-header a[rel="'+ sortColumn + '"]:eq(0)').closest('table');
	if (sortDir == 'ASC') {
		$('.sortable-header a[rel="'+ sortColumn + '"]:eq(0)').addClass("current");
		$('.sortable-header a[rel="'+ sortColumn + '"]:eq(0)').closest('th').addClass("sortedUp");
	} else {
		$('.sortable-header a[rel="'+ sortColumn + '"]:eq(1)').addClass("current");
		$('.sortable-header a[rel="'+ sortColumn + '"]:eq(1)').closest('th').addClass("sortedDown");
	}

	$('.result-table table tbody tr td.sorted').removeClass('sorted');
	var sortTH = $('.sortable-header a[rel="'+ sortColumn + '"]:eq(1)').closest('th');
	var colIndex = $(sortTH).closest('tr').find('th').index($(sortTH));
	var trs = $(tbl).find('tbody tr');
	trs.each(function(){
		var td = $(this).find('td:eq('+colIndex+')');
		td.addClass('sorted');
	});
}

/**
 * Updates the page display based on the paginated results.
 */
function updateAdminSearchPaginationRendering(json) {
	if ($('#resultEntityType').val() == 'OriginationToStateFreightRate') {
		var numResultsInPage = json.result.length;
	    var numTotalMatches = json.total;
	    var pageNumber = json.page;
	    var pageSize = json.pageSize;
    } else if ($('#resultEntityType').val() == 'OriginationToDestinationFreightRate') {
    	var numResultsInPage = json.result.length;
	    var numTotalMatches = json.total;
	    var pageNumber = json.page;
	    var pageSize = json.pageSize;
    } else {
        var numResultsInPage = json.result.values.length;
        var numTotalMatches = json.result.total;
        var pageNumber = json.result.page;
        var pageSize = json.criteria.pageSize;
    }
    if (pageSize == 0) {
    	pageSize = numResultsInPage;
    }

    // calculate display index (display 0 - 0 of 0)
    var startIndex = 0;
    var endIndex = 0;


    // hide links
	$('.pageLinks a').hide();
	$('.page-jump').hide();

    if (numTotalMatches > 0) {
    	$('.per-page-show .last select').prop('disabled', false);
    	$('.page-trun').show();

    	startIndex = (pageNumber - 1) * pageSize + 1; // index starts with 1
    	endIndex = startIndex + numResultsInPage - 1;

        // now find the page link boundaries (Prev , 1, 2, 3, 4, 5, Next)
        var lowerBound = pageNumber - ((pageNumber + (MAX_PAGE_LINKS - 1)) % MAX_PAGE_LINKS);;
        var	totalPages = Math.ceil(numTotalMatches / pageSize);
        var upperBound = Math.min(lowerBound + (MAX_PAGE_LINKS - 1), totalPages);

        // render the paging links
        $('.page-jump em').text(totalPages);
        $('.page-jump').show();  // jump to [] of X

        if (pageNumber > 1) {
        	// previous/first
            $('.pageLinks a.first-link').unbind('click').bind('click', function() {
            	$('#searchForm input[name="criteria.page"]').val(1);
            	$('#searchForm input[name="page"]').val(1);
            	doAdminSearch();
            });

            $('.pageLinks a.prev-link').unbind('click').bind('click', function() {
            	$('#searchForm input[name="criteria.page"]').val(pageNumber - 1);
            	$('#searchForm input[name="page"]').val(pageNumber - 1);
            	doAdminSearch();
            });

        	$('.pageLinks .first-link').show();
        	$('.pageLinks .prev-link').show();
        }

        var linkIndex = 0;
        // specific pages
        $('.pageLinks a.page').removeClass('current');
        for (var i = lowerBound; i <= upperBound; i++) {
        	$('.pageLinks a.page:eq('+ linkIndex + ')').text(i).show();
        	if (i == pageNumber) {
        		$('.pageLinks a.page:eq('+ linkIndex + ')').addClass('current');
        	}
        	linkIndex ++;
        }

        if (totalPages > pageNumber) {
            $('.pageLinks a.last-link').unbind('click').bind('click', function() {
            	$('#searchForm input[name="criteria.page"]').val(totalPages);
            	$('#searchForm input[name="page"]').val(totalPages);
            	doAdminSearch();
            });

            $('.pageLinks a.next-link').unbind('click').bind('click', function() {
            	$('#searchForm input[name="criteria.page"]').val(pageNumber + 1);
            	$('#searchForm input[name="page"]').val(pageNumber + 1);
            	doAdminSearch();
            });

        	// next/last
        	$('.pageLinks .next-link').show();
        	$('.pageLinks .last-link').show();
        }
    }

    ajaxTableLoader = setTimeout(function(){
        $('.loading').hide();
        if(numResultsInPage > 0){
        	if ($('#resultEntityType').val() == 'OriginationToStateFreightRate'
        		|| $('#resultEntityType').val() == 'OriginationToDestinationFreightRate') {
        		$('.state-rates .table-wrapper').show();
				$("#state-table tbody tr:last").addClass('bottom');
        		$('.zip-rates .table-wrapper-zip').show();
				$("#destination-dollar-table tbody tr:last").addClass('bottom');
            } else {
                $('#searchForm table.data').show();
            }
        } else {
        	if ($('#resultEntityType').val() == 'OriginationToStateFreightRate'
        		|| $('#resultEntityType').val() == 'OriginationToDestinationFreightRate') {
        		$('.table-wrapper tbody').empty(); // clear previous results
            } else {
            	$("#searchForm table.data tbody").empty(); // clear previous results
            }
        	$('.no-data').text('No data available');
            $('.no-data').show();
        }

        var singular = numResultsInPage == 1;
        var entityName = singular ? "Vendor" : "Vendors";
        if ($('#resultEntityType').val() == 'Category') {
        	entityName = singular ? "Category" : "Categories";
        } else if ($('#resultEntityType').val() == 'ShipPoint') {
        	entityName = singular ? "Shipping point" : "Shipping points";
        } else if ($('#resultEntityType').val() == 'Product') {
        	entityName = singular ? "Product" : "Products";
        } else if ($('#resultEntityType').val() == 'Market') {
        	entityName = singular ? "Market" : "Markets";
        } else if ($('#resultEntityType').val() == 'Branch') {
        	entityName = singular ? "Branch" : "Branches";
        } else if ($('#resultEntityType').val() == 'User') {
        	entityName = singular ? "User" : "Users";
        } else if ($('#resultEntityType').val() == 'Origination') {
        	entityName = singular ? "Origin" : "Origins";
        } else if ($('#resultEntityType').val() == 'Destination') {
        	entityName = singular ? "Destination" : "Destinations";
        } else if ($('#resultEntityType').val() == 'OriginationToStateFreightRate') {
        	entityName = singular ? "State" : "States";
        } else if ($('#resultEntityType').val() == 'OriginationToDestinationFreightRate') {
        	entityName = singular ? "Destination" : "Destinations";
        }
        $('.total-data').html('Displaying ' + startIndex + ' to '+ endIndex +' of ' + numTotalMatches + ' ' + entityName);
        stripeTable();
    },100);
}

/**
 * Sends the admin search context back to the first page.
 */
function resetAdminSearchPageNumber() {
	$('#searchForm input[name="criteria.page"]').val(1);
	$('#searchForm input[name="page"]').val(1);
}

/**
 * Performs admin searching.
 */
function doAdminSearch() {
	var responseRenderer = getAdminResultTableRenderer();
	$('.no-data').hide();
	$('#searchForm table.data').hide();
	$('#searchForm .loading').show();
	$('.state-rates .table-wrapper').hide();
	$('.state-rates .loading').show();
	$('.zip-rates .table-wrapper-zip').hide();
	$('.zip-rates .loading').show();
	$('#searchForm input[name="pageSize"]').val($('.state-rates select.pageSize, .zip-rates select.pageSize').first().val());
	$('#searchForm input[name="criteria.pageSize"]').val($('#searchForm select.pageSize').first().val());

	$('#searchForm').ajaxSubmit({
        success: function(json){
        	$("#searchForm table.data th input:checked").removeAttr('checked');
        	responseRenderer(json);
        },
        error:function(){
        	alert("There was a problem processing your request. Please contact the site administrator.");
       	}
    });
}

/**
 * Adds striped display to admin search tables.
 */
function stripeTable(){
    $('.data').each(function(){
        $(this).find('tbody tr:odd').addClass('odd');
    });

    $('.column table').each(function(){
        $(this).find('tbody tr:odd').addClass('odd');
    });

    $('.table-data table').each(function(){
        $(this).find('tbody tr:odd').addClass('odd');
    });
}

/**
 * Escapes the given text so it can be appended to the DOM without issues.
 * @param txt the text to be escaped
 */
function escapeText(txt) {
	if (txt == null || typeof(txt) == 'undefined') {
		return "";
	}
	return $('#escapeelement').text(txt).html();
}

/**
 * Called when vendor selection is changed.
 */
function updateVendorFilters(callback) {
    $('#FilterDependencyForm input[type="hidden"]').remove();

    // collect all currently selected product types
    var currentSelections = {};
    $('#allTypes_id .selected').each(function(ind, ele) {
        var selected = $(ele).find('input').attr('checked');
        if (selected == 'checked') {
            currentSelections[$(ele).find('label').attr('id')] = true;
        }
    });


    // setup form with the vendor selections
    var counter = 0;
    $('#allVendors_id').each(function(ind, ele){
    	$(ele).find('.children input:checked').each(function(j, shipPoint){
    		var param = '<input type="hidden" name="vendors[' + counter + '].id" value="' + $(shipPoint).val() + '"/>';
    		$('#FilterDependencyForm').append(param);
    		counter++;
    	});
    });

    var data = $('#FilterDependencyForm').serialize();

    // request for filtered list of product types to be shown
    $.ajax({
        type: "POST",
        url: datactx + "/ajax/SelectVendors.action",
        dataType: "json",
        data : data,
        success: function(json) {
            $("#allTypes_id ul").empty();

            // render new selections
            $.each(json.productTypes, function(idx,item) {
            	var isChecked = '';
            	var cssClass = '';
            	if (currentSelections[item.id] == 1) {
            		isChecked = ' checked="checked" ';
            		cssClass = 'selected';
            	}
                var strData= "";
                strData += '<li class="' + cssClass + '">';
                strData += '<input type="checkbox" ' + isChecked + 'class="checkbox" value="'+ item.id +'">';
                strData += '<label id="'+ item.id + '" >';
                strData += item.name;
                strData += '</label>';
                strData += '<div class="clear"></div>';
                strData += '</li>';
                $("#allTypes_id ul").append(strData);
            });

            runBrowserHacks();

            // using the new product types, update the categories
            updateProductTypeFilters(callback);
        },
        error:function(){
        	alert("There was a problem processing your request. Please contact the site administrator.");
        }
    });
}

/**
 * Called when type selection is changed.
 */
function updateProductTypeFilters(callback) {
    $('#FilterDependencyForm input[type="hidden"]').remove();

    // collect all currently selected product types
    var currentSelections = {};
    $('#allCategories_id .selected').each(function(ind, ele) {
        var selected = $(ele).find('input').attr('checked');
        if (selected == 'checked') {
            currentSelections[$(ele).find('label').attr('id')] = true;
        }
    });

    // setup form with the vendor selections
    var counter = 0;
    $('#allVendors_id').each(function(ind, ele){
    	$(ele).find('.children input:checked').each(function(j, shipPoint){
    		var param = '<input type="hidden" name="vendors[' + counter + '].id" value="' + $(shipPoint).val() + '"/>';
    		$('#FilterDependencyForm').append(param);
    		counter++;
    	});
    });

    // setup form with the type selections
    $('#allTypes_id .selected').each(function(ind, ele){
        var selected = $(ele).find('input').attr('checked');
        if (selected == 'checked') {
            var value = $(ele).find('label').attr('id');
            var param = '<input type="hidden" name="productTypes[' + ind + '].id" value="' + value + '"/>';
            $('#FilterDependencyForm').append(param);
        }
    });

    var data = $('#FilterDependencyForm').serialize();

    // request for filtered list of categories to be shown
    $.ajax({
        type: "POST",
        url: datactx + "/ajax/SelectTypes.action",
        dataType: "json",
        data : data,
        success: function(json) {
            $("#allCategories_id ul").empty();

            // render new selections
            $.each(json.categories, function(idx,item) {
            	var isChecked = '';
            	var cssClass = '';
            	if (currentSelections[item.id] == 1) {
            		isChecked = ' checked="checked" ';
            		cssClass = 'selected';
            	}
                var strData= "";
                strData += '<li class="' + cssClass + '">';
                strData += '<input type="checkbox" ' + isChecked + 'class="checkbox" value="'+ item.id +'">';
                strData += '<label id="'+ item.id + '" >';
                strData += item.name;
                strData += '</label>';
                strData += '<div class="clear"></div>'
                strData += '</li>';
                $("#allCategories_id ul").append(strData);
            });

            runBrowserHacks();
            // update the alt codes
            updateCategoryFilters(callback);
        },
        error:function(){
        	alert("There was a problem processing your request. Please contact the site administrator.");
        }
    });
}

/**
 * Called when category selection is changed.
 */
function updateCategoryFilters(callback) {
	$('#FilterDependencyForm input[type="hidden"]').remove();

	// collect all currently selected product types
	var currentSelections = {};
	$('#allProducts_id .selected').each(function(ind, ele) {
		var selected = $(ele).find('input').attr('checked');
		if (selected == 'checked') {
			currentSelections[$(ele).find('label').attr('id')] = true;
		}
	});

	// setup form with the vendor selections
    var counter = 0;
    $('#allVendors_id').each(function(ind, ele){
    	$(ele).find('.children input:checked').each(function(j, shipPoint){
    		var param = '<input type="hidden" name="vendors[' + counter + '].id" value="' + $(shipPoint).val() + '"/>';
    		$('#FilterDependencyForm').append(param);
    		counter++;
    	});
    });

	// setup form with the type selections
	$('#allTypes_id .selected').each(function(ind, ele){
		var selected = $(ele).find('input').attr('checked');
		if (selected == 'checked') {
			var value = $(ele).find('label').attr('id');
			var param = '<input type="hidden" name="productTypes[' + ind + '].id" value="' + value + '"/>';
			$('#FilterDependencyForm').append(param);
		}
	});

	// setup form with the category selections
	$('#allCategories_id .selected').each(function(ind, ele){
		var selected = $(ele).find('input').attr('checked');
		if (selected == 'checked') {
			var value = $(ele).find('label').attr('id');
			var param = '<input type="hidden" name="categories[' + ind + '].id" value="' + value + '"/>';
			$('#FilterDependencyForm').append(param);
		}
	});

	var data = $('#FilterDependencyForm').serialize();

	// request for filtered list of products to be shown
	$.ajax({
        type: "POST",
		url: datactx + "/ajax/SelectCategories.action",
		dataType: "json",
		data : data,
		success: function(json) {
			$("#allProducts_id ul").empty();

			// render new selections
			$.each(json.products, function(idx,item) {
				var isChecked = '';
				var cssClass = '';
				if (currentSelections[item.id] == 1) {
					isChecked = ' checked="checked" ';
					cssClass = 'selected';
				}
				var strData= "";
				strData += '<li class="' + cssClass + '">';
				strData += '<input type="checkbox" ' + isChecked + 'class="checkbox" value="'+ item.id +'">';
				strData += '<label id="'+ item.id + '" >';
				strData += item.alternateCode1;
                strData += '</label>';
                strData += '<div class="clear"></div>'
				strData += '</li>';
				$("#allProducts_id ul").append(strData);
			});

            runBrowserHacks();
            if (callback) {
            	callback();
            }
		},
		error:function(){
			alert("There was a problem processing your request. Please contact the site administrator.");
		}
	});
}

/**
 * Called when market selection is changed.
 */
function updateMarketFilters(callback) {
    $('#FilterDependencyForm input[type="hidden"]').remove();

    // collect all currently selected product types
    var currentSelections = {};
    $('#allBranches_id .selected').each(function(ind, ele) {
        var selected = $(ele).find('input').attr('checked');
        if (selected == 'checked') {
            currentSelections[$(ele).find('label').attr('id')] = true;
        }
    });

    // setup form with the market selections
    $('#allMarkets_id .selected').each(function(ind, ele){
        var selected = $(ele).find('input').attr('checked');
        if (selected == 'checked') {
            var value = $(ele).find('label').attr('id');
            var param = '<input type="hidden" name="markets[' + ind + '].id" value="' + value + '"/>';
            $('#FilterDependencyForm').append(param);
        }
    });

    var data = $('#FilterDependencyForm').serialize();

    // request for filtered list of branches to be shown
    $.ajax({
        type: "POST",
        url: datactx + "/ajax/SelectMarkets.action",
        dataType: "json",
        data : data,
        success: function(json) {
            $("#allBranches_id ul").empty();

            // render new selections
            $.each(json.branches, function(idx,item) {
            	var isChecked = '';
            	var cssClass = '';
            	if (currentSelections[item.id] == 1) {
            		isChecked = ' checked="checked" ';
            		cssClass = 'selected';
            	}
                var strData= "";
                strData += '<li class="' + cssClass + '">';
                strData += '<input type="checkbox" ' + isChecked + 'class="checkbox" value="'+ item.id +'">';
                strData += '<label id="'+ item.id + '" >';
                strData += item.profitCenter;
                strData += '</label>';
                strData += '<div class="clear"></div>'
                strData += '</li>';
                $("#allBranches_id ul").append(strData);
            });

            runBrowserHacks();
            if (callback) {
            	callback();
            }
        },
        error:function(){
        	alert("There was a problem processing your request. Please contact the site administrator.");
        }
    });
}


/**
 * Updates the page display based on the paginated results.
 */
function updateUserSearchPaginationRendering() {
	var metadata = $('#ajax-fragment-container .result-metadata-container');
    var numResultsInPage = parseInt($(metadata).find('.view-result-size').first().text());
    var numTotalMatches = parseInt($(metadata).find('.total-matches').first().text());
    var pageNumber = parseInt($(metadata).find('.current-page').first().text());
    if (pageNumber == 0) {
    	pageNumber = 1;
    }
    var pageSize = $('.per-page-show .last select').val();
    if (pageSize == 0) {
    	pageSize = numResultsInPage;
    }

    // calculate display index (display 0 - 0 of 0)
    var startIndex = 0;
    var endIndex = 0;

    // hide links
	$('.pageLinks a').hide();
	$('.page-jump').hide();

    if (numTotalMatches > 0) {
    	$('.per-page-show .last select').prop('disabled', false);
    	$('.page-trun').show();

    	startIndex = (pageNumber - 1) * pageSize + 1; // index starts with 1
    	endIndex = startIndex + numResultsInPage - 1;

        // now find the page link boundaries (Prev , 1, 2, 3, 4, 5, Next)
        var lowerBound = pageNumber - ((pageNumber + (MAX_PAGE_LINKS - 1)) % MAX_PAGE_LINKS);;
        var	totalPages = Math.ceil(numTotalMatches / pageSize);
        var upperBound = Math.min(lowerBound + (MAX_PAGE_LINKS - 1), totalPages);

        // render the paging links
        $('.page-jump em').text(totalPages);
        $('.page-jump').show();  // jump to [] of X

        if (pageNumber > 1) {
        	// previous/first
            $('.pageLinks a.first-link').unbind('click').bind('click', function() {
            	$('#searchForm input[name="criteria.page"]').val(1);
            	$('#searchForm input[name="page"]').val(1);
            	$('#searchForm input[name="newSearch"]').val(false);
            	doUserSearch();
            });

            $('.pageLinks a.prev-link').unbind('click').bind('click', function() {
            	$('#searchForm input[name="criteria.page"]').val(pageNumber - 1);
            	$('#searchForm input[name="page"]').val(pageNumber - 1);
            	$('#searchForm input[name="newSearch"]').val(false);
            	doUserSearch();
            });

        	$('.pageLinks .first-link').show();
        	$('.pageLinks .prev-link').show();
        }

        // specific pages
        $('.pageLinks a.page').removeClass('current');
        $('.pageLinks').each(function(idx, el) {
        	var linkIndex = 0;
        	for (var i = lowerBound; i <= upperBound; i++) {
        		$(this).find('a.page:eq('+ linkIndex + ')').text(i).show();
        		if (i == pageNumber) {
        			$(this).find('a.page:eq('+ linkIndex + ')').addClass('current');
        		}
        		linkIndex ++;
        	}
        })

        if (totalPages > pageNumber) {
            $('.pageLinks a.last-link').unbind('click').bind('click', function() {
            	$('#searchForm input[name="criteria.page"]').val(totalPages);
            	$('#searchForm input[name="page"]').val(totalPages);
            	$('#searchForm input[name="newSearch"]').val(false);
            	doUserSearch();
            });

            $('.pageLinks a.next-link').unbind('click').bind('click', function() {
            	$('#searchForm input[name="criteria.page"]').val(pageNumber + 1);
            	$('#searchForm input[name="page"]').val(pageNumber + 1);
            	$('#searchForm input[name="newSearch"]').val(false);
            	doUserSearch();
            });

        	// next/last
        	$('.pageLinks .next-link').show();
        	$('.pageLinks .last-link').show();
        }
    }

    ajaxTableLoader = setTimeout(function(){
        var singular = numResultsInPage == 1;
        var entityName = singular ? "Product" : "Products";
        if (numTotalMatches == 0) {
        	$('.total-data').html('');
        } else {
        	$('.total-data').html('Displaying ' + startIndex + ' to '+ endIndex +' of ' + numTotalMatches + ' ' + entityName);
        }
        stripeTable();
    },100);
}


/**
 * Performs product search for regular users.
 */
function doUserSearch() {
	$('.btn-export').addClass("disabled");
	$('.no-data').hide();
	$('#searchForm').attr('action', 'SearchAction.action');
    $('#searchForm input[name="searchType"]').remove();
    var tab = $('.main-nav .nav-link .active a span').first().text();;
    $('#searchForm').append('<input type="hidden" name="searchType" value="' + tab + '"/>');

    $('#searchForm input[name="delta.type"]').remove();
    $('#searchForm input[name="delta.value"]').remove();
    $('#searchForm input[name="delta.endDate"]').remove();
    $('#searchForm input[name="delta.comments"]').remove();

    var isCW = $('#searchForm input[name="whichType"]').val() == 'cwtype';
    if (isCW) {
    	$('#searchForm').ajaxSubmit({
    		beforeSubmit: function() {
    			$('.table-section').hide();
    			$('.table-CW').show();
    			$('.comfirm-filter').hide();
    			$('.search-flyout').hide();
    			$('.tip-for-prices-click').hide();
    			$('.result-section .scroll-section').addClass('hidden-section');
    			$('.result-section .loading').show();
    		},
        	success: function(data){
              // populate placeholder
          	  $('#ajax-fragment-container').html(data);
          	  updateUserSearchPaginationRendering();

        	  // process fragments
        	  $('#cwResulttable').html($('#ajax-fragment-container .table-data-container').html());
        	  $('.result-tree-section').html($('#ajax-fragment-container .result-tree-container').html());

          	  updateUserSearchSortingHeader();
        	  // clear placeholder
        	  $('#ajax-fragment-container').html('');

              ajaxTableLoader = setTimeout(function(){
                   $('.result-section .scroll-section').removeClass('hidden-section');
                   $('.table-CW .loading').hide();
                   if($('#cwResulttable table.data tbody tr').size() > 0){
                       $('.table-CW .scroll-section').show();
                       $('.table-CW .no-data').hide();
                   }else{
                       $('.table-CW .scroll-section').hide();
                       $('.table-CW .no-data').show();
                   }
                   stripeTable();

                   $('.table-CW .toggle a').removeClass('unfolder');
                   $('.table-CW .result-section').find('.tree-panel').show();
                   $('.table-CW .result-section').find('.result-tree').css('width','192px');
                   $('.table-CW .result-section').find('.result-tree-title h4').show();
                   $('.table-CW .toggle').css({height:$('.table-CW .result-table').height()-31});
                   $('.table-CW .tree-inner').css({height:$('.table-CW .result-table').height()-40});
                   $('.table-CW .scroll-pane').css({height:$('.table-CW .result-table').height()-46});
                   if($('.scroll-pane').length){
                       $('.scroll-pane').jScrollPane();
                   }
                   fixRowHeights();
                   if($.browser.msie&&($.browser.version == "7.0")){
                       $('#wrapper #main-container.admin .table-CW .result-section').each(function(){
                           if($(this).find('.scroll-section').height()){
                               $(this).css('height',$(this).find('.scroll-section').height()+17);                                                                                                                                      }
                       });
                   }
                   runBrowserHacks();
              },100);
        	},
        	error:function(){
        		alert("There was a problem processing your request. Please contact the site administrator.");
        	},
        	complete: function() {
        		$('.btn-export').removeClass("disabled");
        	}
    	});
    } else {
    	$('.no-data').hide();
    	$('#searchForm').ajaxSubmit({
    		beforeSubmit: function() {
                $('.table-section').hide();
                $('.table-other-type').show();
                $('.comfirm-filter').hide();
                $('.search-flyout').hide();
                $('.tip-for-prices-click').hide();
                $('.result-section .scroll-section').addClass('hidden-section');
                $('.result-section .loading').show();
    		},

        	success: function(data){
              // populate placeholder
          	  $('#ajax-fragment-container').html(data);
          	  updateUserSearchPaginationRendering();

        	  // process fragments
        	  $('#resulttable').html($('#ajax-fragment-container .table-data-container').html());
        	  $('.result-tree-section').html($('#ajax-fragment-container .result-tree-container').html());

          	  updateUserSearchSortingHeader();
        	  // clear placeholder
        	  $('#ajax-fragment-container').html('');

              ajaxTableLoader = setTimeout(function(){
                   $('.result-section .scroll-section').removeClass('hidden-section');
                   $('.table-other-type .loading').hide();
                   if($('#resulttable table.data tbody tr').size() > 0){
                       $('.table-other-type .scroll-section').show();
                       $('.table-other-type .no-data').hide();
                   }else{
                	   $('.table-other-type .scroll-section').hide();
                       $('.table-other-type .no-data').show();
                   }
                   stripeTable();

                   $('.table-other-type .toggle a').removeClass('unfolder');
                   $('.table-other-type .result-section').find('.tree-panel').show();
                   $('.table-other-type .result-section').find('.result-tree').css('width','192px');
                   $('.table-other-type .result-section').find('.result-tree-title h4').show();
                   $('.table-other-type .toggle').css({height:$('.table-other-type .result-table').height()-31});
                   $('.table-other-type .tree-inner').css({height:$('.table-other-type .result-table').height()-40});
                   $('.table-other-type .scroll-pane').css({height:$('.table-other-type .result-table').height()-46});
                   if($('.table-other-type .scroll-pane').length){
                       $('.table-other-type .scroll-pane').jScrollPane();
                   }
                   fixRowHeights();
                   if($.browser.msie&&($.browser.version == "7.0")){
                       $('#wrapper #main-container.admin .table-other-type .result-section').each(function(){
                           if($(this).find('.scroll-section').height()){
                               $(this).css('height',$(this).find('.scroll-section').height()+17);                                                                                                                      }
                       });
                   }
                   runBrowserHacks();
              },100);
        	},
        	error:function(){
        		alert("There was a problem processing your request. Please contact the site administrator.");
        	},
        	complete: function() {
        		$('.btn-export').removeClass("disabled");
        	}
    	});

    }
}

/**
 * Fixes the heights for the new rows loaded.
 */
function fixRowHeights() {
    var tableHeight = Math.max($('.table-other-type .result-table').height(), 390);
    $('.table-other-type .toggle').css({height:tableHeight-31});
    $('.table-other-type .tree-inner').css({height:tableHeight-40});
    $('.table-other-type .scroll-pane').css({ height: tableHeight - 46 });
    tableHeight = Math.max($('.table-CW .result-table').height(), 390);
    $('.table-CW .toggle').css({height:tableHeight-31});
    $('.table-CW .tree-inner').css({height:tableHeight-40});
    $('.table-CW .scroll-pane').css({ height: tableHeight - 46 });
    if($('.scroll-pane').length){
        $('.scroll-pane').jScrollPane();
    }
}

/**
 * Executes a drill-down operation
 * @param ddType the type of entity to filter
 * @param ddValue the filter id.
 */
function doDrillDown(ddType, ddValue) {
	copyFilters(); // include existing filters to the request
	$('#searchForm').append('<input type="hidden" name="ddType" value="' + ddType + '"/>');
	$('#searchForm').append('<input type="hidden" name="ddValue" value="' + ddValue + '"/>');
	$('#searchForm input[name="newSearch"]').val(false);
	doUserSearch();
	resetAdminSearchPageNumber();
}

/**
 * Runs the browser hacks from the prototype.
 */
function runBrowserHacks() {
    if($.browser.mozilla){
        $('.pagination select').css('position','relative');
        $('.pagination select').css('top','2px');
    }
    if($.browser.msie){
    	$('#modal-add-roles .allRoles_id input').css('margin-bottom', '0px');
        $('#wrapper #main-container.admin .filter li.column .filter-search .checkbox').css('margin-top','6px');
        $('#wrapper #main-container.admin .filter li.column .filter-search .checkbox').css('margin-right','4px');
        $('#wrapper #main-container.admin .filter li.column .scroll li .checkbox').css('margin-top','0px');
        $('#wrapper #main-container.admin .filter li.column .scroll li .checkbox').css('margin-right','2px');
        $('#modal-create-user .form .row-role input, #modal-update-user .form .row-role input').css('margin-top','0px');
        $('.pagination select').css('margin-top','3px');
        $('.remember .checkbox').css('margin-top','-3px');
        $('.input-prices .radio').css('position','relative');
        $('.input-prices .radio').css('top','-3px');
        $('.input-prices .radio').css('margin-right','0px');
        // $('.data td .checkbox').css('margin','-4px');
        $('#wrapper #main-container.admin .result-section .result-table .data th div.checkboxHeader').removeClass('checkboxHeader');
    }
    if($.browser.msie&&($.browser.version == "7.0")){
        $('#wrapper #main-container.admin .filter li.column .filter-search .checkbox').css('margin-top','9px');
        $('#wrapper #main-container.admin .filter li.column .scroll li .checkbox').css('margin-top','1px');
        $('.data input:checkbox').css('margin-top','4px');
        $('.pagination select').css('margin-top','1px');
        $('.input-prices .radio').css('top','-1px');
        $('.remember .checkbox').css('margin-top','-1px');
    }
    if($.browser.msie&&($.browser.version == "9.0")){
        $('#wrapper #main-container.admin .result-section').css('height','339px');
    }
}

/**
 * Executes search for the my pricing tab of the My War Room page.
 */
function doPricingSearch() {
	$('.my-pricings .no-data').hide();
	$('#my-pricings-table').hide();
	$('.my-pricings .loading').show();
	$.ajax({
		type: "POST",
		url : datactx + "/ajax/SearchMyPricingAndFiltersAction.action",
		data: $('#SearchMyPricingForm').serialize(),
		success: function(json){
			var ajaxTableLoader, strPrcingData = '';
			$.each(json.myPricings.values, function(idx,item){
				strPrcingData += '<tr>';
				strPrcingData += '<td class="myPricingName">';
				strPrcingData += escapeText(item.name);
				strPrcingData += '</td>';
				strPrcingData += '<td >';
		        strPrcingData += '<a href="OpenFilter.action?filterId='+ item.criteria.id +'">' + escapeText(item.criteria.name) + '</a>';
				strPrcingData += '</td>';
				strPrcingData += '<td>';
				var d1 = dateFromString(item.createdOn);
				strPrcingData += $.datepicker.formatDate('dd/mm/yy', d1) + ',' + (d1.getHours() < 10 ? '0' + d1.getHours() : d1.getHours()) + ':' + (d1.getMinutes() < 10 ? '0' + d1.getMinutes() : d1.getMinutes());
				strPrcingData += '</td>';
				strPrcingData += '<td>';
				var d2 = dateFromString(item.updatedOn);
				strPrcingData += $.datepicker.formatDate('dd/mm/yy', d2) + ',' + (d2.getHours() < 10 ? '0' + d2.getHours() : d2.getHours()) + ':' + (d2.getMinutes() < 10 ? '0' + d2.getMinutes() : d2.getMinutes());
				strPrcingData += '</td>';
				strPrcingData += '<td>';
				strPrcingData += '<div class="process-bar"><div class="process-bar-inner"></div><span>';
				strPrcingData += escapeText(item.progress) + '%';
				strPrcingData += '</span></div>';
				strPrcingData += '</td>';
				strPrcingData += '<td>';
				strPrcingData += escapeText(item.status);
				strPrcingData += '</td>';
				strPrcingData += '<td>';
				if(item.criteria.type == 'FREIGHT_FILTER' || item.criteria.type == 'INPUT_PRICE_FILTER'){
					strPrcingData += '<a class="btn-delete btn-option" href="javascript:deleteRelatedValue('+ item.criteria.id + ',' + item.relatedValueId + ');">DELETE</a>';
				}
				strPrcingData += '<a class="btn-edit btn-option" href="OpenFilter.action?filterId='+ item.criteria.id + getFitlerForMyPricing(item) + '">EDIT</a>';
				strPrcingData += '</td>';
				strPrcingData += '</tr>';
			});

			if (json.myPricings.total > 0) {
				$('.my-pricings table.data tbody').empty().append(strPrcingData);
			} else {
				$('.my-pricings table.data tbody').empty();
			}

			updatePaginationRendering($('.my-pricings'), json.myPricingSearchCriteria, json.myPricings);
			updateSearchSortingHeader($('.my-pricings'), json.myPricingSearchCriteria);

			//Process bar
			$('.process-bar').each(function(){
				$(this).find('.process-bar-inner').css('width',parseInt($(this).find('span').text())/100*$(this).width());
			});
		},
		error:function(){
			alert("There was a problem processing your request. Please contact the site administrator.");
		}
	});
}

/**
 * Get product id and branch id from my pricing.
 */
function getFitlerForMyPricing(myPricing) {
	var strFilter = '';
	if(myPricing.cwPricingRecord != null){
		strFilter += '&productId=' + myPricing.cwPricingRecord.product.id;

		var branchId = myPricing.branchId;
		if(branchId != 0){
			strFilter += '&branchId=' + branchId;
		}
	}else if(myPricing.pricingRecord != null){
		strFilter += '&productId=' + myPricing.pricingRecord.product.id;

		var branchId = myPricing.branchId;
		if(branchId != 0){
			strFilter += '&branchId=' + branchId;
		}
	}

	return strFilter;
}
/**
 * Executes search for the my saved filters tab of the My War Room page.
 */
function doSavedFilterSearch() {
	$('.my-saved-filter .no-data').hide();
	$('#my-saved-filter-table').hide();
	$('.my-saved-filter .loading').show();
	$.ajax({
		type: "POST",
		url : datactx + "/ajax/SearchMyPricingAndFiltersAction.action",
		data: $('#SearchMyFiltersForm').serialize(),
		success: function(json){
			var ajaxTableLoader,strData = '';
			$.each(json.userFilterRecords.values, function(idx,item){
				strData += '<tr>';
				strData += '<td class="myFilterName">';
				strData += escapeText(item.name);
				strData += '</td>';
				strData += '<td class="myFilterType">';
				strData += filterTypeDescription[item.type];
				strData += '</td>';
				strData += '<td class="myFilterDesc">';
				strData += escapeText(item.shortDescription);
				strData += '<div class="tip-for-filter-hover">' + escapeText(item.fullDescription) + '</div>';
				strData += '<a href="javascript:;=' + item.id + '" class="details-link">Details</a>';
				strData += '</td>';
				strData += '<td>';
				var d = dateFromString(item.updatedOn);
				strData += $.datepicker.formatDate('dd/mm/yy', d) + ',' + (d.getHours() < 10 ? '0' + d.getHours() : d.getHours()) + ':' + (d.getMinutes() < 10 ? '0' + d.getMinutes() : d.getMinutes());
				strData += '</td>';
				strData += '<td>';
				strData += '<a href="javascript:;" rel="'+ item.id+'" class="btn-delete btn-option">DELETE</a>';
				strData += '<a href="OpenFilter.action?filterId=' + item.id+ '" class="btn-next btn-option">NEXT</a>';
				strData += '</td>';
				strData += '</tr>';
			});

			if (json.userFilterRecords.total > 0) {
				$('.my-saved-filter table.data tbody').empty().append(strData);
			} else {
				$('.my-saved-filter table.data tbody').empty();
			}
			updatePaginationRendering($('.my-saved-filter'), json.userFilterRecordCriteria, json.userFilterRecords);
			updateSearchSortingHeader($('.my-saved-filter'), json.userFilterRecordCriteria);
		},
		error:function(){
			alert("There was a problem processing your request. Please contact the site administrator.");
		}
	});
}

/**
 * Updates the page display based on the paginated results.
 * This function supports multiple grids per page by using a container for the relative lookups
 *
 * @paran {HTMLElement} container for the grid to be modified
 * @param {Object} the BaseCriteria json for the filter that was used by the grid
 * @param {Object} the Search results for the grid
 */
function updatePaginationRendering(container, criteria, result) {
    var numResultsInPage = result.values.length;
    var numTotalMatches = result.total;
    var pageNumber = result.page;
    var pageSize = criteria.pageSize;

    if (pageSize == 0) {
    	pageSize = numResultsInPage;
    }

    // calculate display index (display 0 - 0 of 0)
    var startIndex = 0;
    var endIndex = 0;

    // hide links
	$(container).find('.pageLinks a').hide();
	$(container).find('.page-jump').hide();

    if (numTotalMatches > 0) {
    	$(container).find('.per-page-show .last select').prop('disabled', false);
    	$(container).find('.page-trun').show();

    	startIndex = (pageNumber - 1) * pageSize + 1; // index starts with 1
    	endIndex = startIndex + numResultsInPage - 1;

        // now find the page link boundaries (Prev , 1, 2, 3, 4, 5, Next)
        var lowerBound = pageNumber - ((pageNumber + (MAX_PAGE_LINKS - 1)) % MAX_PAGE_LINKS);;
        var	totalPages = Math.ceil(numTotalMatches / pageSize);
        var upperBound = Math.min(lowerBound + (MAX_PAGE_LINKS - 1), totalPages);

        // render the paging links
        $(container).find('.page-jump em').text(totalPages);
        $(container).find('.page-jump').show();  // jump to [] of X

        // process top and bottom pagers
        $(container).find('.page-trun').each(function (k, pager) {
	        if (pageNumber > 1) {
	        	// previous/first
	        	if($(container).hasClass('my-saved-filter')){
	        		$(pager).find('.pageLinks a.first-link').unbind('click').bind('click', function() {
	        			$(container).find('input[name="userFilterRecordCriteria.page"]').val(1);
	        			doSavedFilterSearch();
	        		});

	        		$(pager).find('.pageLinks a.prev-link').unbind('click').bind('click', function() {
	        			$(container).find('input[name="userFilterRecordCriteria.page"]').val(pageNumber - 1);
	        			doSavedFilterSearch();
	        		});
	        	}

	        	if($(container).hasClass('my-pricings')){
	        		$(pager).find('.pageLinks a.first-link').unbind('click').bind('click', function() {
	        			$(container).find('input[name="myPricingSearchCriteria.page"]').val(1);
	        			doPricingSearch();
	        		});

	        		$(pager).find('.pageLinks a.prev-link').unbind('click').bind('click', function() {
	        			$(container).find('input[name="myPricingSearchCriteria.page"]').val(pageNumber - 1);
	        			doPricingSearch();
	        		});
	        	}

	        	$(pager).find('.pageLinks .first-link').show();
	        	$(pager).find('.pageLinks .prev-link').show();
	        }

	        var linkIndex = 0;

        	$(pager).find('.pageLinks a.page').removeClass('current');
        	for (var i = lowerBound; i <= upperBound; i++) {
        		$(pager).find('.pageLinks a.page:eq('+ linkIndex + ')').text(i).show();
        		if (i == pageNumber) {
        			$(pager).find('.pageLinks a.page:eq('+ linkIndex + ')').addClass('current');
        		}
        		linkIndex ++;
        	}

        	if (totalPages > pageNumber) {
	        	if($(container).hasClass('my-saved-filter')){
	        		$(pager).find('.pageLinks a.last-link').unbind('click').bind('click', function() {
	        			$(container).find('input[name="userFilterRecordCriteria.page"]').val(totalPages);
	        			doSavedFilterSearch();
	        		});

	        		$(pager).find('.pageLinks a.next-link').unbind('click').bind('click', function() {
	        			$(container).find('input[name="userFilterRecordCriteria.page"]').val(pageNumber + 1);
	        			doSavedFilterSearch();
	        		});
	        	}

	        	if($(container).hasClass('my-pricings')){
	        		$(pager).find('.pageLinks a.last-link').unbind('click').bind('click', function() {
	        			$(container).find('input[name="myPricingSearchCriteria.page"]').val(totalPages);
	        			doPricingSearch();
	        		});

	        		$(pager).find('.pageLinks a.next-link').unbind('click').bind('click', function() {
	        			$(container).find('input[name="myPricingSearchCriteria.page"]').val(pageNumber + 1);
	        			doPricingSearch();
	        		});
	        	}

        		// next/last
        		$(pager).find('.pageLinks .next-link').show();
        		$(pager).find('.pageLinks .last-link').show();
        	}
        });
        $(container).find('.total-data').html('Displaying ' + startIndex + ' to '+ endIndex +' of ' + numTotalMatches + ' Filters');
    }

    ajaxTableLoader = setTimeout(function(){
    	$(container).find('.loading').hide();
    	if(numTotalMatches > 0){
    		$(container).find('table.data').show();
    		$(container).find('.no-data').hide();
    	} else{
    		$(container).find('table.data').hide();
    		$(container).find('.no-data').show();
    	}
    	stripeTable();
    },100);
}

/**
 * This function will call the export controller and allows user to download the
 * current search results as a CSV.
 */
function doExportResults() {
    var searchType = $('.main-nav .nav-link .active a span').first().text();;
    var whichType = $('#searchForm input[name="whichType"]').val();

    $('#ExportResultsForm input[name="searchType"]').val(searchType);
    $('#ExportResultsForm input[name="whichType"]').val(whichType);
    $('#ExportResultsForm').submit();
}

/**
 * This function saves the user filter.
 */
function saveFilters(isCriteria) {
	copyFilters();
	if(isCriteria){
		$('#searchForm').append('<input type="hidden" name="userFilterRecord.criteriaFlag" value="true"/>');
		var randomNum = Math.random().toString().substr(2, 9);
		$('#searchForm').append('<input type="hidden" name="userFilterRecord.name" value="criteria'+randomNum+'"/>');
	}else{
		// append name
		var name = $.trim($('.tip-save-search').find('input.text').val());
		$('#searchForm').append('<input type="hidden" name="userFilterRecord.name" value="' + name + '"/>');
	}

	// fpr setting the search type.
    $('#searchForm input[name="searchType"]').remove();
    var tab = $('.main-nav .nav-link .active a span').first().text();;
    $('#searchForm').append('<input type="hidden" name="searchType" value="' + tab + '"/>');

    var data = $('#searchForm').serialize();

    var criteriaId;
    $.ajax({
        type: "POST",
        url: datactx + "/ajax/SaveSearchFilterAction.action",
        dataType: "json",
        async: false,
        data : data,
        success: function(json, status){
            var ajaxTableLoader,strData = '';
            if(isCriteria){
            	criteriaId = json.userFilterRecord.id;
            }else{
            	alert(json.returnMessage);
            	if (json.returnMessage != "Filter name already exists") {
            		var fullName = escapeText(json.userFilterRecord.name);
            		var trimmed = fullName;
            		if (trimmed.length > 20) {
            			var trimmed = trimmed.substring(0, 20) + '...';
            		}
            		var strData = '<li id="my-filters-list-'+ json.userFilterRecord.id + '"><a href="OpenFilter.action?filterId='+ json.userFilterRecord.id + '" title="'+ fullName +'">'+ trimmed  + '</a></li>'
            		$('#my-filters-list-container').append(strData);
            		$('.tip-save-search').hide();
            	}
            }
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
        	alert("There was a problem processing your request. Please contact the site administrator.");
        }
    });

    return criteriaId;
}

/**
 * Converts from xml date format to javascript date.
 * @param s the string in xml format
 * @returns {Date} the converted date
 */
function dateFromString(s) {
  var parts = s.split(/[-T:]/g);
  var d = new Date(parts[0], parts[1]-1, parts[2]);
  d.setHours(parts[3], parts[4], parts[5]);
  return d;
}

/**
 * Format a string date.
 * @param s the string in xml format
 * @returns formatted string date
 */
function formatDateString(s) {
	if(s == null || typeof(s) == "undefined" || s.length == 0){
		return;
	}
    var str = s.toString();
    var dateString = str.substring(5,7) + '/' + str.substring(8,10) + '/'+ str.substring(0,4);
    return dateString;
}

/**
 * Save the criertia filter and my pricing.
 */
function saveCriteriaAndMyPricing(){
	var criteriaId = saveFilters(true);
	$.ajax({
		type: "POST",
		async: false,
		url: datactx + "/ajax/SaveMyPricingAction.action",
		data: {'criteriaId':criteriaId},
		success: function(data) {
			// empty
		},
		error:function(){
			alert("There was a problem processing your request. Please contact the site administrator.");
		}
	});
}

/**
 * Delete my pricing's related value.
 */
function deleteRelatedValue(criteriaId, relatedValueId){
	$.ajax({
		type: "POST",
		url: datactx + "/ajax/DeleteValueByMyPricingAction.action",
		data: {'criteriaId':criteriaId, 'relatedValueId':relatedValueId},
		success: function(data) {
			if(data.hasDeleteRelatedValue){
				alert('The new value related to this pricing has been deleted.');
			}
		},
		error:function(){
			alert("There was a problem processing your request. Please contact the site administrator.");
		}
	});
}

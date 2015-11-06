
function initMainJS() {
	
	/*
	 * Initialize the main javascript to hide alert box and init ajax error handler
	 */
	$(".ajaxAlert").hide();
	$(document).ajaxError(function(event, jqxhr, settings, thrownError) {
		$(".ajaxAlert").show();
		$(".ajaxAlert").append(
		                "<strong>Ajax Error</strong> "
						+ settings.url + " -> "
		                + thrownError + "<br/>"
		);
	});
	
	/* 
	 * Fix issue with sidebar toggle button disapearing
	 */
    $('.sidebar-toggle').click(function() {
    	
    	$('.sidebar-toggle').removeClass('visible-xs');   	
    	
//    	var hidden = $('body').hasClass('sidebar-collapse');
//    	sessionStorage.setItem('sidebar-collapse', hidden);
    });
    
//    var sidebarHidden = sessionStorage.getItem('sidebar-collapse');
//    console.log(sidebarHidden);
//    if (sidebarHidden && sidebarHidden == 'true') {
//    	//$('body').addClass('sidebar-collapse');
//    	document.getElementsByTagName('body')[0].className += " sidebar-collapse";
//    }
    
    /*
     * Initialize select2 components within the page
     */
    $('.select2').select2();
    
    /*
     * Set default date format for datepicker widgets
     */
    $.fn.datepicker.defaults.format = "dd/mm/yyyy";
    
    /*
	 * Call modal to quickly change project
	 */
	$('.toggle-change-project').click(function() {
		changeProjectModal.onSelect(function(key) {
			document.location = '/ui/requirements/'+key
		});
		changeProjectModal.show();
	});
    
}

function define_yesModalButton_clickAction(action) {
	$('#btnModalYes').click(action);
}

/**
 * Object for interacting with the modal
 * asking for a confirmation before deleting
 */
function DeleteModal() {
	
	var modalSelector = '#modal';
	var btnYesSelector = '#btnModalYes';
	
	return {
		
		'set': function(key, value) {
			$(modalSelector).data(key, value);
		},
		
		'get': function(key) {
			return $(modalSelector).data(key);
		},
		
		'show': function() {
			$(modalSelector).modal('show');
		},
		
		'hide': function() {
			$(modalSelector).modal('hide');
		},
		
		'onYesAction': function(action) {
			$(btnYesSelector).click(action);
		}
		
	}
	
}

/**
 * Object for interacting with the modal
 * saying that the user does not have the required permission
 * to do an action
 */
function OperationForbiddenModal() {
	
	var modalSelector = "#forbiddenModal";
	
	return {
		'show': function() {
			$(modalSelector).modal('show');
		},
		
		'hide': function() {
			$(modalSelector).modal('hide');
		}
	}
}

/**
 * Object for interacting with the modal
 * to quickly change project
 */
function ChangeProjectModal() {
	
	var modalSelector = "#modal-change-project";
	var treeviewSelector = '.change-project-treeview';
	
	function init(data) {
		$(treeviewSelector).jstree({
			"core": {
				"animation" : 0,
			    "check_callback" : true,
			    "themes" : { "stripes" : true },
			    'data' : data
			},
			"plugins": ["types", "search"],
			'types': {
				'default': { icon: 'glyphicon glyphicon-folder-open' }
			}
		});
	}	
	
	return {
		
		'show': function() {
			$(modalSelector).modal('show');
			restAPI.portfolio.jstree(init);		
		},

		'hide': function() {
			$(modalSelector).modal('hide');
		},
		
		'onSelect': function(callback) {
			$(treeviewSelector).bind("select_node.jstree", function(e, data) {
				var key = data.instance.get_node(data.node, true).children('a').data('key');
				if (key) {
					callback(key);
				}
			});
		}
	}
	
}

/**
 * Object for interacting with the modal
 * to quiclky select a user
 */
function SelectUserModal() {
	
	var modalSelector = "#modal-select-user";
	var selectAction = null;
	var users = null;
	
	function display() {
		
		$(modalSelector+' table').dataTable();
		$(modalSelector).modal('show');
		
		$(modalSelector+' .btn-pick').off("click");
		$(modalSelector+' .btn-pick').click(function() {
			var tr = $(this).closest('tr');
			var id = tr.data('id');
			if (id && selectAction) {				
				selectAction(getUserByID(id), tr);
			}
		});
	}
	
	function getUserByID(id) {
		for(var i=0; i<users.length; i++) {
			if (users[i].id == id) {
				return users[i];
			} 
		}
		return null;
	}
	
	function addUser(user) {
		var line = $('<tr></tr>');
		line.data('id', user.id);
		if (!user.active) line.addClass('warning');
		line.append('<td><img src="http://www.gravatar.com/avatar/'+ user.gravatar +'?s=48=" class="img-responsive"></td>');
		line.append('<td>'+user.login+'</td>');
		line.append('<td>'+user.firstName+'</td>');
		line.append('<td>'+user.lastName+'</td>');
		line.append('<td>'+user.email+'</td>');
		line.append('<td>'+user.role+'</td>');
		line.append('<td><button class="btn-pick btn btn-primary btn-xs" data-title="Select"><span class="fa fa-crosshairs"></span></button></td>');
		
		$(modalSelector+' .table').append(line);
	}
	
	return {
		
		'show': function() {
			
			if (!users) {
				restAPI.users.allwithrole(function(dto) {
					$.each(dto.data, function(i, v) {
						addUser(v);
					});
					users = dto.data;
					display();
				});
			}else{
				display();
			}
			
		},

		'hide': function() {
			$(modalSelector).modal('hide');
		},
		
		'onSelect': function(callback) {
			selectAction = callback;
		}
	}
	
}

/*
 * Create modal instances
 */
var deleteModal = new DeleteModal();
var changeProjectModal = new ChangeProjectModal();
var selectUserModal = new SelectUserModal();
var operationForbiddenModal = new OperationForbiddenModal();

$(document).ready(function() {
	
	$('.project-releases-tree-view').jstree({
		"core": {
			"animation" : 0,
		    "check_callback" : true,
		    "themes" : { "stripes" : true },
		    'data' : [
               { text: '1.4', icon: 'glyphicon glyphicon-tag' },
               { text: '1.3', icon: 'glyphicon glyphicon-tag' },
               { text: '1.2', icon: 'glyphicon glyphicon-tag' },
               { text: '1.1', icon: 'glyphicon glyphicon-tag' },
               { text: '1.0', icon: 'glyphicon glyphicon-tag' }
		    ]
		},
		"plugins": ["dnd", "contextmenu"],
		"contextmenu": {
			"items": {
				"add": {
					label: "Add",
					action: function(node) {
						var name = node.reference[0].text;
						console.log('adding new element in node: '+name);
					}
				},
				"delete": {
					label: "Delete",
					action: function(node) {
						console.log(node);
						console.log('deleting node: '+node.reference[0].text);
					}
				}
			}
		}
	});
	
});

/**
 * Automatically toggle a tab
 * if the tab id is specified in the URL
 */
function tabRooting() {
	
	var target = window.location.hash; 
	if(target) {		
		$.each($('.nav-tabs li'), function(i, elt){
			var toggleButton = $(elt).find('a');
			if (toggleButton.attr('href') == target) {
				toggleButton.click();
			}
		});
	}
	
}
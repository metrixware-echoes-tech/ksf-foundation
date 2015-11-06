var PROJECTS_TREEVIEW 	= '.projects-tree-view';
var TREE_SEARCH_BAR 	= '.tree-search';
var FILTER_CATEGORIES 	= "#filter_categoryId";
var FILTER_STATUSES		= '#filter_statusId';
var FILTER_OWNERS		= '#filter_ownerId';
var FILTER_TAGS			= '#filter_tags';
var FILTER_MODAL		= '#modal-portoflio-tree-filter';

var jsTreeManager = new JsTreeManager(PROJECTS_TREEVIEW);

function initPortfolio() {

	/*
	 * Initialize JsTree widget
	 */
	restAPI.folderNodeTypes.findAll(function(types) {
		restAPI.portfolio.jstree(function(data) {
			initJsTree(types, data);
		});
	});
	
	/**
	 * Action triggered when clicking on a node
	 */
	jsTreeManager.setSelectAction(function(node, key) {
		if (key) {
			document.location = '/ui/portfolio/manager/' + key;
		}else{
			showSelectedNode(node);
		}
	});
	
	initDeleteProjectModal();
	
	initTreeFilters();
	
}

function initDetailsView() {
	
	/*
	 * Initialize Bootstrap WYSIHTML5 widget
	 */
	$(".textarea").wysihtml5();
	
	/*
	 * Initialize Select2 widget
	 */
	$('.select2').select2({
		minimumResultsForSearch : Infinity
	});
	
	/*
	 * Initialize DatePicker widget
	 */
	$('.datepicker').datepicker();

	/*
	 * Initialize TagManager widget
	 */
	restAPI.projects.tags.find(pageData.projectKey, function(data) {
		$(".tm-input").tagsManager({
			prefilled : data,
			AjaxPush : "/rest/projects/"+pageData.projectKey+"/tags/push",
			AjaxPushAllTags : true
		});
	});
	
	/*
	 * Initialize JsTree widget
	 */
	restAPI.folderNodeTypes.findAll(function(types) {
		restAPI.portfolio.subtree(pageData.projectKey, function(data) {
			initJsTree(types, data);
		});
	});
	
	/**
	 * Action triggered when clicking on a node
	 */
	jsTreeManager.setSelectAction(function(node, key) {
		if (key) {
			document.location = '/ui/projects/' + key;
		}
	});
	
	/**
	 * Init modals
	 */
	initDeleteProjectModal();
	initDeleteProjectBranchModal();
	
}

/*
 * Initialize JsTree widget
 */
function initJsTree(typeData, data) {
	
	var types = {};
	var folderActions = {};
	var folderEditActions = {};
	if (typeData) {
		$.each(typeData, function(i,v) {
			types[v.id] = { 'icon': v.icon };
			folderActions[v.id] = {
					label: v.name,
					icon: v.icon,
					action : function(data) {
						var node = jsTreeManager.extractNode(data);
					    jsTreeManager.addFolder(v.id, node);
					}
			};
			folderEditActions[v.id] = {
					label: v.name,
					icon: v.icon,
					action : function(data) {
						var node = jsTreeManager.extractNode(data);
					    changeFolderType(v.id, node);
					},
					_disabled: function(data) {
						var node = jsTreeManager.extractNode(data);
					    var currentType = v.id;
						return jsTreeManager.getType(node) == currentType;
					}
			};
		});
	}
	
	types['default'] = { 'icon' : 'fa fa-genderless' };
	
	$(PROJECTS_TREEVIEW).jstree({
		"core" : {
			"animation" : 0,
			"themes" : {
				"stripes" : true
			},
			data: data,
			'check_callback': jsTreeManager.callbackHandler
		},
		"plugins" : [ "dnd", "contextmenu", "types", "search", "state" ],
		"contextmenu" : {
			"items" : {
				"add" : {
					label : "Add",
					icon : "fa fa-plus",
					submenu: {
						'add_folder':  {
							label: "Folder",
							icon: "glyphicon glyphicon-folder-open",
							submenu: folderActions
						}
					}			
				},
				"edit" : {
					label : "Edit",
					icon : "fa fa-pencil",
					shortcut : 113,
					shortcut_label : 'F2',
					action : function(data) {
						var node = jsTreeManager.extractNode(data);
					    var projectID = jsTreeManager.getProjectID(node);
					    if (!projectID) {
					    	jsTreeManager.editNode(node);
					    }
					}
				},
				
				"type" : {
					label: "Type",
					icon: "fa fa-tag",
					submenu: folderEditActions,
					_disabled: function(data) {
						var node = jsTreeManager.extractNode(data);
					    var projectID = jsTreeManager.getProjectID(node);
					    return projectID;
					}
				},
				
				"delete" : {
					label : "Delete",
					icon : "fa fa-trash",
					action : function(data) {
						var node = jsTreeManager.extractNode(data);
					    var ID = jsTreeManager.getNodeID(node);
					    restAPI.portfolio.remove(ID, function(data) {
					    	jsTreeManager.deleteNode(node);
					    });
					}
				}
			}
		},
		'types' : types
	});
	
	/**
	 * Action triggered when the treeview is fully loaded
	 */
	jsTreeManager.setLoadedAction(function() {
		if (pageData && pageData.projectKey) {
			var node = jsTreeManager.getNodeByProjectKey(pageData.projectKey);
			if (node) {
				jsTreeManager.toggleNode(node);
				showSelectedNode(node);
			}
		}		
	});

	/**
	 * Action triggered when a new folder has been added in the JsTree
	 */
	jsTreeManager.setCreateAction(function(node, text, typeID, parentID) {
		restAPI.portfolio.folders.add(text, typeID, parentID, function(data) {
			console.log("created node with text: "+text);
			console.log(data);
			if (data && data.id) {
				jsTreeManager.setNodeID(node, data.id);
			}else{
				jsTreeManager.deleteNode(node);
			}
		});
	});
	
	/**
	 * Action triggered when renaming a text node
	 */
	jsTreeManager.setEditAction(function(node, nodeID, text) {
		if (nodeID) {
	    	restAPI.portfolio.folders.edit(nodeID, text, function(data) {
	    		console.log("edited node #"+nodeID+" with text: "+text);
	    	});
    	}
	});
	
	/**
	 * Action triggered when a node has been moved in the JsTree
	 */
	jsTreeManager.setMoveAction(function(nodeID, parentID) {
		restAPI.portfolio.move(nodeID, parentID, function() {
			console.log("moved node #"+nodeID+" into node #"+parentID);
		});
	});

	/**
	 * Action triggered when the user types something
	 * in the treeview's search bar
	 */
	$(TREE_SEARCH_BAR).change(function() {
		var value = $(TREE_SEARCH_BAR).val();
		jsTreeManager.search(value);
	});
	
}

/**
 * Change the type of a folder node
 * @param typeID
 * @param node
 */
function changeFolderType(typeID, node) {
	var ID = jsTreeManager.getNodeID(node);
	restAPI.portfolio.folders.changeType(ID, typeID, function(data) {
		jsTreeManager.setType(node, typeID);
	});
}

function showSelectedNode(node) {
	$('.selected-node-text').text(node.text);
	$('.selected-node-ID').val(jsTreeManager.getNodeID(node));
}

function selectUser() {
	
	selectUserModal.show();
	
	selectUserModal.onSelect(function(user, line) {
		
		console.log(user);

		$('input[name=ownerId]').val(user.id);
		$('#ownerName').text(user.firstName+' '+user.lastName);
		
		selectUserModal.hide();
	});
	
}

function initDeleteProjectModal() {
	
	$('.projectDelete').on('click', function(e) {

		e.preventDefault();
		var projectKey = pageData.projectKey;
		
		deleteModal.show();
		
		deleteModal.onYesAction(function() {		
			document.location = '/ui/projects/'+projectKey+'/delete';			
			deleteModal.hide();			
		});
		
	});
	
}

function initDeleteProjectBranchModal() {
	
	$('.branchDelete').on('click', function(e) {

		e.preventDefault();
		var line = $(this).closest('tr');
		var projectKey = pageData.projectKey;
		var branchName = line.data('branchname');
		
		deleteModal.show();
		
		deleteModal.onYesAction(function() {
			
			deleteModal.hide();
			
			restAPI.projects.branches.delete(projectKey, branchName, function(data) {
				console.log("branch "+branchName+" deleted.");
				line.remove();				
			});

		});
		
	});
	
}

/**
 * Initialize the filter modal
 */
function initTreeFilters() {
	
	$(FILTER_CATEGORIES).select2({
		ajax: {
			url: restAPI.projects.categories.URL,
			dataType: 'json',
			processResults: function(data, page) {
				var items = [{ id: 0, text: 'All' }];
				$.each(data, function(i, v) {
					items.push({ id: v.id, text: v.name });
				});
				return { 
					results: items 
				}
			}
		}
	});
	
	$(FILTER_STATUSES).select2({
		ajax: {
			url: restAPI.projects.statuses.URL,
			dataType: 'json',
			processResults: function(data, page) {
				var items = [{ id: 0, text: 'All' }];
				$.each(data, function(i, v) {
					items.push({ id: v.id, text: v.name });
				});
				return { 
					results: items 
				}
			}
		}
	});
	
	$(FILTER_OWNERS).select2({
		ajax: {
			url: restAPI.users.URL,
			dataType: 'json',
			processResults: function(data, page) {
				var items = [{ id: 0, text: 'All' }];
				$.each(data.data, function(i, v) {
					items.push({ id: v.id, text: v.firstName+' '+v.lastName });
				});
				return { 
					results: items 
				}
			}
		}
	});
	
	$(FILTER_TAGS).select2({
		ajax: {
			url: restAPI.projects.tags.URL,
			dataType: 'json',
			processResults: function(data, page) {
				var items = [];
				$.each(data, function(i, v) {
					items.push({ id: v, text: v });
				});
				return { 
					results: items 
				}
			}
		}
	});
	
}

/**
 * Filter the JsTree widget
 */
function filterTree() {
	
	var filter = {
			'categoryIds'	: $(FILTER_CATEGORIES).val(),
			'statusIds'		: $(FILTER_STATUSES).val(),
			'ownerIds'		: $(FILTER_OWNERS).val(),
			'tags'			: $(FILTER_TAGS).val()
	};
	
	restAPI.projects.filter(filter, function(data) {
		jsTreeManager.showAll();
		var nodes = jsTreeManager.getNodes();
		filterNodes(data, nodes);
		$(FILTER_MODAL).modal('hide');
	});
	
}

function filterNodes(keys, nodes) {
	$.each(nodes, function(i, node) {
		var key = jsTreeManager.getProjectKey(node);
		if (key && $.inArray(key, keys) == -1) {
			jsTreeManager.hideNode(node);
		}
		if (node.children && node.children.length > 0) {
			filterNodes(keys, node.children);
		}
	});
}

/**
 * Clear filters
 * Redraw the original tree view
 */
function clearFilters() {
	jsTreeManager.showAll();
}
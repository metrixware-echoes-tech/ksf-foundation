
var ITEMS_TREEVIEW = '.project-items-tree-view';
var SUNBURST = '.project-items-sunburst';

var jsTreeManager = new JsTreeManager(ITEMS_TREEVIEW);

function initRequirementView() {
	
	/*
	 * Initialize Bootstrap WYSIHTML5
	 */
	$(".textarea").wysihtml5();
	
	/*
	 * Initialize TagManager widget
	 */
	$(".tm-input").tagsManager({
		 prefilled: ["KPI", "CRUD Operation"],
		 AjaxPush: null,
		 AjaxPushAllTags: true
	});
	
	$(".tm-input").on('tm:afterPush', function(e, tag) {
		console.log(tag + " was pushed!");
	});
	
	$(".tm-input").on('tm:afterSplice', function(e, tag) {
		console.log(tag + " was removed!");
	});
	
	/*
	 * Handle click on a revision
	 */
	$('#revisions tr td.toggle').click(function() {
		var link = $(this).parent().data('href');
		if (link) {
			document.location = link;
		}
	});
	
	/**
	 * Handle switch branch
	 */
	$('#selectBranch').change(function() {
		var name = $('#selectBranch').find(":selected").text();
		redirectTo(pageData.projectKey, name);
	});
	
	/*
	 * Initialize JsTree widget
	 */
	restAPI.folderNodeTypes.findAll(function(types) {
		restAPI.requirements.jstree(pageData.projectKey, pageData.branchName, function(data) {
			initJsTree(types, data);
		});
	});
		
}

/**
 * Change page location
 * @param projectKey
 * @param branchName
 * @param requirementKey
 */
function redirectTo(projectKey, branchName, requirementKey) {
	var url = '/ui/requirements/'+projectKey;
	if (branchName) {
		url += '/'+branchName;
	}
	if (requirementKey) {
		url += '/'+requirementKey
	}
	document.location = url;
}

/**
 * Initializes JsTree widget
 * @param data
 */
function initJsTree(typeData, data) {
	
	var types = {
			"REQ": {
				icon: 'glyphicon glyphicon-list-alt'
			}
	};
	var folderActions = {};
	var folderEditActions = {}
	if (data) {
		$.each(typeData, function(i,v) {
			types[v.id] = { 'icon': v.icon }
			folderActions[v.id] = {
					label: v.name,
					icon: v.icon,
					action : function(data) {
						var node = jsTreeManager.extractNode(data);
					    jsTreeManager.addFolder(v.id, node);
					}
			}
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
			}
		});
	}
	
	$(ITEMS_TREEVIEW).jstree({
		"core": {
			"animation" : 0,
		    "check_callback" : true,
		    "themes" : { "stripes" : true },
		    'data' : data,
		    'check_callback': jsTreeManager.callbackHandler
		},
		"plugins": ["dnd", "contextmenu", "types"],
		"contextmenu": {
			"items": {
				"add": {
					label: "Add",
					icon: 'glyphicon glyphicon-plus',
					'submenu': {
						'add_folder':  {
							label: "Folder",
							icon: "glyphicon glyphicon-folder-open",
							submenu: folderActions
						},
						'requirement': {
							label: 'Requirement'
						},
						'user_story': {
							label: 'User Story'
						},
						'set' : {
							label: 'Set'
						},
						'test_case' : {
							label: 'Test Case'
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
					    var ID = jsTreeManager.getNodeID(node);
					    var requirementID = jsTreeManager.getRequirementID(node);
					    if (!requirementID) {
					    	console.log('editing node: ' + ID);
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
					    var requirementID = jsTreeManager.getRequirementID(node);
					    return requirementID;
					}
				},
				"delete": {
					label: "Delete",
					icon: 'glyphicon glyphicon-remove',
					action: function(data) {
						var node = jsTreeManager.extractNode(data);
					    var ID = jsTreeManager.getNodeID(node);
					    restAPI.requirements.folders.remove(pageData.projectKey, pageData.branchName, ID, function(data) {
					    	jsTreeManager.deleteNode(node);
					    });
					}
				}
			}
		},
		"types": types
	});
	
	/**
	 * Action triggered when clicking on a node
	 */
	jsTreeManager.setSelectAction(function(node, key) {
		if (key) {
			redirectTo(pageData.projectKey, pageData.branchName, key);
		}
	});
	
	/**
	 * Action triggered when the jstree is fully loaded
	 */
	$(ITEMS_TREEVIEW).bind("loaded.jstree", function(e, data) {
		jsTreeManager.expand();
	});
	
	/**
	 * Action triggered when a new folder has been added in the JsTree
	 */
	jsTreeManager.setCreateAction(function(node, text, typeID, parentID) {
		restAPI.requirements.folders.add(pageData.projectKey, pageData.branchName, text, typeID, parentID, function(data) {
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
	 * Action triggered when the text of a node has been edited in the JsTree
	 */
	jsTreeManager.setEditAction(function(node, nodeID, text) {
		restAPI.requirements.folders.edit(pageData.projectKey, pageData.branchName, nodeID, text, function(data) {
			console.log("edited node #"+nodeID+" with text: "+text);
		});
	});
	
	/**
	 * Action triggered when a node has been moved in the JsTree
	 */
	jsTreeManager.setMoveAction(function(nodeID, parentID) {
		restAPI.requirements.move(pageData.projectKey, pageData.branchName, nodeID, parentID, function(data) {
			console.log("moved node #"+nodeID+" into node #"+parentID);
		});
	});
		
}

function changeFolderType(typeID, node) {
	var ID = jsTreeManager.getNodeID(node);
	restAPI.requirements.folders.changeType(pageData.projectKey, pageData.branchName, ID, typeID, function(data) {
		jsTreeManager.setType(node, typeID);
	});
}
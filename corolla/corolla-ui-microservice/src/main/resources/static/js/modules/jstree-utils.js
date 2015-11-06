
function JsTreeManager(SELECTOR) {
	
	var editAction = null;
	var createAction = null;
	var selectAction = null;
	var moveAction = null;
	var loadedAction = null;
	
	function getNodeID(node) {
		return node && node.a_attr ? node.a_attr['data-nodeID'] : null;
	}
	
	function getProjectKey(node) {
		return node && node.a_attr ? node.a_attr['data-key'] : null;
	}
	
	function getNodeByProjectKey(nodes, key) {
		for(var i=0; i<nodes.length; i++) {
			if (getProjectKey(nodes[i]) == key) {
				return nodes[i];
			}else{
				var match = getNodeByProjectKey(nodes[i].children, key);
				if (match) {
					return match;
				}
			}
		}
		return null;
	}
	
	$(SELECTOR).on("loaded.jstree", function(e, data) {
		if (loadedAction) {
			loadedAction();
		}
	});
	
	$(SELECTOR).bind("rename_node.jstree", function (e, data) {
    	var node = data.node;
    	var nodeID = getNodeID(node);
    	var text = data.text;
    	if (nodeID) {
    		
	    	if (editAction) editAction(node, nodeID, text);
	    	
    	}else if (createAction) {
    		
    		var parentNode = $(SELECTOR).jstree(true).get_node(data.node.parent);
    		var parentID = parentNode ? getNodeID(parentNode) : null;
    		var typeID = node.type ? node.type : null;
    		
    		createAction(node, text, typeID, parentID);
    	}
    });
	
	$(SELECTOR).on("select_node.jstree", function(e, data) {
		if (!data || !data.event || data.event.handleObj.type == 'contextmenu') {
			return;
		}
		var key = data.instance.get_node(data.node, true).children('a').data('key');		
		selectAction(data.node, key);
	});
	
	return {
		
		'callbackHandler': function(op, node, parent, pos, more) {
			if (op === "move_node" && more && more.core) {
				var nodeID = getNodeID(node);
				var parentID = getNodeID(parent);
				if (moveAction) moveAction(nodeID, parentID);
			}
			return true;
		},
	
		/**
		 * expands all nodes in treeview
		 */
		'expand': function() {
			$(SELECTOR).jstree(true).open_all();
		},
		
		/**
		 * collapses treeview
		 */
		'collapse': function() {
			$(SELECTOR).jstree(true).close_all();
		},
		
		/**
		 * Retrieves the nodes of the JsTree
		 */
		'getNodes': function() {
			return $(SELECTOR).data().jstree.get_json();
		},
		
		/**
		 * Retrieves a node in the JsTree from a project ID 
		 */
		'getNodeByProjectKey': function(key) {
			var nodes = this.getNodes();
			return getNodeByProjectKey(nodes, key);
		},
		
		/**
		 * Retrieves the node ID of a JsTree node
		 */
		'getNodeID': function(node) {
			return getNodeID(node);
		},
		
		/**
		 * Sets the node ID of a JsTree node
		 */
		'setNodeID': function(node, id) {
			node.a_attr['data-nodeID'] = id;
			return node;
		},
		
		/**
		 * Retrieves the project ID of a JsTree node
		 */
		'getProjectID': function(node) {
			return node && node.a_attr ? node.a_attr['data-projectID'] : null;
		},
		
		/**
		 * Retrieves the project key of a JsTree node
		 */
		'getProjectKey': function(node) {
			return getProjectKey(node);
		},
		
		/**
		 * Retrieves the requirement ID of a JsTree node
		 */
		'getRequirementID': function(node) {
			return node && node.a_attr ? node.a_attr['data-requirementID'] : null;
		},

		/**
		 * Retrieves the type of a node
		 */
		'getType': function(node) {
			return $(SELECTOR).jstree(true).get_type(node);
		},
		
		/**
		 * Changes the type of a node
		 */
		'setType': function(node, typeID) {
			$(SELECTOR).jstree(true).set_type(node, typeID);
		},
		
		/**
		 * Remove the node from the JsTree view
		 */
		'deleteNode': function(node) {
			$(SELECTOR).jstree(true).delete_node(node);
		},
		
		/**
		 * Edit the node in the JsTree view
		 */
		'editNode': function(node) {
			$(SELECTOR).jstree(true).edit(node);
		},
		
		/**
		 * Toggles a node in the JsTree view
		 */
		'toggleNode': function(node) {
			$(SELECTOR).jstree(true).select_node(node);
		},
		
		/**
		 * Hide a node in the JsTree view
		 */
		'hideNode': function(node) {
			$(SELECTOR).jstree(true).hide_node(node);
		},
		
		/**
		 * Hide all nodes in the JsTree view
		 */
		'hideAll': function() {
			$(SELECTOR).jstree(true).hide_all();
		},
		
		/**
		 * Show a specific node in the JsTree view
		 */
		'showNode': function(node) {
			$(SELECTOR).jstree(true).show_node(node);
		},
		
		/**
		 * Show all nodes in the JsTree view
		 */
		'showAll': function() {
			$(SELECTOR).jstree(true).show_all();
		},
		
		/**
		 * Insert a new folder in the JsTree
		 */
		'addFolder': function(typeID, parentNode) {
			var ID = this.getNodeID(parentNode);
			console.log('adding new element in node: ' + ID);
			var newNode = $(SELECTOR).jstree(true).create_node(parentNode);
			$(SELECTOR).jstree(true).set_type(newNode, typeID);
			$(SELECTOR).jstree(true).edit(newNode);
		},
		
		/**
		 * Registers a callback that will be triggered
		 * after editing a node in the JsTree
		 */
		'setEditAction': function(action) {
			editAction = action;
		},
		
		/**
		 * Registers a callback that will be triggered
		 * after creating a node in the JsTree
		 */
		'setCreateAction': function(action) {
			createAction = action;
		},
		
		/**
		 * Registers a callback that will be triggered
		 * after selecting a node in the JsTree
		 */
		'setSelectAction': function(action) {
			selectAction = action;
		},
		
		/**
		 * Registers a callback that will be triggered
		 * after moving a nove in the JsTree
		 */
		'setMoveAction': function(action) {
			moveAction = action;
		},
		
		/**
		 * Registers a callback that will be triggered
		 * when the JsTree will be fully loaded
		 */
		'setLoadedAction': function(action) {
			loadedAction = action;
		},
		
		/**
		 * Retrieves the node on which an event has been called
		 */
		'extractNode': function(data) {
			var inst = $.jstree.reference(data.reference);
		   return inst ? inst.get_node(data.reference) : null;
		},
		
		/**
		 * Search a keyword in the text of each node
		 */
		'search': function(kw) {
			$(SELECTOR).jstree(true).search(kw);
		}
	}
	
}
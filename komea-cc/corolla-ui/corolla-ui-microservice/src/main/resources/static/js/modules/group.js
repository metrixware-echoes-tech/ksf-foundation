
var SELECTED_USERS = '#selected-users';
var INPUT_USER_IDS = 'input[name=userIds]';

function initGroupsView() {
		
	$('.datatable').dataTable();
	
	$('.groupDelete').on('click', function(e) {

		e.preventDefault();
		var id = $(this).closest('tr').data('id');
		
		deleteModal.set('id', id);
		deleteModal.show();
		
	});
	
	deleteModal.onYesAction(function() {
		
		var id = deleteModal.get('id');
		
		restAPI.userGroups.delete(id, function(data, status) {
			consoleLog("User " + id + " removed")
			$('[data-id=' + id + ']').remove();
		});
		
		deleteModal.hide();
		
	});
	
}

function initGroupForm() {
	
	$('.pick-user').click(function() {
		addUser();
		return false;
	});
	
	bindButtons();
	
	/*
	 * Fix bug with the String list retrieved from thymeleaf
	 */
	var s = $(INPUT_USER_IDS).val();
	s = s
		.replace('[', '')
		.replace(']', '')
		.replace(/ +?/g, '');
	$(INPUT_USER_IDS).val(s);
	
}


function bindButtons() {
	
	$('.btnDelete').off('click');
	$('.btnDelete').click(function() {
		var tr = $(this).closest('tr');
		removeUserId(tr.data('id'));
		tr.remove();
		return false;
	});
	
}

function getUserIds() {
	
	var s = $(INPUT_USER_IDS).val();
	
	if (!s || s == '') {
		return [];
	}
	
	return s.split(',');
	
}


function addUserId(id) {
	
	var ids = getUserIds();
	ids.push(id);
	
	$(INPUT_USER_IDS).val(ids);		
	
}

function removeUserId(id) {
	
	var newIds = [];
	$.each(getUserIds(), function(i, v) {
		if (v != id) {
			newIds.push(v);
		}
	});

	$(INPUT_USER_IDS).val(newIds);	
}

function addUser() {
	
	selectUserModal.onSelect(function(user, line) {
		
		console.log(user);
		console.log(line);
		
		var login = user.login;

		if ($.inArray(login, getUserIds()) == -1) {
					
			var newLine = line.clone();
			
			while(newLine.find('td')[4]) {
				newLine.find('td')[4].remove();
			}
			
			newLine.append(
				'<td><a href="#" class="btn btn-danger btnDelete"><i class="fa fa-trash" /></a></td>'
			);
				
			$(SELECTED_USERS).append(newLine);
			
			addUserId(login);
			
			bindButtons();
			
		}
		
		selectUserModal.hide();
	});
	
	selectUserModal.show();
	
}
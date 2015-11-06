function roleTable() {
	$('#roles').dataTable();
	
	$("[data-toggle=tooltip]").tooltip();
	
	$('button.roleDelete').on('click', function(e) {
		
		e.preventDefault();
		var id = $(this).closest('tr').data('id');
		
		deleteModal.set('id', id);
		deleteModal.show();
		
	});
	
	deleteModal.onYesAction(function() {
		
		var id = deleteModal.get('id');
		
		restAPI.roles.delete(id, function(data, status) {
			consoleLog("Role " + id + " removed")
			$('[data-id=' + id + ']').remove();
		});
		
		deleteModal.hide();
	});

}

function initFormRoleEdit() {
	$(document).ready(function() {
		
		$('#form').parsley();
		
	});
	
}
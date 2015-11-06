
function initItemsView() {
	
	//$('#items-table').dataTable();
	
	$("[data-toggle=tooltip]").tooltip();
	
	$('.itemDelete').on('click', function(e) {

		e.preventDefault();
		var id = $(this).closest('tr').data('id');

		deleteModal.set('id', id);
		deleteModal.show();
		
	});
	
	deleteModal.onYesAction(function() {
		
		var id = deleteModal.get('id');
		
		/*restAPI.roles.delete(id, function(data, status) {
			consoleLog("Role " + id + " removed")
			$('[data-id=' + id + ']').remove();
		});*/
		
		consoleLog('deleting item '+id);		
		deleteModal.hide();
		
	});

}

function initFormRoleEdit() {
		
	$('#form').parsley();
	
}
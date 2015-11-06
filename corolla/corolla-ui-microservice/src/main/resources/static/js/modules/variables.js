
/**
 * Initialize Variables View
 */
function initVarTable() {

	$('#variables').dataTable();
	
	$("[data-toggle=tooltip]").tooltip();
	
	$('button.userDelete').on('click', function(e) {

		e.preventDefault();
		var id = $(this).closest('tr').data('id');
		
		deleteModal.set('id', id);
		deleteModal.show();
		
	});
	
	deleteModal.onYesAction(function() {
		
		var id = deleteModal.get('id');
		
		restAPI.users.delete(id, function(data, status) {
			consoleLog("User " + id + " removed")
			$('[data-id=' + id + ']').remove();
		});
		
		deleteModal.hide();
		
	});
	
	/*
	 * Call modal to quickly change project
	 */
	$('.toggle-change-project').click(function() {
		changeProjectModal.onSelect(function(key) {
			document.location = '/ui/variables/'+key
		});
		changeProjectModal.show();
	});
	
}
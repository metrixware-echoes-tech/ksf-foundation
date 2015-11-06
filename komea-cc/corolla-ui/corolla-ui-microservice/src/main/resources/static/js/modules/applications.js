function applicationTable() {

	$('#users').dataTable();
	
	$("[data-toggle=tooltip]").tooltip();
	
	$('button.appDelete').on('click', function(e) {
		
		e.preventDefault();
		var id = $(this).closest('tr').data('id');
		
		deleteModal.set('id', id);
		
		deleteModal.show();

	});
	
	deleteModal.onYesAction(function() {
		
		var id = deleteModal.get('id');
		
		restAPI.applications.delete(id, function(data, status) {
			consoleLog("Application "+id+" removed");
			$('[data-id='+id+']').remove();
		});
		
		deleteModal.hide();
		
	});
}
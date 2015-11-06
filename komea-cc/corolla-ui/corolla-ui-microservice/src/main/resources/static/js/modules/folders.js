
function initFolderView() {
	
	$("[data-toggle=tooltip]").tooltip();
	
	$('.itemDelete').on('click', function(e) {

		e.preventDefault();
		var id = $(this).closest('tr').data('id');

		deleteModal.set('id', id);
		deleteModal.show();
		
	});
	
	deleteModal.onYesAction(function() {
		
		var id = deleteModal.get('id');
		
		restAPI.folderNodeTypes.delete(id, function(data, status) {
			consoleLog("Folder type " + id + " removed")
			$('[data-id=' + id + ']').remove();
		});
				
		deleteModal.hide();
		
	});

}
function userTable() {

	$('#users').dataTable();
	
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
	
}

var rand = function() {
	return Math.random().toString(36).substr(2); // remove `0.`
};

var token = function() {
	return rand() + rand() + rand(); // to make it longer
};

function initFormUserEdit() {
	$(document).ready(function() {
		
		$('#generateTokenButton').click(function(){
			var tokenText = token();
		    $('#activationToken').val(tokenText);
		    
		});
		
		$('#form').parsley();
		
	});
	
}
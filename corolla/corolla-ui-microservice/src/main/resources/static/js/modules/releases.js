/**
 * Initialize releases view
 */
function initReleasesView() {
	
	/*
	 * Initialize Bootstrap WYSIHTML5 widget
	 */
	$(".textarea").wysihtml5();

	/*
	 * Initialize DatePicker widget
	 */
	$('.datepicker').datepicker()
	
	/*
	 * Call modal to quickly change project
	 */
	$('.toggle-change-project').click(function() {
		changeProjectModal.onSelect(function(key) {
			document.location = '/ui/releases/'+key
		});
		changeProjectModal.show();
	});
	
}
var TILES_CONTAINER = '#tiles-container';

/**
 * Initialize tiles grid
 */
function initTiles() {

	/**
	 * Initialize tiles container
	 */
	var $container = $('.isotope').isotope({
		itemSelector : '.element-item',
		layoutMode : 'fitRows',
		getSortData : {
			name : '.name',
			symbol : '.symbol',
			number : '.number parseInt',
			category : '[data-category]',
			weight : function(itemElem) {
				var weight = $(itemElem).find('.weight').text();
				return parseFloat(weight.replace(/[\(\)]/g, ''));
			}
		}
	});

	/**
	 * Filter by tags
	 */
	$('.toolbar').on('click', 'span', function() {
		$('.toolbar span').removeClass('active');
		$(this).addClass('active');
		var filterValue;
		if ($(this).data('filter')) {
			filterValue = $(this).data('filter');
		} else {
			filterValue = $(this).text();
		}
		filterValue = filterValue.toLowerCase();
		var filterFn = function() {
			var tags = $(this).data('tags');
			if (tags) {
				tags = tags.toLowerCase();
				return (tags.indexOf(filterValue) > -1);
			}
			return false;
		}
		$container.isotope({
			filter : filterValue == '*' ? filterValue : filterFn
		});
	});

	/**
	 * Handle submit event in search bar
	 */
	$('.search-bar').submit(function() {
		searchTiles($(this).find('#query').val());
		return false;
	});

	/**
	 * Update the displayed items when a key is pressed in the search bar
	 */
	$('.search-bar #query').on('input', function() {
		searchTiles($(this).val());
	});

	/**
	 * Search through the tiles
	 */
	function searchTiles(q) {
		var filterFn;
		$('.toolbar span').removeClass('active');
		$('.toolbar span').first().addClass('active');
		if (q && q != '') {
			q = q.toLowerCase();
			filterFn = function() {
				var name = $(this).find('.name').text();
				if (name) {
					name = name.toLowerCase();
					return (name.indexOf(q) > -1);
				}
			}
		} else {
			filterFn = '*';
		}
		$container.isotope({
			filter : filterFn
		});
	}

	/**
	 * Reorganize the items when the menu is being closed/opened
	 */
	$('.content').resize(function() {
		$container.isotope('layout');
	});

	/**
	 * Handle click event on a tile
	 */
	$('.element-item').click(function() {
		var url = $(this).data('url');
		if (url) {
			window.location = url;
		}
	});

}
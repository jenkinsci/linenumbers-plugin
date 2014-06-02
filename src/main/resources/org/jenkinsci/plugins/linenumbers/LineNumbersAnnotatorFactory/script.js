Behaviour.specify('div.fold', 'fold.toggle', 10, function(element) {
	element.onclick = function() {
		element.toggleClassName('open');
	}
});
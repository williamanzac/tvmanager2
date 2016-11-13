(function () {
	ko.treeview = {
		viewmodel: function(configuration) {
			this.data = configuration.data || ko.observableArray([]);
			this.selectedNodes = ko.observableArray([]);
			this.showBorder = configuration.showBorder;
			this.onhoverColor = configuration.onhoverColor;
			this.selectedBackColor = configuration.selectedBackColor;
			this.updateFunction = configuration.updateFunction;
		}
	};

	ko.bindingHandlers.treeview = {
		init: function(element, valueAccessor, allBindings, viewModel, bindingContext) {
			var value = valueAccessor();
			$(element).treeview({
				data: ko.unwrap(value.data),
				showBorder: ko.unwrap(value.showBorder),
				onhoverColor: ko.unwrap(value.onhoverColor),
				selectedBackColor: ko.unwrap(value.selectedBackColor)
			});
		},
		update: function(element, valueAccessor, allBindings, viewModel, bindingContext) {
			var value = valueAccessor();
			$(element).treeview({
				data: ko.unwrap(value.data),
				showBorder: ko.unwrap(value.showBorder),
				onhoverColor: ko.unwrap(value.onhoverColor),
				selectedBackColor: ko.unwrap(value.selectedBackColor),
				onNodeSelected: function(event, node) {
					var selected = $(element).treeview("getSelected");
					value.selectedNodes(selected);
					if (value.updateFunction) {
						value.updateFunction($(element),value.selectedNodes());
					}
				},
				onNodeUnselected: function(event, node) {
					var selected = $(element).treeview("getSelected");
					value.selectedNodes(selected);
					if (value.updateFunction) {
						value.updateFunction($(element),value.selectedNodes());
					}
				}
			});
		}
	};
})();

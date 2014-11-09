/**
 * display business data as a popover
 */
var formModule = angular.module('blossom.form', [ 'ngRoute', 'ui.bootstrap' ]);

formModule.factory('PopFormFactory', [ '$modal', function($modal) {

	var popForm = {};
	popForm.call = function(id) {
		console.log("generating pop up for: " + id);
		$modal.open({
			templateUrl : 'popform.html',
			controller : 'PopFormCtrl',
			resolve : {
				items : function() {
					return id;
				}
			}
		});
	}
	return popForm;
} ]);

formModule.factory('BusinessFormFactory', [ '$resource', function($resource) {

	var urlBase = './rest/character';
	var businessFormManager = new Object();
	businessFormManager.Character = $resource(urlBase + '/:id', {
		id : '@id'
	});

	return businessFormManager;
} ]);

formModule.controller('PopFormCtrl', function($scope, $modal, $modalInstance, BusinessFormFactory, items) {
	console.log("controller has been loaded " + items);
	$scope.modalmodel = {};
	BusinessFormFactory.Character.get({
		id : items
	}, function(d) {
		console.log(d);
		console.log("id: " + d.id);
		$scope.modalmodel = d;
		$scope.$apply;
	});
});
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

	var urlBase = './rest/table';
	var businessFormManager = new Object();
	businessFormManager.AbstractBlossomEntity = $resource(urlBase + '/:id', {
		id : '@id'
	});

	return businessFormManager;
} ]);

formModule.controller('PopFormCtrl', function($scope, $modal, $modalInstance, BusinessFormFactory, items) {
	console.log("controller has been loaded " + items);
	$scope.modalmodel = {};
	$scope.modalmodel.presentation = {};
	$scope.modalmodel.presentation.modaltitle = "Object properties";
	$scope.modalmodel.presentation.paneltitle = "Details";
	$scope.modalmodel.businesscontent = {};
	$scope.$apply;
	$scope.close = function()
	{
		console.log("closing");
		$modalInstance.close();
	}
	BusinessFormFactory.AbstractBlossomEntity.get({
		id : items
	}, function(d) {
		console.log(d);
		console.log("id: " + d.id);
		$scope.modalmodel.businesscontent = d;
		$scope.$apply;
	});
});
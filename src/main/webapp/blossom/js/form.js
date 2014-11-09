/**
 * display business data as a popover
 */
var module = angular.module('blossom.form', [ 'ngRoute' ]);
module.factory('PopFormFactory', function() {
	var popform = {};
	popform.on = false;

	popform.toggle = function(onoroff) {
		popform.on = onoroff;
	}
	popform.isOn = function() {
		return popform.on;
	}

	return popform;
});
module.controller('PopFormCtrl', function($scope) {
	$scope.test = 'test';
	$scope.isformdisplayed = false;
});
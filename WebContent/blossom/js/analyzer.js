/**
 * simple angularjs controller for a text analyzer poc
 */

var analyzermodule = angular.module('blossom.analyzer', [ 'ngRoute', 'ngResource', 'ngSanitize', 'blossom.misc' ]);

analyzermodule.factory('AnnotationFactory', [ '$resource', function($resource) {

	var urlBase = './rest/annotation';
	var annotationManager = new Object();
	annotationManager.Document = $resource(urlBase + '/:docid', {
		docId : '@id'
	});

	return annotationManager;
} ]);

analyzermodule.controller('AnalyzerCtrl', function($scope, $http, AnnotationFactory) {
	var navbarul = d3.selectAll('ul#navbarul>li');
	navbarul.attr("class", null);
	d3.select('#analyzerNavItem').attr("class", "active");
	$scope.doc = {};

	var selection = "";

	AnnotationFactory.Document.get({
		docid : 'test'
	}, function(d) {
		$scope.doc.text = d.content;
	});

	$scope.setClick = function(option) {
		selection = option;
	}
	$scope.submitFormNode = function() {
		switch (selection) {
		case "Edit":
			console.log("Edit");
			break;
		case "todo":
			break;
		}
	}
	$scope.onChange = function() {
		console.log("change");
	}

})
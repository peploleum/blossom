/**
 * simple angularjs controller for a text analyzer poc
 */

var analyzermodule = angular.module('blossom.analyzer', [ 'ngRoute', 'ngResource', 'blossom.misc' ]);

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
	AnnotationFactory.Document.get({
		docid : 'test'
	}, function(d) {
		console.log('what is this callback for ?' + d.content);
		$scope.doc.text = d.content;
	});
	// $scope.doc.text = blossomDocument.content;

})
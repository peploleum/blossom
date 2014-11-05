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

analyzermodule.controller('AnalyzerCtrl', function($scope, $sce, $http, AnnotationFactory, uuidFactory) {
	var navbarul = d3.selectAll('ul#navbarul>li');
	navbarul.attr("class", null);
	d3.select('#analyzerNavItem').attr("class", "active");
	$scope.doc = {};
	$scope.isTagging = false;
	$scope.analyzerselection = {};

	var selection = "";

	AnnotationFactory.Document.get({
		docid : 'test'
	}, function(d) {

		// $scope.doc.text = d.content;
		$scope.doc.text = $sce.trustAsHtml(d.decoratedContent);
	});

	$scope.setClick = function(option) {
		selection = option;
	}
	$scope.setCategory = function(option) {
		$scope.analyzerselection.category = option;
	}

	$scope.submitAnalyzerAction = function() {
		switch (selection) {
		case "Edit":
			$scope.isTagging = !$scope.isTagging;
			console.log("Tagging is now " + $scope.isTagging);
			if (!$scope.isTagging) {
				$scope.analyzerselection.text = '';
			}
			break;
		case "Commit":
			$scope.analyzerselection.id = uuidFactory.generateUUID();
			console.log("Committing " + $scope.analyzerselection.text + " id " + $scope.analyzerselection.id);
			AnnotationFactory.Document.save({
				docid : 'test'
			}, $scope.analyzerselection, function(d) {
				console.log('received new content');
				$scope.doc.text = $sce.trustAsHtml(d.decoratedContent)
			});
			break;
		}
	}
	$scope.onChange = function() {
		console.log("change");
	}
	document.getElementById
	$scope.onDivClick = function($event) {
		var selectedText = "" + document.getSelection();
		console.log(document.getSelection().getRangeAt(0));
		var offsetValue = parseInt(document.getSelection().focusNode.parentElement.attributes.offset.value);
		console.log("offset value: " + offsetValue);
		console.log("$event target: " + $event.target);
		$scope.analyzerselection.text = selectedText;
		$scope.analyzerselection.startOffset = (document.getSelection().getRangeAt(0).startOffset + offsetValue);
		console.log("end: " + document.getSelection().getRangeAt(0).endOffset);
		$scope.analyzerselection.endOffset = (document.getSelection().getRangeAt(0).endOffset + offsetValue);
	}

})
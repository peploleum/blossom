/**
 * simple angularjs controller for a text analyzer poc
 */

var analyzermodule = angular.module('blossom.analyzer', [ 'ngRoute', 'ngResource', 'blossom.misc' ]);

analyzermodule.controller('AnalyzerCtrl', function($scope, $http) {
	var navbarul = d3.selectAll('ul#navbarul>li');
	navbarul.attr("class", null);
	d3.select('#analyzerNavItem').attr("class", "active");
	
	
})
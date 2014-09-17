var module = angular.module('blossom.home', [ 'ngRoute' ])

module.controller('HomeCtrl', function($scope, $http, $templateCache) {
	var navbarul = d3.selectAll('ul#navbarul>li');
	navbarul.attr("class", null);
	d3.select('#homeNavItem').attr("class", "active");
	$scope.message = 'Blossom';
})

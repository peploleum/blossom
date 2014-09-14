var module = angular.module('blossom.home', [ 'ngRoute' ])

module.controller('HomeCtrl', function($scope, $http, $templateCache) {
	$scope.message = 'Blossom';
})

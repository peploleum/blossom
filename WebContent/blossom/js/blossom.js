var blossomModule = angular.module('blossom', [ 'ngRoute', 'blossom.home', 'blossom.search', 'blossom.network', 'blossom.map', 'blossom.stats', 'blossom.misc' ]);

blossomModule.config(function($routeProvider) {
	$routeProvider.when('/about', {
		controller : 'AboutCtrl',
		templateUrl : 'about.html'
	}).when('/home', {
		controller : 'HomeCtrl',
		templateUrl : 'home.html'
	}).when('/contact', {
		controller : 'ContactCtrl',
		templateUrl : 'contact.html'
	}).when('/network', {
		controller : 'NetworkCtrl',
		templateUrl : 'network.html'
	}).when('/map', {
		controller : 'MapCtrl',
		templateUrl : 'map.html'
	}).when('/search', {
		controller : 'SearchCtrl',
		templateUrl : 'search.html'
	}).when('/stat', {
		controller : 'StatCtrl',
		templateUrl : 'stats.html'
	}).otherwise({
		redirectTo : '/home'
	});
})

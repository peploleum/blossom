var blossomModule = angular.module('blossom', [ 'ngRoute', 'blossom.home', 'blossom.search', 'blossom.network', 'blossom.misc' ]);

blossomModule.factory('Projects', function() {
	return [ {
		id : 0,
		name : "a",
		description : "adesc",
		site : "http://A",
		saved : true
	}, {
		id : 1,
		name : "b",
		description : "bdesc",
		site : "http://b",
		saved : true
	} ];
})

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
	}).when('/search', {
		controller : 'SearchCtrl',
		templateUrl : 'search.html'
	}).otherwise({
		redirectTo : '/home'
	});
})

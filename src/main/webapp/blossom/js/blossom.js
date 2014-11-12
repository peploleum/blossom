var blossomModule = angular.module('blossom', [ 'ngRoute', 'blossom.home', 'blossom.search', 'blossom.network', 'blossom.map', 'blossom.stats', 'blossom.analyzer', 'blossom.misc', 'blossom.mission' ]);

blossomModule.config(function($routeProvider, $locationProvider) {
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
	}).when('/mission', {
		controller : 'MissionCtrl',
		templateUrl : 'mission.html'
	}).when('/search', {
		controller : 'SearchCtrl',
		templateUrl : 'search.html'
	}).when('/stat', {
		controller : 'StatCtrl',
		templateUrl : 'stats.html'
	}).when('/analyzer', {
		controller : 'AnalyzerCtrl',
		templateUrl : 'analyzer.html'
	}).when('/popform/:formId', {
		controller : 'PopFormCtrl',
		templateUrl : 'popform.html'
	}).otherwise({
		redirectTo : '/home'
	});
	// $locationProvider.html5Mode(true);
})

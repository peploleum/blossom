var appMod = angular.module('app', [ 'ngRoute' ]);

appMod.config(function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : "app.html",
		controller : "AppCtrl"
	})
});

appMod.controller('AppCtrl', function($scope) {
	$scope.model = {
		message : "wesh wesh canne a peche"
	}
})
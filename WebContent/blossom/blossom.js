angular.module('blossom', [ 'ngRoute' ])

// .factory('Projects', function() {
// return [ {
// id : 0,
// name : "a",
// description : "a",
// site : "http://A",
// saved : true
// }, {
// id : 1,
// name : "b",
// description : "b",
// site : "http://b",
// saved : true
// } ];
// })

.config(function($routeProvider) {
	$routeProvider.when('/about', {
		controller : 'AboutCtrl',
		templateUrl : 'about.html'
	}).when('/home', {
		controller : 'MainCtrl',
		templateUrl : 'home.html'
	}).when('/contact', {
		controller : 'ContactCtrl',
		templateUrl : 'contact.html'
	}).otherwise({
		redirectTo : '/'
	});
})

.controller('MainCtrl', function($scope) {
	$scope.message = "toubidou";
})

.controller('AboutCtrl', function($scope) {
	$scope.message = "toubidou";
})

.controller('ContactCtrl', function($scope) {
	$scope.message = "toubidou";
})

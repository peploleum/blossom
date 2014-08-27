angular
		.module('blossom', [ 'ngRoute' ])

		.factory('Projects', function() {
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
			}).when('/search', {
				controller : 'SearchCtrl',
				templateUrl : 'search.html'
			}).otherwise({
				redirectTo : '/'
			});
		})

		.controller('MainCtrl', function($scope) {
			$scope.message = 'toubidou';
		})

		.controller('AboutCtrl', function($scope) {
			$scope.message = 'toubidou';
		})

		.controller('ContactCtrl', function($scope) {
			$scope.message = 'toubidou';
		})

		.controller(
				'SearchCtrl',
				function($scope, $http, $templateCache, Projects) {
					// $scope.message = Projects[1];
					$scope.results = Projects;

					$scope.message = $scope.text;
					$scope.submit = function() {
						$http(
								{
									method : 'GET',
									url : 'http://localhost:8081/solr/collection1/select?wt=json&q=a',
									cache : $templateCache,
								}).success(function(data, status) {
							// $scope.numfound = data.response.numFound;
							$scope.message = "youpi"
						}).error(function(data, status) {
							$scope.numfound = 0;
							$scope.message = "kop"
						});

					}
					function on_data(data) {
						$scope.message = data.response.numFound;
					}

				})

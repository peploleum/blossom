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
				function($scope, $http, $templateCache) {
					$scope.results = [];
					$scope.submit = function() {
						$http(
								{
									url : 'http://localhost:8081/solr/collection1/select?wt=json&indent=true&q='
											+ $scope.text,
									cache : $templateCache,
								})
								.success(function(data, status) {
									$scope.numfound = data.response.numFound;
									$scope.solrresults = data;
								})
								.error(function(data, status) {
									$scope.numfound = 0;
									$scope.message = "fail"
								})
								.then(
										function() {
											$scope.results = [];
											var localResults = $scope.solrresults.response.docs;
											$scope.message = localResults[0].id;
											localResults
													.forEach(function(element) {
														$scope.results
																.push({
																	id : element.id,
																	name : element.name,
																	manu : element.manu,
																	manu_id : element.manu_id_s,
																	price : element.price_c,
																	saved : true
																})
													})
										});
					}
				})

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
				redirectTo : '/home'
			});
		})

		.controller(
				'MainCtrl',
				function($scope) {
					//a d3js bit here
					var data = [ 4, 8, 15, 16, 23, 42, 51 ];

					var x = d3.scale.linear().domain([ 0, d3.max(data) ])
							.range([ 0, 420 ]);

					d3.select(".chart").selectAll("div").data(data).enter()
							.append("div").style("width", function(d) {
								return x(d) + "px";
							}).text(function(d) {
								return d;
							});
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

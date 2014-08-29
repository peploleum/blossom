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
					// a d3js bit here
					var data = [ 4, 8, 15, 16, 23, 42 ];

					// var x = d3.scale.linear().domain([ 0, d3.max(data) ])
					// .range([ 0, 420 ]);
					//
					// d3.select(".chart").selectAll("div").data(data).enter()
					// .append("div").style("width", function(d) {
					// return x(d) + "px";
					// }).text(function(d) {
					// return d;
					// });

					var width = 420, barHeight = 20;

					var xsvg = d3.scale.linear().domain([ 0, d3.max(data) ])
							.range([ 0, width ]);

					var chart = d3.select(".chartsvg").attr("width", width)
							.attr("height", barHeight * data.length);

					var bar = chart.selectAll("g").data(data).enter().append(
							"g").attr("transform", function(d, i) {
						return "translate(0," + i * barHeight + ")";
					});

					bar.append("rect").attr("width", xsvg).attr("height",
							barHeight - 1);

					bar.append("text").attr("x", function(d) {
						return xsvg(d) - 3;
					}).attr("y", barHeight / 2).attr("dy", ".35em").text(
							function(d) {
								return d;
							});

					var graph = d3.select(".graphsvg").attr("width", width)
							.attr("height", width);
					graph.append("circle").attr("cx", 90).attr("cy", 90).attr(
							"r", 30).style("fill", "red");
					graph.append("circle").attr("cx", 180).attr("cy", 90).attr(
							"r", 30).style("fill", "blue");

					d3.json("data/data.json", function(json) {
						$scope.message = "wesh wesh canne à pêche";
						graph.append("circle").attr("cx", 180).attr("cy", 180)
								.attr("r", 30).style("fill", "yellow");
						graph.append("text").attr("x", 180).attr("y", 180)
								.text("text", "testtext").attr("fill", "red");
					})

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
					$scope.solrerror = false;
					$scope.solrsuccess = true;
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
									$scope.solrerror = false;
									$scope.solrsuccess = true;
								})
								.error(function(data, status) {
									$scope.numfound = 0;
									$scope.solrerror = true;
									$scope.solrsuccess = false;
								})
								.then(
										function() {
											$scope.results = [];
											var localResults = $scope.solrresults.response.docs;
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

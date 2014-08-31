var module = angular.module('blossom.search', [ 'ngRoute' ])

module.controller('SearchCtrl', function($scope, $http, $templateCache) {
	$scope.results = [];
	$scope.solrerror = false;
	$scope.solrsuccess = true;
	$scope.submit = function() {
		$http({
			url : 'http://localhost:8081/solr/collection1/select?wt=json&indent=true&q=' + $scope.text,
			cache : $templateCache,
		}).success(function(data, status) {
			$scope.numfound = data.response.numFound;
			$scope.solrresults = data;
			$scope.solrerror = false;
			$scope.solrsuccess = true;
		}).error(function(data, status) {
			$scope.numfound = 0;
			$scope.solrerror = true;
			$scope.solrsuccess = false;
		}).then(function() {
			$scope.results = [];
			var localResults = $scope.solrresults.response.docs;
			localResults.forEach(function(element) {
				$scope.results.push({
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




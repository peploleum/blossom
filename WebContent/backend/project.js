angular.module('project', [ 'ngRoute' ])

.factory('Projects', function() {
	return [ {
		id : 0,
		name : "a",
		description : "a",
		site : "http://A",
		saved : true
	}, {
		id : 1,
		name : "b",
		description : "b",
		site : "http://b",
		saved : true
	} ];
})

.config(function($routeProvider) {
	$routeProvider.when('/', {
		controller : 'ListCtrl',
		templateUrl : 'list.html'
	}).when('/edit/:projectId', {
		controller : 'EditCtrl',
		templateUrl : 'detail.html'
	}).when('/new', {
		controller : 'CreateCtrl',
		templateUrl : 'detail.html'
	}).otherwise({
		redirectTo : '/'
	});
})

.controller('ListCtrl', function($scope, Projects) {
	$scope.projects = Projects;
})

.controller('CreateCtrl', function($scope, $location, $timeout, Projects) {
	$scope.save = function() {
		Projects.push($scope.project);
		$scope.project.saved = true;
		$scope.project.id = Projects.length;
		$location.path('/');
	};
})

.controller('EditCtrl', function($scope, $location, $routeParams, Projects) {
	var projectId = $routeParams.projectId, projectIndex;
	$scope.projects = Projects;
	var element;
	var index;
	for (var i = 0; i < $scope.projects.length; i++) {
		if ($scope.projects[i].id == projectId) {
			element = $scope.projects[i];
			index = i;
		}
	}
	// $scope.message = element;
	$scope.project = element;
	$scope.destroy = function() {
		$scope.projects.splice(index, 1);
		$location.path('/');
	};

	$scope.save = function() {
		$scope.projects.splice(index, 1, $scope.project);
		$location.path('/');
	};
});
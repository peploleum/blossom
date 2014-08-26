angular.module('ngRouteExample', [ 'ngRoute' ])

// .controller('MainController',
// function($scope, $route, $routeParams, $location) {
// $scope.$route = $route;
// $scope.$location = $location;
// $scope.$routeParams = $routeParams;
// })
.controller('MainController', function($scope, $route, $location) {
	$scope.$route = $route;
	$scope.$location = $location;
})

// .controller('BookController', function($scope, $routeParams) {
// $scope.name = "BookController";
// $scope.params = $routeParams;
// })
.controller('BookController', function($scope) {
	$scope.name = "BookController";
	// $scope.params = $routeParams;
})

.controller('ChapterController', function($scope, $routeParams) {
	$scope.name = "ChapterController";
	$scope.params = $routeParams;
})

.config(function($routeProvider) {
	$routeProvider.when('/Book/:bookId', {
		// templateUrl :
		// 'file:///C:/dev/workspace/blossom/WebContent/route/book.html',
		templateUrl : 'test/book.html',
		controller : 'BookController',
		resolve : {
			// I will cause a 1 second delay
			delay : function($q, $timeout) {
				var delay = $q.defer();
				$timeout(delay.resolve, 1000);
				return delay.promise;
			}
		}
	});

	// .when('/Book/:bookId/ch/:chapterId', {
	// templateUrl : 'chapter.html',
	// controller : 'ChapterController'
	// });

	// configure html5 to get links working on jsfiddle
	// $locationProvider.html5Mode(true);
});
// .config(function($routeProvider, $locationProvider) {
// $routeProvider.when('/Book/:bookId', {
// templateUrl : 'book.html',
// controller : 'BookController',
// resolve : {
// // I will cause a 1 second delay
// delay : function($q, $timeout) {
// var delay = $q.defer();
// $timeout(delay.resolve, 1000);
// return delay.promise;
// }
// }
// }).when('/Book/:bookId/ch/:chapterId', {
// templateUrl : 'chapter.html',
// controller : 'ChapterController'
// });
//	
// // configure html5 to get links working on jsfiddle
// // $locationProvider.html5Mode(true);
// });

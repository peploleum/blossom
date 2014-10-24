/**
 * handle table model loading from Server
 */
var blossomtable = [];
blossomtable.module = angular.module('blossom.table', [ 'ngRoute', 'ngResource' ]);
blossomtable.module.factory('TableFactory', [ '$http', function($http) {

	var urlBase = './rest/table';
	var tableFactory = {};

	tableFactory.getData = function() {
		return $http.get(urlBase + '/' + 'getdata');
	};

	return tableFactory;
} ]);
blossomtable.module.factory('TableGetFactory', function(TableFactory) {
	var tableGetFactory = {};
	tableGetFactory.getData = function() {
		return TableFactory.getData();
	}
	return tableGetFactory;
})

blossomtable.loadTable = function($scope, TableGetFactory) {
	$scope.tablerows = TableGetFactory.getData().success(function(data, status, headers, config) {
		console.log("ok: " + data + " rows " + data.rows);
		$scope.tablerows = data.rows;
	}).error(function(data, status, headers, config) {
		console.log("ko");
	});
}
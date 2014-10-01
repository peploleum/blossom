/**
 * handle table model loading from Server
 */
var bt = [];
bt.module = angular.module('blossom.table', [ 'ngRoute', 'ngResource' ]);
bt.module.factory('TableFactory', [ '$http', function($http) {

	var urlBase = './rest/table';
	var tableFactory = {};

	tableFactory.getData = function() {
		return $http.get(urlBase + '/' + 'getdata');
	};

	return tableFactory;
} ]);
bt.module.factory('TableGetFactory', function(TableFactory) {
	var tableGetFactory = {};
	tableGetFactory.getData = function() {
		return TableFactory.getData();
	}
	return tableGetFactory;
})
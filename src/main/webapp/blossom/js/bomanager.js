/**
 * manage BOs
 */

var module = angular.module('blossom.bomanager', [ 'ngRoute' ]);

module.factory('boManagerFactory', [ '$http', function($http) {

	var urlBase = './rest/character';
	var boManager = {};

	boManager.getCharacterById = function(id) {
		return $http.get(urlBase, id);
	};

	boManager.createCharacter = function(character) {
		return $http.post(urlBase, character);
	};

	boManager.updateCharacter = function(character) {
		return $http.put(urlBase, character);
	}

	boManager.deleteCharacter = function(id) {
		return $http['delete'](urlBase, id);
	}
	return boManager;
} ]);
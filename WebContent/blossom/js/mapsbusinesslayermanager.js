/**
 * We use a dedicated class to handle WebSocket for business layer updates
 */

var module = angular.module('blossom.maps.businesslayer', [ 'ngRoute' ]);

// access WebService to CRUD geolocalized business objects
module.factory('refresherFactory', [ '$http', function($http) {

	var urlBase = './maps/businesslayerupdates';
	var refresherFactory = {};
	var websocket = new WebSocket(urlBase);
	websocket.onopen = function(event) {
		websocket.send('maps module connected');
	};

	websocket.onmessage = function(event) {
		console.log(event.data);
	};

	refresherFactory.onOpen = function() {
		return $http.get(urlBase + '/' + 'getgeoentity');
	};

	refresherFactory.addFeature = function(feature) {
		return $http.put(urlBase + '/' + 'addfeature', feature);
	};

	return refresherFactory;
} ]);

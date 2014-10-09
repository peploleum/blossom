/**
 * We use a dedicated class to handle WebSocket for business layer updates
 */

var module = angular.module('blossom.maps.businesslayer', [ 'ngRoute', 'ng' ]);

// access WebService to CRUD geolocalized business objects
module.factory('refresherFactory', [ '$http', '$location', function($http, $location) {

	var urlBase = 'ws://' + $location.host() + ':' + $location.port() + '/Blossom' + '/maps/businesslayerupdates';
	var refresherFactory = {};
	var websocket = new WebSocket(urlBase);
	websocket.onopen = function(event) {
		websocket.send('maps module connected');
	};

	websocket.onmessage = function(event) {
		console.log("received: " + event.data);
		try {
			var receivedFeature = JSON.parse(event.data);
			console.log(receivedFeature.type);
		} catch (e) {
			console.log("we failed to parse JSON websocket payload");
		}
	};
	// websocket.send("salut");

	refresherFactory.onOpen = function() {
		return $http.get(urlBase + '/' + 'getgeoentity');
	};

	refresherFactory.addFeature = function(feature) {
		return $http.put(urlBase + '/' + 'addfeature', feature);
	};

	return refresherFactory;
} ]);

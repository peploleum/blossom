/**
 * We use a dedicated class to handle WebSocket for business layer updates
 */

var module = angular.module('blossom.maps.businesslayer', [ 'ngRoute', 'ng' ]);

// access the websocket to be notified of geolocalized business objects on the
// server side.
module.factory('refresherFactory', [ '$http', '$location', function($http, $location) {

	var urlBase = 'ws://' + $location.host() + ':' + $location.port() + '/Blossom' + '/maps/businesslayerupdates';
	var refresherFactory = {};
	var websocket = new WebSocket(urlBase);
	websocket.onopen = function(event) {
		websocket.send('maps module connected');
	};

	refresherFactory.webSocket = websocket;

	return refresherFactory;
} ]);
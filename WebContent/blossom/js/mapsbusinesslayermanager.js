/**
 * We use a dedicated class to handle WebSocket for business layer updates
 */

var module = angular.module('blossom.maps.businesslayer', [ 'ngRoute', 'ng' ]);

// access the websocket to be notified of geolocalized business objects on the
// server side.
module.factory('refresherFactory', [ '$http', '$location', function($http, $location) {

	// must look at the $resource management to maybe write something simpler.
	var urlBase = 'ws://' + $location.host() + ':' + $location.port() + '/' + $location.absUrl().split('/')[3] + '/maps/businesslayerupdates';
	var refresherFactory = {};
	var websocket = new WebSocket(urlBase);
	websocket.onopen = function(event) {
		websocket.send('maps module connected');
	};

	refresherFactory.webSocket = websocket;

	return refresherFactory;
} ]);

// access WebService to CRUD geolocalized business objects
module.factory('geoFactory', [ '$http', function($http) {

	var rootUrl = './rest'
	var urlBase = './rest/geo';
	var geoFactory = {};

	geoFactory.getGeoEntity = function() {
		return $http.get(urlBase + '/' + 'getgeoentity');
	};

	geoFactory.addFeature = function(feature) {
		return $http.put(urlBase + '/' + 'addfeature', feature);
	};

	geoFactory.saveFeatures = function(features) {
		return $http.put(urlBase + '/' + "savefeatures");
	}
	
	geoFactory.query = function(extent) {
		return $http.post(rootUrl + '/geoquery', extent);
	}
	return geoFactory;
} ]);

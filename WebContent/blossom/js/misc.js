var module = angular.module('blossom.misc', [ 'ngRoute' ]);

module.factory('uuidFactory', [ function() {
	var uuidGen = {};

	uuidGen.generateUUID = function() {
		return ('xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
			var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
			return v.toString(16);
		}));
	}
	return uuidGen;
} ])
module.controller('AboutCtrl', function($scope) {
	var navbarul = d3.selectAll('ul#navbarul>li');
	navbarul.attr("class", null);
	d3.select('#aboutNavItem').attr("class", "active");
})

module.controller('ContactCtrl', function($scope) {
	var navbarul = d3.selectAll('ul#navbarul>li');
	navbarul.attr("class", null);
	d3.select('#contactNavItem').attr("class", "active");
})

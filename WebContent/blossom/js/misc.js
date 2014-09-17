var module = angular.module('blossom.misc', [ 'ngRoute' ]);

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

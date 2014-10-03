var module = angular.module('blossom.stats', [ 'ngRoute', 'ngResource' ]);

module.controller('StatCtrl', function($scope, $http) {

	var navbarul = d3.selectAll('ul#navbarul>li');
	navbarul.attr("class", null);
	d3.select('#statNavItem').attr("class", "active");

	var graph = d3.select(".graphsvg").attr("width", "100%").attr("height", "100%");
	graph.append("rect").attr("width", "100%").attr("height", "100%").style("fill", "white").style("stroke", "red").style("stroke-width", 1);
	var offset = 20;
	console.log($("#jsonstatable").width());
	console.log($(".graphsvg").width());
	console.log("graphsvg width " + d3.select(".graphsvg").attr("width"));
	// where we're going to draw .. to give ourselves a little offset
	// var innerRect = graph.append("g").attr("transform", function(d, i) {
	// return "translate(" + offset + ",0)";
	// }).attr("id", "innerrect");
	// innerRect.append("rect").attr("width", $("#jsonstatable").width() -
	// (offset * 2)).attr("height", "100%").style("fill",
	// "white").style("stroke", "green").style("stroke-width", 1);

	d3.json("data/datastat.json", function(json) {
		var texts = graph.selectAll("text").data(json.dataset).enter().append("text");
		var startScale = $("#jsonstatable").width() / (json.dataset.length - 1);
		var textBaseline = 10;
		var rectBaseline = 20;
		var linearScale = d3.scale.linear().domain([ 0, json.dataset.length ]).range([ offset * 2, $("#jsonstatable").width() + offset ]);
		var circleAttributes = texts.attr("x", function(d) {
			console.log("index : " + json.dataset.indexOf(d));
			return linearScale(json.dataset.indexOf(d));
		}).attr("y", function(d) {
			return textBaseline;
		}).text(function(d) {
			return d.valued
		}).attr("font-family", "sans-serif").attr("font-size", "10px").attr("fill", "black").attr("text-anchor", "middle");

		var barWidth = 20;
		var offsetBarPlacement = barWidth / 2;
		
		var singlebar = graph.selectAll("g").data(json.dataset).enter().append("g").attr("transform", function(d, i) {
			console.log("d" + d + " ind " + json.dataset.indexOf(d));
			return "translate( " + (linearScale(json.dataset.indexOf(d)) - offsetBarPlacement) + "," + rectBaseline + ")";
		})

		var rectangles = singlebar.append("rect").attr("height", 0).attr("width", 20).attr("id", "chartbar");
		rectangles.transition().attr("height", function(d) {
			return d.value
		}).attr("width", 20);

	})

})
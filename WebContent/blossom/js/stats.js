var module = angular.module('blossom.stats', [ 'ngRoute', 'ngResource' ]);
module.factory('DatabaseStatFactory', function($resource) {
	return $resource('./rest/stat/all', {}, {
		query : {
			method : 'GET',
			params : {},
			isArray : false
		}
	})
});

module.controller('StatCtrl', function($scope, $http, DatabaseStatFactory) {

	var navbarul = d3.selectAll('ul#navbarul>li');
	navbarul.attr("class", null);
	d3.select('#statNavItem').attr("class", "active");

	var graph = d3.select(".graphsvg").attr("width", "100%").attr("height", "100%");
	graph.append("rect").attr("width", "100%").attr("height", "100%").style("fill", "white").style("stroke", "red").style("stroke-width", 1);
	var offset = 20;
	var scaleWidth = 60;
	var sceneWidth = $("#jsonstatable").width() - scaleWidth;
	var sceneHeight = $("#jsonstatable").height() - offset;

	// d3.json("data/datastat.json", function(json) {
	// buildHistogram(json);
	// });

	DatabaseStatFactory.get({}, function(retrievedStats) {
		buildHistogram(retrievedStats);
	});
	buildHistogram = function(json) {
		var textBaseline = -10;
		var rectBaseline = 20;
		var valuesLength = json.dataset.length;
		var maxValue = computeMax(json.dataset)
		// linearscale to adapt bar height to component dimensions
		var linearScale = d3.scale.linear().domain([ 0, maxValue ]).range([ 0, sceneHeight ]);
		// var scaleStepNumber = maxValue / 5;
		// var scaleStepNumber = Math.max(5, (maxValue / 5));
		var scaleStepNumber = 10;
		if (scaleStepNumber > maxValue) {
			scaleStepNumber = maxValue;
		}
		var scaleStep = (maxValue / scaleStepNumber);
		var scaledScaleStep = linearScale(maxValue / scaleStepNumber);
		var equalSplit = (sceneWidth / valuesLength);
		var barWidth = equalSplit - (offset * 2);
		var offsetBarPlacement = barWidth / 2;
		var xTranslate = function(index) {
			return (index * equalSplit) + scaleWidth
		};
		var scaleYtranslate = function(index) {
			return (rectBaseline + (index * scaledScaleStep));
		}
		var computeScaleValue = function(index) {
			return (index * scaleStep)
		}
		// we build the scale to draw the scale overlay
		var scaledValuesForScale = [];
		for (i = 0; i <= scaleStepNumber; i++) {
			scaledValuesForScale.push(scaleYtranslate(i));
		}
		var scaleArea = graph.selectAll("g").data(scaledValuesForScale).enter().append("g").attr("id", "scaleg").attr("transform", function(d, i) {
			return "translate( 0," + d + ")";
		})
		var scaleX = function(value) {
			return (scaleWidth / 2);
		}
		scaleArea.append("rect").attr("width", scaleWidth).attr("height", scaledScaleStep).style("stroke", "blue").style("stroke-width", 2).attr("id", "scaleg");
		scaleArea.append("text").attr("x", scaleWidth / 2).attr("y", 4).text(function(d, i) {
			return computeScaleValue(i)
		}).style("fill", "yellow").style("text-anchor", "middle");

		var singlebar = graph.selectAll("g#chartbar").data(json.dataset).enter().append("g").attr("transform", function(d, i) {
			index = json.dataset.indexOf(d);
			return "translate( " + xTranslate(index) + "," + rectBaseline + ")";
		})

		singlebar.append("rect").attr("width", equalSplit).attr("height", sceneHeight).style("stroke", "green").style("stroke-width", 2).attr("id", "chartbar");

		var rectangles = singlebar.append("rect").attr("x", offset).attr("width", barWidth / 2).attr("height", 0).style("fill", "red").style("stroke", "yellow").style("stroke-width", 1).attr("id", "chartbar");
		rectangles.transition().attr("height", function(d) {
			return linearScale(d.value);
		}).attr("width", barWidth);
		singlebar.append("text").attr("x", function(d) {
			index = json.dataset.indexOf(d);
			return offsetBarPlacement - (d.valued.length / 2);
		}).attr("y", function(d) {
			return textBaseline;
		}).text(function(d) {
			return d.valued
		});

		var scaleOverlayArea = graph.selectAll("g#scaleoverlay").data(scaledValuesForScale).enter().append("g").attr("id", "scaleoverlay").attr("transform", function(d, i) {
			return "translate( " + scaleWidth + "," + d + ")";
		})
		scaleOverlayArea.append("line").attr("x1", 0).attr("y1", 0).attr("x2", sceneWidth).attr("xy", 0).style("stroke", "blue").style("stroke-width", 2).attr("id", "scaleline");

	}
	computeMax = function(dataset) {
		var max = 0;
		dataset.forEach(function(d) {
			if (d.value > max) {
				max = d.value;
			}
		});
		return max;
	}
})
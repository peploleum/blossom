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
	$(window).resize(function() {
		showBarChart(), showPieChart()
	});
	showBarChart = function() {
		var histosvg = d3.select("#jsonstatablehisto").attr("width", "100%").attr("height", "100%");
		histosvg.append("rect").attr("width", "100%").attr("height", "100%").style("fill", "white").style("stroke", "white").style("stroke-width", 1);
		var offset = 20;
		var scaleWidth = 60;
		var sceneWidth = $("#jsonstatablehisto").width() - scaleWidth;
		var sceneHeight = $("#jsonstatablehisto").height() - offset;

		d3.json("data/datastat.json", function(json) {
			buildHistogram(json);
		});

		// DatabaseStatFactory.get({}, function(retrievedStats) {
		// buildHistogram(retrievedStats);
		// });
		buildHistogram = function(json) {

			d3.selectAll("#scaleoverlay").remove();
			d3.selectAll("#scaleg").remove();
			d3.selectAll("#scaleline").remove();
			d3.selectAll("#scalearea").remove();
			d3.selectAll("#chartbar").remove();
			d3.selectAll("#singlebar").remove();
			d3.selectAll("#chartrect").remove();
			var textBaseline = -10;
			var rectBaseline = 20;
			var valuesLength = json.dataset.length;
			var maxValue = computeMax(json.dataset)
			// linearscale to adapt bar height to component dimensions
			var linearScale = d3.scale.linear().domain([ 0, maxValue ]).range([ 0, sceneHeight ]);
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
			var scaleArea = histosvg.selectAll("g").data(scaledValuesForScale).enter().append("g").attr("id", "scaleg").attr("transform", function(d, i) {
				return "translate( 0," + d + ")";
			})
			var scaleX = function(value) {
				return (scaleWidth / 2);
			}
			scaleArea.append("rect").attr("width", scaleWidth).attr("height", scaledScaleStep).style("fill", "white").attr("id", "scalearea");
			scaleArea.append("text").attr("x", scaleWidth / 2).attr("y", 4).text(function(d, i) {
				return computeScaleValue(i)
			}).style("fill", "black").style("text-anchor", "middle");

			var singlebar = histosvg.selectAll("g#chartbar").data(json.dataset).enter().append("g").attr("transform", function(d, i) {
				index = json.dataset.indexOf(d);
				return "translate( " + xTranslate(index) + "," + rectBaseline + ")";
			}).attr("id", "chartbar");

			// singlebar.append("rect").attr("width", equalSplit).attr("height",
			// sceneHeight).style("stroke", "green").style("stroke-width",
			// 2).attr("id", "singlebar");
			singlebar.append("rect").attr("width", equalSplit).attr("height", sceneHeight).style("fill", "white").attr("id", "singlebar");

			var rectangles = singlebar.append("rect").attr("x", offset).attr("width", barWidth / 2).attr("height", 0).style("fill", "red").style("stroke", "yellow").style("stroke-width", 1).attr("id", "chartbar");
			rectangles.transition().attr("height", function(d) {
				return linearScale(d.value);
			}).attr("width", barWidth).attr("id", "chartrect");
			singlebar.append("text").attr("x", function(d) {
				index = json.dataset.indexOf(d);
				return offsetBarPlacement - (d.valued.length / 2);
			}).attr("y", function(d) {
				return textBaseline;
			}).text(function(d) {
				return d.valued
			});

			var scaleOverlayArea = histosvg.selectAll("g#scaleoverlay").data(scaledValuesForScale).enter().append("g").attr("id", "scaleoverlay").attr("transform", function(d, i) {
				return "translate( " + scaleWidth + "," + d + ")";
			})
			scaleOverlayArea.append("line").attr("x1", 0).attr("y1", 0).attr("x2", sceneWidth).attr("xy", 0).style("stroke", "black").style("stroke-dasharray", "0.2 5").attr("id", "scaleline");

		}
	}
	showPieChart = function() {
		var piesvg = d3.select("#jsonstatablepie").attr("width", "100%").attr("height", "100%");
		piesvg.append("rect").attr("width", "100%").attr("height", "100%").style("fill", "white").style("stroke", "red").style("stroke-width", 1);
		var offset = 10;
		var sceneWidth = $("#jsonstatablepie").width();
		var sceneHeight = $("#jsonstatablepie").height();
		var radius = Math.min(sceneWidth, sceneHeight) / 2;
		d3.json("data/datastat.json", function(json) {
			buildPie(json);
		});

		// DatabaseStatFactory.get({}, function(retrievedStats) {
		// buildHistogram(retrievedStats);
		// });
		buildPie = function(json) {
			var color = d3.scale.ordinal().range([ "#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00" ]);
			var valuesLength = json.dataset.length;
			var maxValue = computeMax(json.dataset)
			var arc = d3.svg.arc().outerRadius(radius - offset).innerRadius(0);

			var pie = d3.layout.pie().sort(null).value(function(d) {
				return d.value;
			})
			var pieZero = d3.layout.pie().sort(null).value(function(d) {
				return 1;
			})

			var innerSvg = piesvg.append("g").attr("transform", "translate(" + sceneWidth / 2 + "," + sceneHeight / 2 + ")").attr("id", "pie");
			// linearscale to adapt bar height to component dimensions
			var g = innerSvg.selectAll(".arc").data(pie(json.dataset)).enter().append("g").attr("class", "arc");
			// paths = innerSvg.selectAll("path").data(pie(json.dataset));
			// paths = innerSvg.selectAll("path");
			// console.log(paths);
			// paths.transition().duration(1000).attr("d", function(d) {
			// console.log(d);
			// return pie(d)
			// });
			// paths.transition().duration(750).attrTween("d",
			// function(d){return this});

			// rectangles.transition().attr("height", function(d) {
			// return linearScale(d.value);
			// }).attr("width", barWidth).attr("id", "chartrect");
			g.append("path").attr("d", arc).style("fill", function(d) {
				return color(d.value);
			});

			g.append("text").attr("transform", function(d) {
				return "translate(" + arc.centroid(d) + ")";
			}).attr("dy", ".35em").style("textq-anchor", "middle").text(function(d) {
				console.log("d: " + d.data.valued);
				return d.data.valued;
			});
		}
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

	showBarChart();
	showPieChart();
})
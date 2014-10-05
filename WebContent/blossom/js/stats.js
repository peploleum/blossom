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
		showBarChart();
		showPieChart();
		showHorizBarChart();
	});

	var ORIENTATION = {};
	ORIENTATION.HORIZ = 0;
	ORIENTATION.VERT = 1;
	ORIENTATION.UP = 2;
	ORIENTATION.DOWN = 3;

	showBarChart = function() {
		var histosvg = d3.select("#jsonstatablehisto").attr("width", "100%").attr("height", "100%");
		histosvg.append("rect").attr("width", "100%").attr("height", "100%").style("fill", "white").style("stroke", "white").style("stroke-width", 1);
		var offset = 20;
		var sceneOffset = 40;
		var scaleWidth = 60;
		var sceneWidth = $("#jsonstatablehisto").width() - scaleWidth;
		var sceneHeight = $("#jsonstatablehisto").height() - (sceneOffset);

		d3.json("data/datastat.json", function(json) {
			buildHistogram(json);
		});

		// DatabaseStatFactory.get({}, function(retrievedStats) {
		// buildHistogram(retrievedStats);
		// });
		buildHistogram = function(json) {
			var color = d3.scale.ordinal().range([ "#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00" ]);
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
			var scaleArea = histosvg.selectAll("g#scaleg").data(scaledValuesForScale).enter().append("g").attr("id", "scaleg").attr("transform", function(d, i) {
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

			var rectangles = singlebar.append("rect").attr("x", offset).attr("width", barWidth / 2).attr("height", 0).style("fill", function(d) {
				return color(linearScale(d.value))
			}).attr("id", "chartbar");
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
			console.log("vert length: " + scaledValuesForScale.length);

			var scaleOverlayArea = histosvg.selectAll("g#scaleoverlay").data(scaledValuesForScale).enter().append("g").attr("id", "scaleoverlay").attr("transform", function(d, i) {
				return "translate( " + scaleWidth + "," + d + ")";
			})
			scaleOverlayArea.append("line").attr("x1", 0).attr("y1", 0).attr("x2", sceneWidth).attr("xy", 0).style("stroke", "black").style("stroke-dasharray", "0.2 5").attr("id", "scaleline");

		}
	}
	showHorizBarChart = function() {
		var histosvg = d3.select("#jsonstatablehistohoriz").attr("width", "100%").attr("height", "100%");
		var offset = 3;
		var sceneOffset = 50;
		var scaleSize = 15;
		// var sceneWidth = $("#jsonstatablehistohoriz").width() - scaleSize;
		// var sceneHeight = $("#jsonstatablehistohoriz").height() -
		// (sceneOffset);
		var sceneWidth = $("#jsonstatablehistohoriz").width() - sceneOffset;
		var sceneHeight = $("#jsonstatablehistohoriz").height() - (scaleSize);
		console.log("sceneWidth: " + sceneWidth);
		computeScaleMaxValueBaseline = function(orientation) {
			if (orientation == ORIENTATION.HORIZ) {
				return sceneWidth;
			} else {
				return sceneHeight
			}
		}

		d3.json("data/datastat.json", function(json) {
			buildBartChart(json);
		});

		// DatabaseStatFactory.get({}, function(retrievedStats) {
		// buildHistogram(retrievedStats);
		// });
		buildBartChart = function(json) {
			var color = d3.scale.ordinal().range([ "#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00" ]);
			d3.selectAll("#horizscaleoverlay").remove();
			d3.selectAll("#horizscaleg").remove();
			d3.selectAll("#horizscaleline").remove();
			d3.selectAll("#horizscalearea").remove();
			d3.selectAll("#horizchartbar").remove();
			d3.selectAll("#horizsinglebar").remove();
			d3.selectAll("#horizchartrect").remove();
			d3.selectAll("#horizchartrect").remove();
			d3.selectAll("#horizscene").remove();
			histosvg.append("rect").attr("width", "100%").attr("height", "100%").style("fill", "white").style("stroke", "red").style("stroke-width", 1).attr("id", "horizscene");
			var orientation = ORIENTATION.HORIZ;
			var textBaseline = -10;
			var rectBaseline = 6;
			var valuesLength = json.dataset.length;
			console.log("valuesLength: " + valuesLength);
			computeSingleBarDimensions = function(orientation) {
				var dim = {};
				if (orientation == ORIENTATION.HORIZ) {
					dim.width = sceneWidth;
					dim.height = equalSplit;
				} else if (orientation == ORIENTATION.VERT) {
					dim.width = equalSplit;
					dim.height = sceneHeight;
				}
				return dim;
			};
			// var singleBarWidth = computeSingleBarWidth(ORIENTATION.HORIZ);
			var maxValue = computeMax(json.dataset)
			// linearscale to adapt bar height to component dimensions
			var maxRange = computeScaleMaxValueBaseline(orientation);
			console.log("maxRange: " + maxRange);
			var linearScaleFunction = d3.scale.linear().domain([ 0, maxValue ]).range([ 0, computeScaleMaxValueBaseline(orientation) ]);
			var scaleStepNumber = 10;
			if (scaleStepNumber > maxValue) {
				scaleStepNumber = maxValue;
			}
			var scaleStep = (maxValue / scaleStepNumber);
			var scaledScaleStep = linearScaleFunction(maxValue / scaleStepNumber);
			console.log("scaledScaleStep: " + scaledScaleStep + " scaleStepNumber: " + scaleStepNumber);
			computeEqualSplit = function(orientation) {
				if (orientation == ORIENTATION.HORIZ) {
					return (sceneHeight / valuesLength)
				} else {
					return (sceneWidth / valuesLength)
				}
			}
			var equalSplit = computeEqualSplit(orientation);
			console.log("equalSplit: " + equalSplit);
			var barWidth = equalSplit - (offset * 2);
			var offsetBarPlacement = barWidth / 2;
			var splitTranslate = function(index) {
				return (index * equalSplit) + scaleSize
			};
			var scaleTranslate = function(index) {

				return ((index * scaledScaleStep));
			}
			var computeScaleValue = function(index) {
				return (index * scaleStep)
			}
			// we build the scale to draw the scale overlay
			var scaledValuesForScale = [];
			for (i = 0; i <= scaleStepNumber; i++) {
				scaledValuesForScale.push(scaleTranslate(i));
			}
			computeScaleAreaTranslate = function(d) {
				var translate = {};
				if (orientation == ORIENTATION.HORIZ) {
					translate.x = sceneOffset + d;
					translate.y = rectBaseline;
				} else if (orientation == ORIENTATION.VERT) {
					translate.x = 0;
					translate.y = rectBaseline;
				}
				return translate;
			}

			computeSingleBarTranslate = function(index) {
				var translate = {};
				if (orientation == ORIENTATION.HORIZ) {
					translate.x = sceneOffset;
					translate.y = splitTranslate(index);
				} else if (orientation == ORIENTATION.VERT) {
					translate.x = splitTranslate(index);
					translate.y = rectBaseline;
				}
				return translate;
			}
			computeScaleAreaDimensions = function(orientation) {
				var dim = {};
				if (orientation == ORIENTATION.HORIZ) {
					dim.width = sceneWidth / maxValue;
					dim.height = rectBaseline
				} else if (orientation == ORIENTATION.VERT) {
					dim.width = rectBaseline
					dim.height = sceneHeight / maxValue;
				}
				return dim;
			}
			console.log("horiz length: " + scaledValuesForScale.length);
			var scaleArea = histosvg.selectAll("g#horizscaleg").data(scaledValuesForScale).enter().append("g").attr("id", "horizscaleg").attr("transform", function(d, i) {
				return "translate(" + computeScaleAreaTranslate(d).x + "," + computeScaleAreaTranslate(d).y + ")";
			})
			var scaleX = function(value) {
				return (scaleSize / 2);
			}
			computeScaleTextCoords = function() {
				var coords = {};
				if (orientation == ORIENTATION.HORIZ) {
					coords.x = 0;
					coords.y = 7;
				} else if (orientation == ORIENTATION.VERT) {
					coords.x = scaleSize / 2;
					coords.y = 4;
				}
				return coords;
			}
			computeSingleBarRectangleDimensions = function(d) {
				var dim = {};
				if (orientation == ORIENTATION.HORIZ) {
					dim.width = linearScaleFunction(d.value);
					dim.height = equalSplit
				} else if (orientation == ORIENTATION.VERT) {
					dim.width = equalSplit
					dim.height = linearScaleFunction(d.value);
				}
				return dim;
			}
			console.log("scaled value for scale " + scaledValuesForScale[1]);
			// scaleArea.append("rect").attr("width",
			// scaledValuesForScale[1]).attr("height",
			// computeScaleAreaDimensions(orientation).height).style("fill",
			// "white").style("stroke", "blue").style("stroke-width",
			// 2).attr("id", "scalearea");
			scaleArea.append("text").attr("x", computeScaleTextCoords().x).attr("y", computeScaleTextCoords().y).text(function(d, i) {
				return computeScaleValue(i)
			}).style("fill", "black").style("text-anchor", "middle");
			//
			var singlebar = histosvg.selectAll("g#horizchartbar").data(json.dataset).enter().append("g").attr("transform", function(d, i) {
				index = json.dataset.indexOf(d);
				return "translate( " + computeSingleBarTranslate(index).x + "," + computeSingleBarTranslate(index).y + ")";
			}).attr("id", "horizchartbar");

			var rectangles = singlebar.append("rect").attr("x", 0).attr("width", 0).attr("height", 0).style("fill", function(d) {
				return color(linearScaleFunction(d.value))
			}).attr("id", "horizchartbar");
			rectangles.transition().attr("height", function(d) {
				return computeSingleBarRectangleDimensions(d).height
			}).attr("width", function(d) {
				return computeSingleBarRectangleDimensions(d).width
			}).attr("id", "horizchartrect");
			singlebar.append("text").attr("x", function(d) {
				return 0;
			}).attr("y", function(d) {
				return (equalSplit / 2);
			}).text(function(d) {
				return d.valued
			}).style("fill", "black").style("text-anchor", "end");
			//
			var scaleOverlayArea = histosvg.selectAll("g#horizscaleoverlay").data(scaledValuesForScale).enter().append("g").attr("id", "scaleoverlay").attr("transform", function(d, i) {
				return "translate( " + (sceneOffset + d) + "," + 0 + ")";
			})
			scaleOverlayArea.append("line").attr("x1", 0).attr("y1", 0).attr("x2", 0).attr("y2", sceneHeight).style("stroke", "black").style("stroke-dasharray", "0.2 5").attr("id", "horizscaleline");

		}
	}
	showPieChart = function() {
		var piesvg = d3.select("#jsonstatablepie").attr("width", "100%").attr("height", "100%");
		piesvg.append("rect").attr("width", "100%").attr("height", "100%").style("fill", "white").style("stroke", "white").style("stroke-width", 1);
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
	showHorizBarChart();
})
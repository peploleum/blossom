var module = angular.module('blossom.network', [ 'ngRoute' ]);

module.controller('NetworkCtrl', function($scope) {
	// a d3js bit here
	var data = [ 4, 8, 15, 16, 23, 42 ];

	var width = 420, barHeight = 20;

	var xsvg = d3.scale.linear().domain([ 0, d3.max(data) ]).range([ 0, width ]);

	var chart = d3.select(".chartsvg").attr("width", width).attr("height", barHeight * data.length);

	var bar = chart.selectAll("g").data(data).enter().append("g").attr("transform", function(d, i) {
		return "translate(0," + i * barHeight + ")";
	});

	bar.append("rect").attr("width", xsvg).attr("height", barHeight - 1);

	bar.append("text").attr("x", function(d) {
		return xsvg(d) - 3;
	}).attr("y", barHeight / 2).attr("dy", ".35em").text(function(d) {
		return d;
	});

	var graph = d3.select(".graphsvg").attr("width", width).attr("height", width);

	d3.json("data/data.json", function(json) {
		var circles = graph.selectAll("cirle").data(json.nodes).enter().append("circle");
		var circleAttributes = circles.attr("cx", function(d) {
			return d.x;
		}).attr("cy", function(d) {
			return d.y;
		}).attr("r", function(d) {
			return d.radius;
		}).style("fill", function(d) {
			return d.color;
		});

		var text = graph.selectAll("text").data(json.nodes).enter().append("text");

		var textLabels = text.attr("x", function(d) {
			return d.x;
		}).attr("y", function(d) {
			return d.y;
		}).text(function(d) {
			return "(" + d.name + ")";
		}).attr("font-family", "sans-serif").attr("font-size", "10px").attr("fill", "black").attr("text-anchor", "middle");
	})
	$scope.tooltipmessage = "tooltiptext";
	var graphplus = d3.select(".graphsvgplus").attr("width", width).attr("height", width);
	graphplus.append("rect").attr("width", width).attr("height", width).style("fill", "white").style("stroke", "gray").style("stroke-width", 5);
	var force = d3.layout.force().gravity(.05).distance(100).charge(-100).size([ width, width ]);
	d3.json("data/data.json", function(json) {
		force.nodes(json.nodes).links(json.links).start();

		var link = graphplus.selectAll(".link").data(json.links).enter().append("line").attr("class", "link");

		var node = graphplus.selectAll(".node").data(json.nodes).enter().append("g").attr("class", "nodetest").call(force.drag);

		var images = node.append("image").attr("xlink:href", function(d) {
			return "resources/" + d.name + ".png"
		}).attr("x", function(d) {
			return -1 * d.size / 2
		}).attr("y", function(d) {
			return -1 * d.size / 2
		}).attr("width", function(d) {
			return d.size
		}).attr("height", function(d) {
			return d.size
		}).attr("custom", function(d) {
			return d.catchphrase
		});
		// .append("title") // Adding the title
		// // element to the
		// // rectangles.
		// .text(function(d) {
		// return d.catchphrase;
		// });
		var tooltip = d3.select(".tooltipcustom");

		console.log("testingngg: ");
		d3.selectAll("image").each(function(d, i) {
			if (d.catchphrase != null) {
				var catchphrase = d3.select(this).attr("custom");
				console.log("testing3: " + catchphrase + " i  " + i + " e " + d3.select(this));
				d3.select(this).on("mouseover", function(d) {
					tooltip.text(function(d) {
						return catchphrase;
					});
					tooltip.style("visibility", "visible")
				}).on("mousemove", function() {
					return tooltip.style("top", (d3.event.pageY + 16) + "px").style("left", (d3.event.pageX + 16) + "px");
				}).on("mouseout", function() {
					return tooltip.style("visibility", "hidden")
				}).on("click", function() {
					console.log("click")
				});
				;
			}
		});

		node.append("text").attr("dx", function(d) {
			return 2 + d.size / 2
		}).attr("dy", ".35em").text(function(d) {
			return d.name;
		}).attr("custom", function(d) {
			return d.catchphrase;
		});

		force.on("tick", function() {
			link.attr("x1", function(d) {
				return d.source.x;
			}).attr("y1", function(d) {
				return d.source.y;
			}).attr("x2", function(d) {
				return d.target.x;
			}).attr("y2", function(d) {
				return d.target.y;
			});

			node.attr("transform", function(d) {
				return "translate(" + d.x + "," + d.y + ")";
			});

		})
		

	});
	$scope.submitNode = function(){
		console.log("submitnode");
		$scope.submitmessage = "haha";
	}
})
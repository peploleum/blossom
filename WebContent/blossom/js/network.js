var module = angular.module('blossom.network', [ 'ngRoute' ]);

module.controller('NetworkCtrl', function($scope) {

	// init some control scope vars used to pop error display
	$scope.addNodeError = false;
	$scope.addNodeSuccess = true;

	Object
	selection = null;
	// a d3js bit here

	var data = [ 4, 8, 15, 16, 23, 42 ];

	// var width = 420, barHeight = 20;
	var barHeight = 20;

	var width = $("svg").parent().width();
	var height = $("svg").height();

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

	d3.json("data/data2.json", function(json) {
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
	console.log("width : " + width + " height " + height);
	var graphplus = d3.select(".graphsvgplus").attr("width", "100%").attr("height", 600);
	graphplus.append("rect").attr("width", "100%").attr("height", 600).style("fill", "white");// .style("stroke",
	// "gray").style("stroke-width",
	// 0);
	var force = d3.layout.force().gravity(.05).distance(100).charge(-100).size([ width, 600 ]);

	d3.json("data/data.json", function(json) {

		// just another way of pushing nodes
		json.nodes.forEach(function(n) {
			force.nodes().push(n)
		});
		force.links(json.links);
		force.start();

		updateGraph();

	});

	updateGraph = function() {
		// this is an update: removing existing links & nodes before adding them
		graphplus.selectAll(".link").remove();
		var linkData = graphplus.selectAll(".link").data(force.links());
		var link = linkData.enter().append("line").attr("class", "link");

		graphplus.selectAll(".graphnode").remove();
		var nodeData = graphplus.selectAll(".node").data(force.nodes());
		var node = nodeData.enter().append("g").attr("class", "graphnode").call(force.drag);

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
		// no need for a title we aim to provide our own tooltip
		// .append("title") // Adding the title
		// // element to the
		// // rectangles.
		// .text(function(d) {
		// return d.catchphrase;
		// });

		// this is hidden in the html, we use it to pop it on the grid of the
		// svg when needed
		var tooltip = d3.select(".tooltipcustom");

		d3.selectAll("image").each(function(d, i) {
			if (d.catchphrase != null) {
				var catchphrase = d3.select(this).attr("custom");
				d3.select(this).on("mouseover", function(d) {
					tooltip.text(function(d) {
						return catchphrase;
					});
					tooltip.style("visibility", "visible")
				}).on("mousemove", function() {
					return tooltip.style("top", (d3.event.pageY + 16) + "px").style("left", (d3.event.pageX + 16) + "px");
				}).on("mouseout", function() {
					return tooltip.style("visibility", "hidden")
				});
			}
		});

		d3.selectAll("image").on("click", function(d) {
			console.log("node clicked");
			console.log(this.parentNode);
			var createElement = document.createElement("rect");
			d3.select(createElement).attr("class", "graphnodeselect").attr("width", "100%").attr("height", "100%").style("stroke", "gray");
			// createElement.select("#width").append(70);
			// createElement.attr("class", "graphnodeselect").attr("width",
			// 70).attr("height", 70);
			console.log(createElement);
			this.parentNode.appendChild(createElement);
		});
		// append("rect").attr("width", "100%").attr("height",
		// 600).style("fill", "white");// .style("stroke",
		// "gray").style("stroke-width",
		// node.append("rect").attr("width", function(d) {
		// return d.width
		// }).attr("height", function(d) {
		// return d.height
		// }).style("stroke", "gray").style("stroke-width", 3);

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

		});
	}

	$scope.submitNode = function() {
		console.log("submitnode");
		force.nodes().push({
			"name" : "leela",
			"size" : 70,
			"catchphrase" : "Monday monkey lives for the weekend!"
		});
		force.links().push({
			"source" : 7,
			"target" : 0
		})
		updateGraph();
		force.start();
		$scope.submitmessage = "haha";
	}

	findNodeByName = function(name) {
		console.log("searching for node by name:" + name);
		var matchingNode;
		force.nodes().forEach(function(n) {
			if (n.name == name) {
				matchingNode = n
			}
		});
		console.log("matchingNode:" + matchingNode);
	}

	findNodeIndexByNodeName = function(name) {
		console.log("searching for node index by name:" + name);
		var matchingNodeIndex = -1;
		for (var i = 0; i < force.nodes().length; i++) {
			if (force.nodes()[i].name == name) {
				matchingNodeIndex = i;
				console.log("matchingNodeIndex:" + matchingNodeIndex);
				return matchingNodeIndex;
			}
		}
		console.log("matchingNodeIndex:" + matchingNodeIndex);
		return matchingNodeIndex;
	}

	$scope.submitFormNode = function() {
		console.log("submitnode from form name " + $scope.nodehelper.name + " neighbors: " + $scope.nodehelper.neighbors);
		var neighborIndex = findNodeIndexByNodeName($scope.nodehelper.neighbors);
		console.log("found neighbor at index " + neighborIndex);
		if (neighborIndex == -1) {
			$scope.addNodeError = true;
			$scope.addNodeSuccess = false;
		} else {
			console.log("pushing node " + $scope.nodehelper.name + " " + $scope.nodehelper.catchphrase);
			force.nodes().push({
				"name" : $scope.nodehelper.name,
				"size" : 70,
				"catchphrase" : $scope.nodehelper.catchphrase
			});
			console.log("pushing link " + (force.nodes().length - 1) + " " + neighborIndex);
			force.links().push({
				"source" : force.nodes().length - 1,
				"target" : neighborIndex
			})
			updateGraph();
			force.start();
			$scope.addNodeSuccess = true;
		}
	}
})
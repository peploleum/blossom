var module = angular.module('blossom.network', [ 'ngRoute' ]);

module.controller('NetworkCtrl', function($scope) {

	// init some control scope vars used to pop error display
	$scope.addNodeError = false;
	$scope.addNodeSuccess = true;

	var selectionClicked = null; // id the clicked button in a group
	selection = []; // selection model : stores ids of selected nodes
	pinnedNodes = []; // pin model : stores ids of pinned nodes

	// manage key events for the whole body
	d3.select("body").on("keydown", function(d) {
		console.log("key stroke detected : " + d3.event.keyCode);
		if (d3.event.keyCode == 27) // the famous ESC key ...
		{
			console.log("ESC pressed, purge selection");
			clearSelection();
		}
	});

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
	var graphplus = d3.select(".graphsvgplus").attr("width", "100%").attr("height", 500);
	graphplus.append("rect").attr("width", "100%").attr("height", 600).style("fill", "white");// .style("stroke",
	// "gray").style("stroke-width",
	// 0);
	var force = d3.layout.force().gravity(.05).distance(150).charge(-200).size([ width, 500 ]);

	clearSelection = function() {
		d3.selectAll(".graphnodeselect").remove();
		selection = [];
	}

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
		// we add a custom attr to reuse it without entering data
		var node = nodeData.enter().append("g").attr("class", "graphnode").attr("custom", function(d) {
			return d.id
		}).call(force.drag);

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

		d3.selectAll("image").each(function(d, i) {
			paintItem(this);
		});

		function paintItem(imageItem) {
			var retrievedCustomFieldAsId = d3.select(imageItem.parentNode).attr("custom");

			// manage selection
			if (selection.indexOf(retrievedCustomFieldAsId) != -1) {
				console.log("adding " + retrievedCustomFieldAsId);
				console.log(selection);
				var selectedImageItem = d3.select(imageItem);
				var selectionWidth = selectedImageItem.attr("width");
				var selectionHeight = selectedImageItem.attr("height");
				var selectionX = selectedImageItem.attr("x");
				var selectionY = selectedImageItem.attr("y");
				d3.select(imageItem.parentNode).append("rect").attr("class", "graphnodeselect").attr("width", selectionWidth).attr("height", selectionHeight).attr("x", selectionX).attr("y", selectionY).attr("custom", retrievedCustomFieldAsId).on("click", function(d) {
					console.log("rect clicked");
					if (d3.event.ctrlKey) {
						var selectedRect = d3.select(this);
						selectedRect.remove();
						console.log("index to remove: " + selectedRect.attr("custom"))
						selection.splice(selection.indexOf(selectedRect.attr("custom")), 1);

						console.log(selection);
					}
				});

			}

			// manage pins
			if (pinnedNodes.indexOf(retrievedCustomFieldAsId) != -1) {
				console.log("pinning " + retrievedCustomFieldAsId);
				console.log(selection);
				var PIN_SIZE = 10;
				var selectedImageItem = d3.select(imageItem);
				var imageWidth = selectedImageItem.attr("width");
				var imageHeight = selectedImageItem.attr("height");
				// var pinX = selectedImageItem.attr("x") + (imageWidth / 2) -
				// (PIN_SIZE / 2);
				// var pinY = selectedImageItem.attr("y") + (imageHeight / 2) -
				// (PIN_SIZE / 2);
				var pinX = selectedImageItem.attr("x");
				var pinY = selectedImageItem.attr("y");
				d3.select(imageItem.parentNode).append("rect").attr("class", "pinnode").attr("width", PIN_SIZE).attr("height", PIN_SIZE).attr("x", pinX).attr("y", pinY).attr("custom", retrievedCustomFieldAsId);

			}
		}
		d3.selectAll("image").on("click", function(d) {
			console.log("node clicked");
			var retrievedCustomFieldAsId = d3.select(this.parentNode).attr("custom");
			if (selection.indexOf(retrievedCustomFieldAsId) == -1) {
				selection.push(retrievedCustomFieldAsId);
			}
			paintItem(this);
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

		});
	}

	findNodeById = function(id) {
		console.log("searching for node by id:" + id);
		var matchingNode;
		force.nodes().forEach(function(n) {
			if (n.id == id) {
				matchingNode = n
			}
		});
		console.log("matchingNode:" + matchingNode);
	}

	findNodeIndexByNodeId = function(id) {
		console.log("searching for node index by name:" + id);
		var matchingNodeIndex = -1;
		for (var i = 0; i < force.nodes().length; i++) {
			if (force.nodes()[i].id == id) {
				matchingNodeIndex = i;
				console.log("matchingNodeIndex:" + matchingNodeIndex);
				return matchingNodeIndex;
			}
		}
		console.log("matchingNodeIndex:" + matchingNodeIndex);
		return matchingNodeIndex;
	}

	$scope.submitFormNode = function() {

		if (selectionClicked == 'AddLink') {
			console.log("selection clicked");
			for (var i = 0; i < selection.length; i++) {
				var sourceIndex = findNodeIndexByNodeId(selection[i]);
				console.log("source index " + sourceIndex);
				if (sourceIndex != -1) {
					for (var j = 0; j < selection.length; j++) {
						if (j != i) {
							var destinationIndex = findNodeIndexByNodeId(selection[j]);
							console.log("destination index " + destinationIndex);
							if (destinationIndex != -1) {
								console.log("drawing link from " + sourceIndex + " to " + destinationIndex);
								force.links().push({
									"source" : sourceIndex,
									"target" : destinationIndex
								})
							}
						}
					}

				}
			}
		} else if (selectionClicked == 'AddNode') {
			console.log("submitnode from form name " + $scope.nodehelper.name);
			console.log("pushing node " + $scope.nodehelper.name + " " + $scope.nodehelper.catchphrase);
			var size = 70;
			if ($scope.nodehelper.size != null)
				size = $scope.nodehelper.size;
			console.log("size from scope:" + $scope.nodehelper.size + " var: " + size);
			force.nodes().push({
				"id" : generateUUID(),
				"name" : $scope.nodehelper.name,
				"size" : size,
				"catchphrase" : $scope.nodehelper.catchphrase
			});
		} else if (selectionClicked == 'Start') {
			force.nodes().forEach(function(n) {
				console.log(n);
				if (pinnedNodes.indexOf(n.id) == -1)
					n.fixed = false;
			});
		} else if (selectionClicked == 'Stop') {
			force.nodes().forEach(function(n) {
				console.log(n);
				n.fixed = true;
			});
		} else if (selectionClicked == 'ClearSel') {
			console.log("clearsel");
			clearSelection();
		} else if (selectionClicked == 'PinSelection') {
			console.log("Pin selection");
			for (var i = 0; i < selection.length; i++) {
				var sourceIndex = findNodeIndexByNodeId(selection[i]);
				console.log("pin " + force.nodes()[sourceIndex]);
				force.nodes()[sourceIndex].fixed = true;
				pinnedNodes.push(force.nodes()[sourceIndex].id);
			}
		} else if (selectionClicked == 'UnPinSelection') {
			console.log("UnPin selection");
			for (var i = 0; i < selection.length; i++) {
				var sourceIndex = findNodeIndexByNodeId(selection[i]);
				console.log("pin " + force.nodes()[sourceIndex]);
				force.nodes()[sourceIndex].fixed = false;
				pinnedNodes.splice(force.nodes()[sourceIndex].id);
			}
		} else if (selectionClicked == 'ComputeStats') {
			console.log("Compute stats");
			computeStats();
		}
		force.start();
		updateGraph();
		$scope.addNodeSuccess = true;
		selectionClicked = null;
	}

	$scope.setClick = function(b) {
		selectionClicked = b;
	}

	computeStats = function() {
		// we shall then try to place this logic
		// server-side
		// first wo do an ultra slow stat computing : how much of each node name
		console.log("computing stats");
		var max = 0;
		var checkedNames = {};
		for (var i = 0; i < force.nodes().length; i++) {
			var name = force.nodes()[i].name;
			if (name in checkedNames)
				checkedNames[name] = checkedNames[name] + 1;
			else
				checkedNames[name] = 1;
		}
		for ( var key in checkedNames) {
			console.log("pushing " + key + " " + checkedNames[key]);
			if (max < checkedNames[key]) {
				max = checkedNames[key];
			}
		}
		var maxAndMap = {};
		maxAndMap.max = max;
		maxAndMap.map = checkedNames;
		console.log("result: " + maxAndMap);
		buildStatGraph(maxAndMap);
		return maxAndMap;
	}

	buildStatGraph = function(map) {

		// var width = 420, barHeight = 20;
		var barHeight = 20;

		var statwidth = $("svg").parent().width();
		var statheight = $("svg").height();
		var a, count = 0;
		var stats = [];
		for (a in map.map) {
			count++;
			stats.push(map[a]);
		}
		console.log("map.max: " + map.max + " statwidth " + statwidth);
		var test = d3.scale.linear().domain([ 0, 40 ]).range([ 0, statwidth ]);

		// this is OK for new browsers
		console.log("heightt: " + count + " widthFunction " + test);
		var statchart = d3.select(".statgraph").attr("width", statwidth).attr("height", barHeight * count);

		var singlebar = statchart.selectAll("g").data(stats).enter().append("g").attr("transform", function(d, i) {
			console.log("trans " + " " + i);
			return "translate(0," + i * barHeight + ")";
		});
		//
		singlebar.append("rect").attr("width", function(d) {
			return Math.floor();
		}).attr("height", barHeight - 1);
		//
		// bar.append("text").attr("x", function(d) {
		// return widthFunction(d) - 3;
		// }).attr("y", barHeight / 2).attr("dy", ".35em").text(function(d) {
		// return d;
		// });
	}

	generateUUID = function() {
		return ('xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
			var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
			return v.toString(16);
		}));
	}
})
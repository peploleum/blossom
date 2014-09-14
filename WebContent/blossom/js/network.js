var module = angular.module('blossom.network', [ 'ngRoute', 'ngResource' ]);

module.factory('StatFactory', function($resource) {
	return $resource('./rest/graph/stat', {}, {
		query : {
			method : 'GET',
			params : {},
			isArray : false
		}
	})
});

module.factory('GraphFactory', function($resource) {
	return $resource('./rest/graph', {}, {
		query : {
			method : 'GET',
			params : {},
			isArray : false
		}
	})
});

module.controller('NetworkCtrl', function($scope, $http, StatFactory, GraphFactory) {

	// init some control scope vars used to pop error display
	$scope.addNodeError = false;
	$scope.addNodeSuccess = true;

	var selectionClicked = null; // id the clicked button in a group
	selection = []; // selection model : stores ids of selected nodes
	pinnedNodes = []; // pin model : stores ids of pinned nodes

	// manage key events for the whole body
	d3.select("body").on("keydown", function(d) {
		if (d3.event.keyCode == 27) // the famous ESC key ...
		{
			console.log("ESC pressed, purge selection");
			clearSelection();
		}
	});

	// a d3js bit here
	var barHeight = 20;

	var width = $("svg").parent().width();
	var height = $("svg").height();

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

	computeGraphRest = function() {
		// this is the RESTful version of the graph initialization, we aim to
		// have a server-side model
		GraphFactory.get({}, function(graphFactory) {
			console.log(graphFactory);
			// just another way of pushing nodes
			graphFactory.nodes.forEach(function(n) {
				force.nodes().push(n)
			});
			links = force.links();
			links = [];
			force.links(graphFactory.links);
			force.start();

			updateGraph();
		})
	}
	computeGraphRest();
	// d3.json("data/data.json", function(json) {
	//
	// // just another way of pushing nodes
	// json.nodes.forEach(function(n) {
	// force.nodes().push(n)
	// });
	// force.links(json.links);
	// force.start();
	//
	// updateGraph();
	//
	// });

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

	// adding a link - client-side
	addLink = function() {
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
	}

	// adding a link - server-side
	addLinkPut = function() {
		console.log("selection clicked");
		newLinks = [];
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
							link = {
								"source" : sourceIndex,
								"target" : destinationIndex
							};
							newLinks.push(link);
						}
					}
				}

			}
			$http.put('./rest/graph/addLink', newLinks).success(function() {
				console.log("success");
				console.log(newLinks);
				for (link in newLinks) {
					console.log(newLinks[link]);
					force.links().push(newLinks[link]);
				}
				$scope.addNodeError = false;
				$scope.addNodeSuccess = true;
				force.start();
				updateGraph();
			}).error(function() {
				console.log("error");
				$scope.addNodeError = true;
				$scope.addNodeSuccess = false;
			});
		}
	}

	// adding a node - server-side
	addNodePut = function() {
		console.log("submitnode from form name " + $scope.nodehelper.name);
		console.log("pushing node " + $scope.nodehelper.name + " " + $scope.nodehelper.catchphrase);
		var size = 70;
		if ($scope.nodehelper.size != null)
			size = $scope.nodehelper.size;
		console.log("size from scope:" + $scope.nodehelper.size + " var: " + size);
		newNode = {
			"id" : generateUUID(),
			"name" : $scope.nodehelper.name,
			"size" : size,
			"catchphrase" : $scope.nodehelper.catchphrase
		};
		$http.put('./rest/graph/addNode', newNode).success(function() {
			console.log("success");
			force.nodes().push(newNode);
			force.start();
			updateGraph();
			$scope.addNodeError = false;
			$scope.addNodeSuccess = true;
		}).error(function() {
			console.log("error");
			$scope.addNodeError = true;
			$scope.addNodeSuccess = false;
		});
	}

	// adding a node - client side
	addNode = function() {
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
	}

	// unfixing nodes to animate
	startGraph = function() {
		force.nodes().forEach(function(n) {
			console.log(n);
			if (pinnedNodes.indexOf(n.id) == -1)
				n.fixed = false;
		});
	}

	// fixing nodes to stop animation
	stopGraph = function() {
		force.nodes().forEach(function(n) {
			console.log(n);
			n.fixed = true;
		});
	}

	// fixes nodeset from Selection
	pinSelection = function() {
		console.log("Pin selection");
		for (var i = 0; i < selection.length; i++) {
			var sourceIndex = findNodeIndexByNodeId(selection[i]);
			console.log("pin " + force.nodes()[sourceIndex]);
			force.nodes()[sourceIndex].fixed = true;
			pinnedNodes.push(force.nodes()[sourceIndex].id);
		}
	}

	// unfixes nodeset from Selection
	unPinSelection = function() {
		console.log("UnPin selection");
		for (var i = 0; i < selection.length; i++) {
			var sourceIndex = findNodeIndexByNodeId(selection[i]);
			console.log("pin " + force.nodes()[sourceIndex]);
			force.nodes()[sourceIndex].fixed = false;
			pinnedNodes.splice(force.nodes()[sourceIndex].id);
		}
	}
	// sureley there is a pro way of doing this, we use a var to store the
	// button we click on
	$scope.submitFormNode = function() {
		if (selectionClicked == 'AddLink') {
			// addLink();
			addLinkPut();
		} else if (selectionClicked == 'AddNode') {
			// addNode();
			addNodePut();
			computeStatsRest();
		} else if (selectionClicked == 'Start') {
			startGraph();
			force.start();
			updateGraph();

		} else if (selectionClicked == 'Stop') {
			stopGraph();
			force.start();
			updateGraph();

		} else if (selectionClicked == 'ClearSel') {
			clearSelection();
			force.start();
			updateGraph();

		} else if (selectionClicked == 'PinSelection') {
			pinSelection();
			force.start();
			updateGraph();

		} else if (selectionClicked == 'UnPinSelection') {
			unPinSelection();
			force.start();
			updateGraph();

		} else if (selectionClicked == 'ComputeStats') {
			computeStatsRest();
			force.start();
			updateGraph();
		}
		$scope.addNodeSuccess = true;
		selectionClicked = null;
	}

	$scope.setClick = function(b) {
		selectionClicked = b;
	}

	computeStatsRest = function() {
		// this is the RESTful version of the server-side stat computing
		StatFactory.get({}, function(statFactory) {
			console.log(statFactory.max);
			buildStatGraph(statFactory);
		})
	}

	computeStats = function() {
		// we since placed this logic
		// server-side
		// this is the full client version
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

		var barHeight = 20;

		var count = 0;
		var stats = [];
		for (a in map.map) {
			count++;
			console.log("a " + a + " " + map.map[a].stat);
			stats.push(map.map[a].stat);
		}
		console.log("heightt: " + count + " widthFunction " + linearScale);
		d3.selectAll("#chartbar").remove();
		var statchart = d3.select(".statgraph");

		console.log("map.max: " + map.max + " statwidth " + statchart[0][0].clientWidth);
		statchart.attr("width", statchart[0][0].clientWidth).attr("height", barHeight * count);
		var linearScale = d3.scale.linear().domain([ 0, d3.max(stats) ]).range([ 0, statchart[0][0].clientWidth ]);

		var singlebar = statchart.selectAll("g").data(stats).enter().append("g").attr("transform", function(d, i) {
			return "translate(0," + i * barHeight + ")";
		}).attr("id", "chartbar");

		// animating the rectangles
		var rectangles = singlebar.append("rect").attr("width", 0).attr("height", barHeight - 1).attr("id", "chartbar");
		rectangles.transition().attr("width", linearScale).attr("height", barHeight - 1).attr("id", "chartbar");

		singlebar.append("text").attr("x", function(d) {
			return linearScale(d) - 20;
		}).attr("y", barHeight / 2).attr("dy", ".35em").text(function(d) {
			return d;
		}).attr("id", "chartbar");
	}

	generateUUID = function() {
		return ('xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
			var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
			return v.toString(16);
		}));
	}
})
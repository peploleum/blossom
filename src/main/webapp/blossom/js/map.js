var module = angular.module('blossom.map', [ 'blossom.maps.businesslayer', 'blossom.bomanager', 'blossom.misc', 'ngRoute' ]);

module.controller('MapCtrl', function($scope, geoFactory, refresherFactory, boManagerFactory, uuidFactory) {
	$scope.currentCoordinates;
	$scope.names = [ 'amy', 'bender', 'farnsworth', 'fry', 'zoidberg', 'scruffy', 'nibbler', 'leela', 'hermes' ];
	var coordinate;
	var isIconStyle = false;
	var isShowPopUpOnMapClick = false;
	var navbarul = d3.selectAll('ul#navbarul>li');
	navbarul.attr("class", null);
	d3.select('#mapNavItem').attr("class", "active");
	$scope.layerName = "0";
	$scope.monitormodel = {};
	$scope.monitormodel.count = 0;
	$scope.monitormodel.ispopover = isShowPopUpOnMapClick;

	// styles
	var defaultStyle = {
		'Point' : [ new ol.style.Style({
			image : new ol.style.Circle({
				fill : new ol.style.Fill({
					color : 'rgba(255,255,0,0.5)'
				}),
				radius : 5,
				stroke : new ol.style.Stroke({
					color : '#ff0',
					width : 1
				})
			})
		}) ],
		'LineString' : [ new ol.style.Style({
			stroke : new ol.style.Stroke({
				color : '#f00',
				width : 3
			})
		}) ],
		'Polygon' : [ new ol.style.Style({
			fill : new ol.style.Fill({
				color : 'rgba(0,255,255,0.5)'
			}),
			stroke : new ol.style.Stroke({
				color : '#0ff',
				width : 1
			})
		}) ],
		'MultiPoint' : [ new ol.style.Style({
			image : new ol.style.Circle({
				fill : new ol.style.Fill({
					color : 'rgba(255,0,255,0.5)'
				}),
				radius : 5,
				stroke : new ol.style.Stroke({
					color : '#f0f',
					width : 1
				})
			})
		}) ],
		'MultiLineString' : [ new ol.style.Style({
			stroke : new ol.style.Stroke({
				color : '#0f0',
				width : 3
			})
		}) ],
		'MultiPolygon' : [ new ol.style.Style({
			fill : new ol.style.Fill({
				color : 'rgba(0,0,255,0.5)'
			}),
			stroke : new ol.style.Stroke({
				color : '#00f',
				width : 1
			})
		}) ]
	};

	// custom text depending on feature name
	createTextStyle = function(feature, offset) {
		return new ol.style.Text({
			text : feature.get('name'),
			fill : new ol.style.Fill({
				color : 'green'
			}),
			stroke : new ol.style.Stroke({
				color : 'white',
				width : 3
			}),
			font : 'Normal' + ' ' + '12px' + ' ' + 'Arial',
			offsetX : offset.x,
			offsetY : offset.y,
			textAlign : 'Center',
			textBaseline : 'Middle',
		});
	}

	// custom point style depending on feature
	createPointStyle = function() {
		return function(feature, resolution) {
			var offset = [];
			offset.x = 0;
			offset.y = 0;
			var pointStyle = new ol.style.Style({
				image : new ol.style.Circle({
					fill : new ol.style.Fill({
						color : 'rgba(255,0,0,0.5)'
					}),
					radius : 2,
					stroke : new ol.style.Stroke({
						color : '#ff0',
						width : 1
					})
				}),
			// text : createTextStyle(feature, offset)
			})
			return [ pointStyle ];
		}
	}

	// custom point style depending on feature
	createIconStyle = function() {
		return function(feature, resolution) {
			var iconWidth = 256;
			var scale = 0.3;
			var anchorX = -(iconWidth * scale / 2);
			var anchorY = -(iconWidth * scale / 2);
			var opacity = 1;
			var offset = [];
			offset.x = anchorX;
			offset.y = anchorY;
			var pointStyle = new ol.style.Style({
				image : new ol.style.Icon(({
					anchor : [ 0.5, 0.5 ],
					scale : scale,
					anchorXUnits : 'fraction',
					anchorYUnits : 'fraction',
					// anchorYUnits : 'pixels',
					opacity : opacity,
					src : 'resources/' + feature.get('name') + '.png'
				})),
				text : createTextStyle(feature, offset)
			})
			return [ pointStyle ];
		}
	}

	var iconStyle = new ol.style.Style({
		image : new ol.style.Icon(({
			anchor : [ 0, 0 ],
			scale : 0.5,
			anchorXUnits : 'fraction',
			anchorYUnits : 'pixels',
			opacity : 0.75,
			src : 'resources/scruffy.png'
		}))
	});

	// layers to have something to show
	var layers = [];
	layers[0] = new ol.layer.Tile({
		source : new ol.source.MapQuest({
			layer : 'sat'
		})
	});
	layers[0].setVisible(true);
	layers[1] = new ol.layer.Group({
		layers : [ new ol.layer.Tile({
			source : new ol.source.MapQuest({
				layer : 'sat'
			})
		}), new ol.layer.Tile({
			source : new ol.source.MapQuest({
				layer : 'hyb'
			})
		}) ]
	});

	layers[2] = new ol.layer.Tile({
		source : new ol.source.MapQuest({
			layer : 'osm'
		})
	});
	layers[3] = new ol.layer.Tile({
		source : new ol.source.OSM()
	});
	for (i = 1, ii = layers.length; i < ii; ++i)
		layers[i].setVisible(false);
	$scope.switchLayer = function() {
		for (i = 0, ii = layers.length; i < ii; ++i)
			layers[i].setVisible(i == $scope.layerName);
	}

	var businessObjectsLayer;

	var geoj = new ol.source.GeoJSON({
		projection : 'EPSG:3857',
		url : 'data/point-samples.geojson'
	});

	var points = new ol.layer.Vector({
		source : geoj,
		style : iconStyle
	// style : defaultStyle['Point']
	});
	var geojbo = new ol.source.GeoJSON({
		projection : 'EPSG:3857',
		url : 'data/sample.geojson'
	});

	var pointsbo = new ol.layer.Vector({
		source : geojbo,
		style : iconStyle
	});

	// the map (default renderer)
	var map = new ol.Map({
		controls : ol.control.defaults(),
		target : 'map',
		layers : layers,
		view : new ol.View({
			// projection: 'EPSG:4326',
			projection : 'EPSG:3857',
			// center : ol.proj.transform([ 37.41, 8.82 ], 'EPSG:4326',
			// 'EPSG:3857'),
			center : [ 0, 0 ],
			zoom : 4
		})
	});

	buildGeoForm = function() {
		var geoForm = document.getElementById('geoform');
		var select = d3.select("#geoform");
		select.style({
			"visibility" : "visible",
			"bottom" : "0px",
			"position" : "relative"
		});

		return geoForm;
	}

	// control callback
	addGeoJSONCallback = function() {
		map.getLayers().push(points);
		var view = map.getView();
		console.log("extent " + geoj.getExtent() + " size " + map.getSize());
		view.fitExtent(geoj.getExtent(), map.getSize());
	}

	// control callback
	pointToCoordCallback = function() {
		console.log("toubidou");
	}

	// show form overlay callback
	showFormCallback = function() {
		console.log("toubidou");
		var select = d3.select("#formControl");
		select.style("visibility", "visible");
	}

	// using the restful client factory to interact with the
	// server to handle
	// business objects
	var businessObjectsSource = new ol.source.GeoJSON({
		projection : 'EPSG:4326'
	});
	var businessObjectsLayer = new ol.layer.Vector({
		source : businessObjectsSource,
		style : createPointStyle()
	});

	// businessObjectsSource.on("change", function() {
	// console.log("changed");
	// $scope.monitormodel.count = businessObjectsSource.getFeatures().size();
	// });
	map.getLayers().push(businessObjectsLayer);
	visualizeBOCallback = function() {
		geoFactory.getGeoEntity().success(function(geoEntity) {
			var parser = new ol.format.GeoJSON();
			// this is very uneffective, but I cannot get it working unless
			// sending 3857 from the server, which I refuse to do.
			geoEntity.features.forEach(function(feature) {
				feature.geometry.coordinates = ol.proj.transform(feature.geometry.coordinates, 'EPSG:4326', 'EPSG:3857')
			})
			feats = parser.readFeatures(geoEntity);
			businessObjectsSource.clear();
			businessObjectsSource.addFeatures(feats);
			// object : is another way to populate JSON source
			// source = new ol.source.GeoJSON({
			// projection : 'EPSG:3857',
			// object : geoEntity
			// });
			var view = map.getView();
			// console.log("extent " + source.getExtent() + " size " +
			// map.getSize());

			if (businessObjectsLayer.getSource().getFeatures().length > 1) {
				// view.fitExtent(source.getExtent(), map.getSize());
			}

		}).error(function(error) {
			console.log("error");
		});

	}

	toggleIconStyle = function() {
		isIconStyle = !isIconStyle;
		if (!isIconStyle) {
			businessObjectsLayer.setStyle(createPointStyle())
		} else {
			businessObjectsLayer.setStyle(createIconStyle())
		}

	}
	fitExtentCallBack = function() {
		console.log("fit extent current map extent is " + map.getView().calculateExtent(map.getSize()));
		console.log("extent " + businessObjectsSource.getExtent() + " size " + map.getSize());
		map.getView().fitExtent(businessObjectsSource.getExtent(), map.getSize());
		console.log("extent " + businessObjectsSource.getExtent(), map.getView().calculateExtent(map.getSize()));
	}

	toggleDrawingToolCallback = function() {
		var select = d3.select("#drawingToolForm");
		select.style("visibility", "visible");
	}
	toggleShowPopupCallback = function() {
		isShowPopUpOnMapClick = !isShowPopUpOnMapClick;
	}
	saveBusinessLayerCallback = function() {
		var features = source.getFeatures();
		console.log("features " + features);
		var parser = new ol.format.GeoJSON();
		feats = parser.writeFeatures(features);
		geoFactory.saveFeatures(feats).success(function(data) {
			console.log("saved business layer")
		}).error(function(data) {
			console.log("failed to persist business layer")
		});
	}
	queryAllCallback = function() {
		geoFactory.query().success(function(data) {
			console.log("querying extent")
		});
	}
	queryExtentCallback = function() {
		console.log("installing drag box interaction for querying extent");
		map.addInteraction(dt.dragboxinteration);
	}
	clearFeaturesCallback = function() {
		console.log("clearing all features");
		businessObjectsSource.clear();
	}
	dt.dragboxinteration.on('boxend', function() {
		console.log("received " + this.getGeometry())
		console.log("extent: " + this.getGeometry().getExtent());
		var featureCollection = [];
		var extent = this.getGeometry().getExtent();

		var parser = new ol.format.GeoJSON({
			projection : 'EPSG:3857'
		});

		var geom = parser.writeGeometry(this.getGeometry(), {
			dataProjection : 'EPSG:4326',
			featureProjection : 'EPSG:3857'
		});
		console.log("features: " + geom.coordinates);
		var newgeometry = {};
		newgeometry.coordinates = [];
		for (i = 0; i < geom.coordinates[0].length; i++) {
			console.log("pushing " + geom.coordinates[0][i]);
			newgeometry.coordinates.push(geom.coordinates[0][i]);
		}
		newgeometry.type = geom.type;
		console.log("newgeometry: " + newgeometry);
		geoFactory.query(geom).success(function(data) {
			console.log("querying extent")
		});
		console.log("uninstalling drag box interaction for querying extent");
		map.removeInteraction(dt.dragboxinteration);
	})
	// creating a control
	customControl = function() {
		var toolbar = document.getElementById("toolbar");
		var toggleStyle = document.getElementById("toggleStyle");
		var visualizeBusinessObjects = document.getElementById("visualizeBusinessObjects");
		var showForm = document.getElementById("showForm");
		var addSample = document.getElementById("addSample");
		var fitExtent = document.getElementById("fitExtent");
		var toggleDrawingTool = document.getElementById("toggleDrawingTool");
		var toggleShowPopUpOnClick = document.getElementById("toggleShowPopUpOnClick");
		var saveBusinessLayer = document.getElementById("saveBusinessLayer");
		var queryAll = document.getElementById("queryAll");
		var queryExtent = document.getElementById("queryExtent");
		var clearFeatures = document.getElementById("clearFeatures");
		toggleStyle.addEventListener('click', toggleIconStyle, false);
		visualizeBusinessObjects.addEventListener('click', visualizeBOCallback, false);
		showForm.addEventListener('click', showFormCallback, false);
		addSample.addEventListener('click', addGeoJSONCallback, false);
		fitExtent.addEventListener('click', fitExtentCallBack, false);
		toggleDrawingTool.addEventListener('click', toggleDrawingToolCallback, false);
		toggleShowPopUpOnClick.addEventListener('click', toggleShowPopupCallback, false);
		saveBusinessLayer.addEventListener('click', saveBusinessLayerCallback, false);
		queryAll.addEventListener('click', queryAllCallback, false);
		queryExtent.addEventListener('click', queryExtentCallback, false);
		clearFeatures.addEventListener('click', clearFeaturesCallback, false);
		// binding the control with something in the html
		ol.control.Control.call(this, {
			element : toolbar,
		});
	};
	// basically it just needs to inherit control
	ol.inherits(customControl, ol.control.Control);
	// adding a control
	map.addControl(new ol.control.FullScreen());
	map.addControl(new customControl());

	var mousePositionControl = new ol.control.MousePosition({
		coordinateFormat : ol.coordinate.createStringXY(4),
		projection : 'EPSG:4326',
		className : 'statusbar',
		undefinedHTML : '&nbsp;'
	});
	map.addControl(mousePositionControl);

	// manageing interactions with map
	onClick = function(evt) {
		var coord = evt.coordinate;
		$scope.$apply(function() {
			var hdms = ol.coordinate.toStringHDMS(ol.proj.transform(coord, 'EPSG:3857', 'EPSG:4326'));
			$scope.currentCoordinates = hdms;
			$scope.coord = coord;
		});
	}
	// Popup showing the position the user clicked
	var popup = new ol.Overlay({
		element : document.getElementById('popup')
	});
	map.addOverlay(popup);

	map.on('singleclick', function(evt) {
		onClick(evt);
		var element = popup.getElement();
		coordinate = evt.coordinate;
		var hdms = ol.coordinate.toStringHDMS(ol.proj.transform(coordinate, 'EPSG:3857', 'EPSG:4326'));
		$scope.hdms = hdms;
		popup.setPosition(coordinate);
		if (isShowPopUpOnMapClick) {
			$(popup.getElement()).popover({
				'placement' : 'top',
				'animation' : true,
				'html' : true,
				'content' : buildGeoForm()
			});
			$(element).popover('show');
		}
	});
	// manage key events for the whole body
	d3.select("body").on("keydown", function(d) {
		if (d3.event.keyCode == 27) // the famous ESC key ...
		{
			console.log("ESC pressed");
			var element = popup.getElement();
			$(element).popover('hide');

			var select = d3.select("#drawingToolForm");
			select.style("visibility", "hidden");

			dt.removeDrawingOverlay(map);
		}
	});

	$scope.submitFormItem = function() {
		console.log("submitting " + $scope.formhelper.name + " @ " + $scope.currentCoordinates);
		var characterEntity = {};
		characterEntity.id = uuidFactory.generateUUID();
		characterEntity.name = $scope.formhelper.name;
		characterEntity.catchphrase = $scope.formhelper.catchphrase;

		var feature = {};
		coordinate = ol.proj.transform(coordinate, 'EPSG:3857', 'EPSG:4326');
		console.log("  " + coordinate);

		feature.geometry = {
			"type" : "Point",
			"coordinates" : coordinate
		};
		feature.properties = {
			"name" : $scope.formhelper.name
		}
		feature.type = "Feature";

		characterEntity.geom = feature;
		boManagerFactory.createCharacter(characterEntity);
	}

	console.log("creating controls");
	d3.select("#startPolygon").on("click", function(d) {
		console.log("clicked");
		dt.createControls(map, document.getElementById("drawingToolForm"), document.getElementById('startPolygon'), 'Polygon')
	});
	d3.select("#startPolyline").on("click", function(d) {
		dt.createControls(map, document.getElementById("drawingToolForm"), document.getElementById('startPolyline'), 'LineString')
	});
	d3.select("#startCircle").on("click", function(d) {
		console.log("clicked");
		dt.createControls(map, document.getElementById("drawingToolForm"), document.getElementById('startCircle'), 'Circle')
	});
	d3.select("#startPoint").on("click", function(d) {
		console.log("clicked");
		dt.createControls(map, document.getElementById("drawingToolForm"), document.getElementById('startPoint'), 'Point')
	});

	refresherFactory.webSocket.onmessage = function(event) {
		try {
			var receivedFeature = JSON.parse(event.data);
			// console.log("received JSON: " + receivedFeature.type + " geom: "
			// +
			// receivedFeature.geometry);
			var parser = new ol.format.GeoJSON();
			// I still have to convert on the fly when I receive coords, because
			// source & view need to be in the same projection
			receivedFeature.geometry.coordinates = ol.proj.transform(receivedFeature.geometry.coordinates, 'EPSG:4326', 'EPSG:3857');
			// console.log("geom: " + receivedFeature.geometry.coordinates);
			feats = parser.readFeatures(receivedFeature, {
				projection : 'EPSG:4326'
			});
			businessObjectsSource.addFeatures(feats);
			if (businessObjectsSource.getFeatures().length % 100 == 1) {
				console.log("features: " + businessObjectsSource.getFeatures().length);
				// $scope.$apply(function() {
				// $scope.monitormodel.count =
				// businessObjectsSource.getFeatures().length;
				// })
			}

		} catch (e) {
			console.log("Failed to parse JSON websocket payload " + e);
		}
	};

})

var module = angular.module('blossom.map', [ 'ngRoute' ]);

module.controller('MapCtrl', function($scope) {
	var navbarul = d3.selectAll('ul#navbarul>li');
	navbarul.attr("class", null);
	d3.select('#mapNavItem').attr("class", "active");
	$scope.layerName = "0";

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

	var iconStyle = new ol.style.Style({
		image : new ol.style.Icon(({
			anchor : [ 0.5, 46 ],
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

	var geoj = new ol.source.GeoJSON({
		projection : 'EPSG:3857',
		url : 'data/point-samples.geojson'
	});

	var points = new ol.layer.Vector({
		source : geoj,
		style : iconStyle
	// style : defaultStyle['Point']
	});

	// the map (default renderer)
	var map = new ol.Map({
		controls : ol.control.defaults(),
		target : 'map',
		layers : layers,
		view : new ol.View({
			center : ol.proj.transform([ 37.41, 8.82 ], 'EPSG:4326', 'EPSG:3857'),
			zoom : 4
		})
	});

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
	// creating a control
	customControl = function() {
		var toolbar = document.getElementById("toolbar");
		var getJson = document.getElementById("geoJson");
		var addObject = document.getElementById("addObject");
		getJson.addEventListener('click', addGeoJSONCallback, false);
		addObject.addEventListener('click', pointToCoordCallback, false);
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
		});
	}
	// Popup showing the position the user clicked
	var popup = new ol.Overlay({
		element : document.getElementById('popup')
	});
	map.addOverlay(popup);

	buildGeoForm = function() {
		var attrs = [ 'toubidou', 'scrapidou', 'roubidou' ];
		var geoForm = document.getElementById('geoform');
		console.log(geoForm);
		var div = d3.select(geoForm);
		return geoForm;
	}

	map.on('singleclick', function(evt) {
		onClick(evt);
		var element = popup.getElement();
		var coordinate = evt.coordinate;
		var hdms = ol.coordinate.toStringHDMS(ol.proj.transform(coordinate, 'EPSG:3857', 'EPSG:4326'));

		$(element).popover('destroy');
		popup.setPosition(coordinate);
		// the keys are quoted to prevent renaming in ADVANCED_OPTIMIZATIONS
		// mode.
		$(element).popover({
			'placement' : 'top',
			'animation' : true,
			'html' : true,
			// 'content' : '<p>The location you clicked was:</p><code>' +
			// $scope.currentCoordinates + '</code>'
			'content' : buildGeoForm()
		});
		$(element).popover('show');
	});
	// manage key events for the whole body
	d3.select("body").on("keydown", function(d) {
		if (d3.event.keyCode == 27) // the famous ESC key ...
		{
			console.log("ESC pressed");
			var element = popup.getElement();
			$(element).popover('destroy');
		}
	});
	$scope.submitFormItem = function() {
		console.log("toubidou");
		console.log("form  " + $scope.formhelper.name);
	}
})

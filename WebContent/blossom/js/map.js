var module = angular.module('blossom.map', [ 'ngRoute' ]);

module.controller('MapCtrl', function($scope) {
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

	var layers = [];
	layers[0] = new ol.layer.Tile({
		source : new ol.source.MapQuest({
			layer : 'sat'
		})
	});
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
		style : defaultStyle['Point']
	});

	var map = new ol.Map({
		controls : ol.control.defaults(),
		target : 'map',
		layers : layers,
		view : new ol.View({
			center : ol.proj.transform([ 37.41, 8.82 ], 'EPSG:4326', 'EPSG:3857'),
			zoom : 4
		})
	});
	submitFeature = function() {
		console.log("haha");
		map.getLayers().push(points);
		var view = map.getView();
		console.log("extent " + geoj.getExtent() + " size " + map.getSize());
		view.fitExtent(geoj.getExtent(), map.getSize());
	}
	// creating a control
	customControl = function() {
		var wesh = document.getElementById("wesh");
		var getJson = document.getElementById("wesh");
		getJson.addEventListener('click', submitFeature, false);
		// binding the control with something in the html
		ol.control.Control.call(this, {
			element : wesh,
		});
	};
	// basically it just needs to inherit control
	ol.inherits(customControl, ol.control.Control);
	// adding a control
	map.addControl(new ol.control.FullScreen());
	map.addControl(new customControl());

})

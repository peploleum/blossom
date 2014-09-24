var module = angular.module('blossom.map', [ 'ngRoute' ]);

module.factory('geoFactory', [ '$http', function($http) {

	var urlBase = './rest/geo';
	var geoFactory = {};

	geoFactory.getGeoEntity = function() {
		return $http.get(urlBase + '/' + 'getgeoentity');
	};

	geoFactory.addFeature = function(feature) {
		return $http.put(urlBase + '/' + 'addfeature', feature);
	};

	return geoFactory;
} ]);

module
		.controller(
				'MapCtrl',
				function($scope, geoFactory) {
					$scope.currentCoordinates;
					var coordinate;
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
							center : ol.proj.transform([ 37.41, 8.82 ], 'EPSG:4326', 'EPSG:3857'),
							zoom : 4
						})
					});

					buildGeoForm = function() {
						// var attrs = [ 'toubidou', 'scrapidou', 'roubidou' ];
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
					addGeoJSONBOCallback = function() {
						map.getLayers().push(pointsbo);
						var view = map.getView();
						console.log("extent bo " + geojbo.getExtent() + " size " + map.getSize());
						view.fitExtent(geojbo.getExtent(), map.getSize());
					}

					// control callback
					pointToCoordCallback = function() {
						console.log("toubidou");
					}

					// show form overlay callback
					showFormCallback = function() {
						console.log("toubidou");
						var elementById = document.getElementById("formControl");
						var select = d3.select("#formControl");
						select.style("visibility", "visible");
						console.log(elementById);
					}

					// using the restful client factory to interact with the
					// server to handle
					// business objects

					visualizeBOCallback = function() {
						geoFactory
								.getGeoEntity()
								.success(
										function(geoEntity) {
											console.log("success " + geoEntity.features);
											var parser = new ol.format.GeoJSON();
											// var correct = "{\"crs\" :
											// {\"properties\" : {\"name\" :
											// \"urn:ogc:def:crs:OGC:1.3:CRS84\"}
											// ,\"type\" :
											// \"name\"},\"features\" : [
											// {\"geometry\" : { \"type\" :
											// \"Point\",\"coordinates\" : [
											// -75.8492535793898,
											// 47.6434349837781 ]
											// },\"properties\" : {\"name\" :
											// \"Saguenay (Arrondissement
											// Latterière)\"} , \"type\" :
											// \"Feature\" } ], \"type\" :
											// \"FeatureCollection\" }"
											// var correct = "{\"crs\" :
											// {\"properties\" : {\"name\"
											// :\"urn:ogc:def:crs:OGC:1.3:CRS84\"}
											// ,\"type\" :\"name\"},\"features\"
											// : [ {\"geometry\" : { \"type\"
											// :\"Point\",\"coordinates\" : [
											// 26.0, 14.0 ] },\"properties\"
											// :{\"name\" : \"Saguenay
											// (Arrondissement Latterière)\"} ,
											// \"type\" : \"Feature\" } ],
											// \"type\" : \"FeatureCollection\"
											// }"
											var correct = "{\"crs\" : {\"properties\" : {\"name\" :\"EPSG:4326\"} ,\"type\" :\"name\"},\"features\" : [ {\"geometry\" : { \"type\" :\"Point\",\"coordinates\" : [ 26.0, 14.0 ] },\"properties\" :{\"name\" : \"Saguenay (Arrondissement Latterière)\"} , \"type\" : \"Feature\" } ], \"type\" : \"FeatureCollection\" }"
											feats = parser.readFeatures(geoEntity);
											console.log("proj " + parser.readProjection(geoEntity).getCode());
											feats.forEach(function(feat) {
												console.log("feat : " + feat);
												console.log("geom " + feat.getGeometry().getType());
												console.log("geom coord " + feat.getGeometry().getCoordinates());
//												feat.getGeometry().transform('EPSG:4326', 'EPSG:3857');
												console.log("geom coord " + feat.getGeometry().getCoordinates());
											});
											var source = new ol.source.GeoJSON({
												projection : 'EPSG:3857'
											});
											var businessObjectsLayer = new ol.layer.Vector({
												source : source,
												style : iconStyle
											});
											businessObjectsLayer.getSource().addFeatures(feats);
											// console.log(businessObjectsLayer.getFeatures());
											map.getLayers().remove(businessObjectsLayer);
											map.getLayers().push(businessObjectsLayer);
											var view = map.getView();
											console.log("extent " + source.getExtent() + " size " + map.getSize());
											// view.fitExtent(source.getExtent(),
											// map.getSize());
										}).error(function(error) {
									console.log("error");
								});

					}

					// creating a control
					customControl = function() {
						var toolbar = document.getElementById("toolbar");
						var getJson = document.getElementById("geoJson");
						var visualizeBusinessObjects = document.getElementById("visualizeBusinessObjects");
						var showForm = document.getElementById("showForm");
						getJson.addEventListener('click', addGeoJSONCallback, false);
						visualizeBusinessObjects.addEventListener('click', visualizeBOCallback, false);
						showForm.addEventListener('click', showFormCallback, false);
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

					$(popup.getElement()).popover({
						'placement' : 'top',
						'animation' : true,
						'html' : true,
						'content' : buildGeoForm()
					});

					map.on('singleclick', function(evt) {
						onClick(evt);
						var element = popup.getElement();
						coordinate = evt.coordinate;
						var hdms = ol.coordinate.toStringHDMS(ol.proj.transform(coordinate, 'EPSG:3857', 'EPSG:4326'));
						$scope.hdms = hdms;
						popup.setPosition(coordinate);

						$(element).popover('show');
					});
					// manage key events for the whole body
					d3.select("body").on("keydown", function(d) {
						if (d3.event.keyCode == 27) // the famous ESC key ...
						{
							console.log("ESC pressed");
							var element = popup.getElement();
							$(element).popover('hide');
						}
					});

					$scope.submitFormItem = function() {
						console.log("submitting " + $scope.formhelper.name + " @ " + $scope.currentCoordinates);
						var feature = {};
						// ol.coordinate.
//						coordinate = ol.proj.transform(coordinate, 'EPSG:3857', 'EPSG:4326');
						console.log("  " + coordinate);

						feature.geometry = {
							"type" : "Point",
							"coordinates" : coordinate
						};
						feature.properties = {
							"name" : "awesome business Object"
						}
						feature.type = "Feature";
						geoFactory.addFeature(feature);
					}
				})

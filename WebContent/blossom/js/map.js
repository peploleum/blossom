var module = angular.module('blossom.map', [ 'ngRoute' ]);

module.controller('MapCtrl', function($scope) {
	var exampleNS = {};

	exampleNS.getRendererFromQueryString = function() {
		var obj = {}, queryString = location.search.slice(1), re = /([^&=]+)=([^&]*)/g, m;

		while (m = re.exec(queryString)) {
			obj[decodeURIComponent(m[1])] = decodeURIComponent(m[2]);
		}
		if ('renderers' in obj) {
			return obj['renderers'].split(',');
		} else if ('renderer' in obj) {
			return [ obj['renderer'] ];
		} else {
			return undefined;
		}
	};
	
	customControl = function() {
		console.log("paf");
		var weshDiv = document.getElementById("wesh");
		ol.control.Control.call(this, {
			element : weshDiv,
		// target: options.target
		});
	};
	ol.inherits(customControl, ol.control.Control);

	var map = new ol.Map({
		controls : ol.control.defaults(),
		target : 'map',
		layers : [ new ol.layer.Tile({
			source : new ol.source.MapQuest({
				layer : 'sat'
			})
		}) ],
		renderer : exampleNS.getRendererFromQueryString(),
		view : new ol.View({
			center : ol.proj.transform([ 37.41, 8.82 ], 'EPSG:4326', 'EPSG:3857'),
			zoom : 4
		})
	});
	// new ol.control.FullScreen()
	map.addControl(new ol.control.FullScreen());
	map.addControl(new customControl());
//	map.addControl(new paf());
})

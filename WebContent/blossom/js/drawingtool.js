var dt = new Object();
dt.polygon = {
	"type" : "polygon"
};

dt.createControls = function(map, linkedElement) {
	dtControls = function() {
		startPolygon.addEventListener('click', dt.initDrawingOverlay(map, 'Polygon'), false);
		// binding the control with something in the html
		ol.control.Control.call(this, {
			element : linkedElement,
		});
	};
	// basically it just needs to inherit control
	ol.inherits(dtControls, ol.control.Control);
	// adding the drawing control
	map.addControl(new dtControls());
}
dt.initDrawingOverlay = function(map, geometryType) {
	console.log("calling drawing callback");
	var currentDrawnType = geometryType;

	// The features are not added to a regular vector layer/source,
	// but to a feature overlay which holds a collection of features.
	// This collection is passed to the modify and also the draw
	// interaction, so that both can add or modify features.
	var featureOverlay = new ol.FeatureOverlay({
		style : new ol.style.Style({
			fill : new ol.style.Fill({
				color : 'rgba(255, 255, 255, 0.2)'
			}),
			stroke : new ol.style.Stroke({
				color : '#ffcc33',
				width : 2
			}),
			image : new ol.style.Circle({
				radius : 7,
				fill : new ol.style.Fill({
					color : '#ffcc33'
				})
			})
		})
	});
	featureOverlay.setMap(map);

	var modify = new ol.interaction.Modify({
		features : featureOverlay.getFeatures(),
		// the SHIFT key must be pressed to delete vertices, so
		// that new vertices can be drawn at the same position
		// of existing vertices
		deleteCondition : function(event) {
			return ol.events.condition.shiftKeyOnly(event) && ol.events.condition.singleClick(event);
		}
	});
	map.addInteraction(modify);

	var draw; // global so we can remove it later
	function addInteraction() {
		draw = new ol.interaction.Draw({
			features : featureOverlay.getFeatures(),
			type : /** @type {ol.geom.GeometryType} */
			currentDrawnType
		});
		map.addInteraction(draw);
	}

	// think about what to listen to to change the type
	addInteraction();
}

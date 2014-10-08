var dt = new Object();
dt.polygon = {
	"type" : "polygon"
};

dt.createControls = function(map, linkedElement, button, geomType) {
	dtControls = function() {
		linkedElement.addEventListener('click', dt.initDrawingOverlay(map, geomType), false);
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
dt.draw = new Object();
dt.featureOverlay = new ol.FeatureOverlay({
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
dt.initDrawingOverlay = function(map, geometryType) {
	console.log("calling drawing callback");
	var currentDrawnType = geometryType;
	dt.removeDrawingOverlay(map);
	// The features are not added to a regular vector layer/source,
	// but to a feature overlay which holds a collection of features.
	// This collection is passed to the modify and also the draw
	// interaction, so that both can add or modify features.
	dt.featureOverlay.setMap(map);

	var modify = new ol.interaction.Modify({
		features : dt.featureOverlay.getFeatures(),
		// the SHIFT key must be pressed to delete vertices, so
		// that new vertices can be drawn at the same position
		// of existing vertices
		deleteCondition : function(event) {
			return ol.events.condition.shiftKeyOnly(event) && ol.events.condition.singleClick(event);
		}
	});
	map.addInteraction(modify);

	// global so we can remove it later
	function addInteraction() {
		dt.draw = new ol.interaction.Draw({
			features : dt.featureOverlay.getFeatures(),
			type : /** @type {ol.geom.GeometryType} */
			currentDrawnType
		});
		map.addInteraction(dt.draw);
	}

	// think about what to listen to to change the type
	addInteraction();
}

dt.removeDrawingOverlay = function(map) {
	map.removeInteraction(dt.draw);
	map.removeOverlay(dt.featureOverlay);
}

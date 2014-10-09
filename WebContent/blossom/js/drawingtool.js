var dt = new Object();

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
	dt.featureOverlay.setMap(map);

	var modify = new ol.interaction.Modify({
		features : dt.featureOverlay.getFeatures(),
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

	addInteraction();
}

dt.removeDrawingOverlay = function(map) {
	map.removeInteraction(dt.draw);
	map.removeOverlay(dt.featureOverlay);
}

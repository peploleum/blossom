var ci = new Object();

ci.CircleInteraction = function() {
	// prototype = ol.interaction.Pointer.prototype;
	ol.interaction.Pointer.call(this);
};
// ci.CircleInteraction.prototype = new Object();
// ci.CircleInteraction.prototype = new Object();
ol.inherits(ci.CircleInteraction, ol.interaction.Pointer);
// ci.CircleInteraction.prototype.handleMapBrowserEvent = function(event) {
// var map = event.map;
// console.log("there was a map browser event");
// if (!map.isDef()) {
// return true;
// }
// var pass = true;
// if (event.type === ol.MapBrowserEvent.EventType.POINTERMOVE) {
// pass = this.handlePointerMove_(event);
// } else if (event.type === ol.MapBrowserEvent.EventType.DBLCLICK) {
// pass = false;
// }
// return true;
// };

package blossom.restful.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import blossom.restful.service.tech.geo.GeoConsumerService;
import blossom.restful.service.tech.geo.GeoService;

/**
 * Entry point for extent based queries that pipe their results throug a WebSocket.
 *
 * @author peploleum
 *
 */
@Path("geoquery")
public class GeoQueryRestService {
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void submitExtentQuery(final String extent) {
        final GeoService geoService = new GeoService();
        final GeoConsumerService consumerService = new GeoConsumerService(geoService.getFeatureQueue());
        geoService.produceGeoEntities();
        consumerService.startConsuming();
    }
}

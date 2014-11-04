package blossom.restful.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import blossom.restful.service.business.geo.dto.PolygonGeometry;
import blossom.restful.service.tech.geo.GeoConsumerDao;
import blossom.restful.service.tech.geo.GeoDao;

/**
 * Entry point for extent based queries that pipe their results throug a WebSocket.
 *
 * @author peploleum
 *
 */
@Path("geoquery")
public class GeoQueryRestService {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void submitExtentQuery(final PolygonGeometry extent) {
        final GeoDao geoService = new GeoDao(extent);
        final GeoConsumerDao consumerService = new GeoConsumerDao(geoService.getFeatureQueue());
        geoService.produceGeoEntities();
        consumerService.startConsuming();
    }
}

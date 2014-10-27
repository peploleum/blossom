package blossom.restful.rest;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import blossom.exception.TopLevelBlossomException;
import blossom.restful.service.business.geo.GeoEntitiesService;
import blossom.restful.service.business.geo.dto.Feature;
import blossom.restful.service.business.geo.dto.GeoEntity;

@Path("/geo")
public class GeoEntityRestService {
    private static final Logger LOGGER = Logger.getLogger(GeoEntityRestService.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getgeoentity")
    public GeoEntity getGeoEntity() throws TopLevelBlossomException {
        LOGGER.info("getting geoentities");
        final GeoEntitiesService geoEntitiesTransfer = new GeoEntitiesService();
        return geoEntitiesTransfer.getGeoEntity();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addfeature")
    public void addFeature(final Feature feature) throws TopLevelBlossomException {
        LOGGER.info("adding Feature ");
        final GeoEntitiesService geoEntitiesTransfer = new GeoEntitiesService();
        geoEntitiesTransfer.addFeature(feature);

    }

    @PUT
//    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/savefeatures")
    public void saveFeatures() throws TopLevelBlossomException {
        LOGGER.info("saving Features ");
        final GeoEntitiesService geoEntitiesTransfer = new GeoEntitiesService();
//        if (features == null || features.length == 0)
            geoEntitiesTransfer.saveFeatures();

    }
}

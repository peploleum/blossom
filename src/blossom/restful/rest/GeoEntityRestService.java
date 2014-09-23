package blossom.restful.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import blossom.restful.geo.entity.Feature;
import blossom.restful.geo.entity.GeoEntity;
import blossom.restful.service.geo.GeoEntitiesSingleton;

@Path("/geo")
public class GeoEntityRestService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GeoEntity getGraph() {
        // to be able to return a marshalled json object based on a Jaxb bean it
        // seems we need to deploy heavy artillery with glassfish servlet + moxy
        // ...
        return new GeoEntity();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addGeoEntity")
    public void addNode(final Feature feature) {
        final GeoEntitiesSingleton ges = GeoEntitiesSingleton.getInstance();
        ges.addFeature(feature);
    }

}

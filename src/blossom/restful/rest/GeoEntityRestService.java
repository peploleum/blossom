package blossom.restful.rest;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import blossom.restful.geo.entity.Feature;
import blossom.restful.geo.entity.GeoEntity;
import blossom.restful.service.geo.GeoEntitiesSingleton;
import blossom.websocket.BusinessLayerEndpointConfiguration;

@Path("/geo")
public class GeoEntityRestService {
    private static final Logger LOGGER = Logger.getLogger(GeoEntityRestService.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getgeoentity")
    public GeoEntity getGeoEntity() {
        final GeoEntitiesSingleton ges = GeoEntitiesSingleton.getInstance();

        // for now, for debug, we marshall it this way to check what's coming
        // out against what's coming in on clien-side.
        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance(GeoEntity.class);
            final Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(MarshallerProperties.JSON_VALUE_WRAPPER, "coordinates");
            marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
            marshaller.marshal(ges.getEntity(), System.out);
        } catch (final JAXBException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return ges.getEntity();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addfeature")
    public void addNode(final Feature feature) {

        JAXBContext jc;
        // we need to marshall it to send it through the websocket pipe
        try {
            StringWriter writer = new StringWriter();
            jc = JAXBContext.newInstance(Feature.class);
            final Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(MarshallerProperties.JSON_VALUE_WRAPPER, "coordinates");
            marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
            marshaller.marshal(feature, writer);
            BusinessLayerEndpointConfiguration.getEndPointSingleton().onMessage(writer.getBuffer().toString(), null);
            final GeoEntitiesSingleton ges = GeoEntitiesSingleton.getInstance();
            ges.addFeature(feature);
        } catch (final JAXBException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

    }
}

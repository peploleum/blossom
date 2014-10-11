package blossom.restful.service.geo.transfer;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import blossom.exception.TopLevelBlossomException;
import blossom.restful.service.geo.GeoEntitiesSingleton;
import blossom.restful.service.geo.dto.Feature;
import blossom.restful.service.geo.dto.GeoEntity;
import blossom.websocket.BusinessLayerEndpointConfiguration;

/**
 * Build GeoEntities from business to persistence and the other way around, to ensure database
 * access is not done directly in the service layer.
 *
 * @author peploleum
 *
 */
public class GeoEntitiesTransfer {
    private static final Logger LOGGER = Logger.getLogger(GeoEntitiesTransfer.class.getName());

    public GeoEntitiesTransfer() {

    }

    /**
     * Retrieve localized entities model
     *
     * @return {@link GeoEntity} serializable as a JSON object
     * @throws TopLevelBlossomException
     *             if persistence access fails
     */
    public GeoEntity getGeoEntity() throws TopLevelBlossomException {
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
            throw new TopLevelBlossomException(e, "failed to get GeoEntities");
        }
        return ges.getEntity();
    }

    /**
     * Add a feature to the geo entities model
     *
     * @param feature
     *            the {@link Feature} to add
     * @throws TopLevelBlossomException
     *             if persistence layer fails
     */
    public void addFeature(final Feature feature) throws TopLevelBlossomException {
        JAXBContext jc;
        // we need to marshall it to send it through the websocket pipe
        try {
            final StringWriter writer = new StringWriter();
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
            throw new TopLevelBlossomException(e, "failed to add feature to geo entites");
        }
    }
}

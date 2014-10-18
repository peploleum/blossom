package blossom.restful.service.geo.transfer;

import java.io.StringWriter;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import blossom.exception.TopLevelBlossomException;
import blossom.persistence.EntityManagerFactorySingleton;
import blossom.persistence.location.Location;
import blossom.restful.service.geo.GeoEntitiesSingleton;
import blossom.restful.service.geo.dto.Feature;
import blossom.restful.service.geo.dto.GeoEntity;
import blossom.websocket.BusinessLayerEndpointConfiguration;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

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

    public void saveFeatures() throws TopLevelBlossomException {
        final GeoEntitiesSingleton geos = GeoEntitiesSingleton.getInstance();
        final EntityManager entityManager = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            for (final Feature feature : geos.getEntity().getFeatures()) {
                // feature.
                final Location location = new Location();
                location.setId(UUID.randomUUID().toString());
                final Geometry geom = wktToGeometry("POINT(" + feature.getGeometry().getCoordinates()[0] + " " + feature.getGeometry().getCoordinates()[1]
                        + ")");
                geom.setSRID(Integer.valueOf(geos.getEntity().getCrs().getProperties().getName().split(":")[1]).intValue());
                if (!geom.getGeometryType().equals("Point")) {
                    throw new TopLevelBlossomException("Geometry must be a point. Got a " + geom.getGeometryType());
                }
                location.setLocation((Point) geom);
                entityManager.persist(location);
            }
            entityManager.getTransaction().commit();
            LOGGER.log(Level.INFO, "Features persisted");
        } catch (final Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new TopLevelBlossomException(e, "Failed to remove Nodes from database.");
        } finally {
            entityManager.close();
        }
    }

    private Geometry wktToGeometry(final String wktPoint) {
        final WKTReader fromText = new WKTReader();
        Geometry geom = null;
        try {
            geom = fromText.read(wktPoint);
        } catch (final ParseException e) {
            throw new RuntimeException("Not a WKT string:" + wktPoint);
        }
        return geom;
    }
}

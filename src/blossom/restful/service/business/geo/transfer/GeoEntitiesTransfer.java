package blossom.restful.service.business.geo.transfer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import blossom.exception.TopLevelBlossomException;
import blossom.persistence.EntityManagerFactorySingleton;
import blossom.persistence.location.Location;
import blossom.restful.service.business.geo.GeoEntitiesSingleton;
import blossom.restful.service.business.geo.dto.Feature;
import blossom.restful.service.business.geo.dto.GeoEntity;
import blossom.util.geo.GeoUtils;
import blossom.websocket.BusinessLayerEndpointConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

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
        // we need to marshall it to send it through the websocket pipe
        try {
            final StringWriter writer = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(writer, feature);
            BusinessLayerEndpointConfiguration.getEndPointSingleton().onMessage(writer.getBuffer().toString(), null);
            final GeoEntitiesSingleton ges = GeoEntitiesSingleton.getInstance();
            ges.addFeature(feature);
        } catch (final IOException e) {
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
                final Geometry geom = GeoUtils.wktToGeometry(GeoUtils.fromFeature(feature));
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

}

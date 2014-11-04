package blossom.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import blossom.exception.TopLevelBlossomException;
import blossom.persistence.EntityManagerFactorySingleton;
import blossom.persistence.entity.Location;
import blossom.restful.service.business.geo.dto.Feature;
import blossom.util.geo.GeoUtils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class GeoServiceTest {

    private static final Logger LOGGER = Logger.getLogger(GeoServiceTest.class.getName());

    public static void main(final String[] args) throws TopLevelBlossomException {
        // final GeoService gs = new GeoService();
        // gs.produceGeoEntities();
        
        
        randomPopDb();
    }

    public static void randomPopDb() throws TopLevelBlossomException {
        final EntityManager em = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();

        try {
            do {
                em.getTransaction().begin();
                try {
                    Feature randomFeature = GeoUtils.randomFeature();
                    final Location location = new Location();
                    location.setId(randomFeature.getProperties().getName());
                    final Geometry geometry = GeoUtils.wktToGeometry(GeoUtils.fromFeature(randomFeature));
                    geometry.setSRID(4326);
                    location.setLocation((Point) geometry);
                    em.persist(location);
                    em.getTransaction().commit();
                } catch (final Exception e) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    throw new TopLevelBlossomException(e, "Failed to create Character in database.");
                }

            } while (getHitCount(em) < 10000);
        } finally {
            em.close();
        }
    }

    private static long getHitCount(final EntityManager em) {
        try {
            final Query queryTotal = em.createQuery("Select count(l.id) from Location l");
            final long countResult = (long) queryTotal.getSingleResult();
            return countResult;
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to count results", e);
            return 0;
        }
    }
}

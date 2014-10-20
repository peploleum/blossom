package blossom.restful.service.tech.geo;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import blossom.framework.BlossomProducer;
import blossom.persistence.EntityManagerFactorySingleton;
import blossom.persistence.location.Location;
import blossom.restful.service.business.geo.dto.Feature;
import blossom.restful.service.business.geo.dto.GeoEntity;
import blossom.restful.service.business.geo.dto.Geometry;
import blossom.restful.service.business.geo.dto.PolygonGeometry;
import blossom.restful.service.business.geo.dto.Property;
import blossom.util.geo.GeoUtils;

public class GeoService implements BlossomProducer<Feature> {
    private static final Logger LOGGER = Logger.getLogger(GeoService.class.getName());

    private final ConcurrentLinkedQueue<Feature> featureQueue = new ConcurrentLinkedQueue<Feature>();

    private final PolygonGeometry extent;

    private String qlCountString;

    private String qlString;

    public GeoService(final PolygonGeometry extent) {
        this.extent = extent;
    }

    public long getHitCount() {
        this.qlCountString = "Select count(l.id) from Location l";
        this.qlString = "Select l from Location l";
        com.vividsolutions.jts.geom.Geometry extractWkt = extractWkt();
        if (extractWkt != null) {
            this.qlCountString += " where within(l.location, :extractWkt) = true";
        }
        final EntityManager entityManager = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();
        try {
            final Query queryTotal = entityManager.createQuery(this.qlCountString);
            if (extractWkt != null)
                queryTotal.setParameter("extractWkt", extractWkt);

            final long countResult = (long) queryTotal.getSingleResult();
            LOGGER.info("Found " + countResult + " results");
            return countResult;
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to count results", e);
        } finally {
            entityManager.close();
        }
        return 0;
    }

    private com.vividsolutions.jts.geom.Geometry extractWkt() {
        if (this.extent != null) {
            LOGGER.info("extent is " + this.extent.toString());
            LOGGER.info("extent wkt is " + this.extent.toWkt());
            com.vividsolutions.jts.geom.Geometry wktToGeometry = GeoUtils.wktToGeometry(this.extent.toWkt());
            assert (wktToGeometry.getCoordinates().length != 0);
            wktToGeometry.setSRID(4326);
            return wktToGeometry;
        }
        return null;
    }

    private GeoEntity getGeoentity(final Query pagedQuery, final int from, final int howmany) {
        final GeoEntity entity = new GeoEntity();
        pagedQuery.setFirstResult(from);
        pagedQuery.setMaxResults(howmany);
        @SuppressWarnings("unchecked")
        final List<Location> locationList = pagedQuery.getResultList();
        for (final Location location : locationList) {
            LOGGER.log(Level.INFO, "Location fetched: " + location.getId());
            final Feature feature = new Feature();
            final Property properties = new Property();
            properties.setName(location.getId());
            final Geometry geometry = new Geometry();
            geometry.setType("Point");
            final Double[] coo = new Double[] { location.getLocation().getX(), location.getLocation().getY() };
            geometry.setCoordinates(coo);
            feature.setProperties(properties);
            feature.setGeometry(geometry);
            feature.setType("Feature");
            LOGGER.log(Level.INFO, "offering resulting feature");
            this.featureQueue.offer(feature);
        }
        return entity;
    }

    public void produceGeoEntities() {
        final int pageSize = 10;
        final long hitCount = getHitCount();
        final int pageCount = Math.max(1, (int) hitCount / pageSize);
        final int leftOver = (hitCount <= pageSize) ? (int) hitCount : ((int) Math.IEEEremainder(hitCount, pageCount));
        LOGGER.info("producing geo entities with parameters: hit count=" + hitCount + " pageSize=" + pageSize + " pageCount=" + pageCount + " remainder="
                + leftOver);

        final EntityManager entityManager = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();
        try {
            com.vividsolutions.jts.geom.Geometry extractWkt = extractWkt();
            if (extractWkt != null) {
                this.qlString += " where within(l.location, :extractWkt) = true";
            }
            final Query pageQuery = entityManager.createQuery(this.qlString, Location.class);
            if (extractWkt != null)
                pageQuery.setParameter("extractWkt", extractWkt);

            for (int i = 0; i < pageCount; i++) {
                LOGGER.info("producing page " + i + " out of " + (pageCount - 1));
                getGeoentity(pageQuery, i * pageSize, (i == pageCount - 1) ? (leftOver) : pageSize);
            }
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to count results", e);
        } finally {
            LOGGER.info("production ended");
            entityManager.close();
        }

    }

    public ConcurrentLinkedQueue<Feature> getFeatureQueue() {
        return this.featureQueue;
    }

}

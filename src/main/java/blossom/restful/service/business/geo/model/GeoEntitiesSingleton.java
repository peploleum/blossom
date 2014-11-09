package blossom.restful.service.business.geo.model;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import blossom.restful.service.business.geo.dto.CRS;
import blossom.restful.service.business.geo.dto.Feature;
import blossom.restful.service.business.geo.dto.GeoEntity;
import blossom.restful.service.business.geo.dto.Property;

/**
 * Singleton model for geolocalized business objects
 *
 * @author peploleum
 *
 */
public class GeoEntitiesSingleton {

    private static GeoEntitiesSingleton INSTANCE = null;

    private static final Logger LOGGER = Logger.getLogger(GeoEntitiesSingleton.class.getName());

    private GeoEntity entity;

    /**
     * Builds a model for geolocalized business objects
     */
    private GeoEntitiesSingleton() {
        if (this.entity == null) {
            this.entity = new GeoEntity();
            this.entity.setType("FeatureCollection");
            final Property property = new Property();
//            property.setName("EPSG:3857");
            property.setName("EPSG:4326");
            final CRS crs = new CRS();
            crs.setType("name");
            crs.setProperties(property);
            this.entity.setCrs(crs);
            this.entity.setFeatures(new ArrayList<Feature>());
        }
    }

    /**
     * the geo entities model singleton
     *
     * @return {@link GeoEntitiesSingleton}
     */
    public static GeoEntitiesSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GeoEntitiesSingleton();
        }
        return INSTANCE;
    }

    /**
     * gets the serializable model
     *
     * @return
     */
    public GeoEntity getEntity() {
        return this.entity;
    }

    /**
     * set the serializable model
     *
     * @param entity
     *            a not <code>null</code> {@link GeoEntity}
     */
    public void setEntity(final GeoEntity entity) {
        this.entity = entity;
    }

    /**
     * Add a feature to the entity model
     *
     * @param feature
     *            the {@link Feature} to add
     */
    public void addFeature(final Feature feature) {
        LOGGER.log(Level.INFO, "adding entity " + this.entity.toString());
        if (this.entity.getFeatures() == null) {
            this.entity.setFeatures(new ArrayList<Feature>());
        }
        this.entity.getFeatures().add(feature);
    }

}

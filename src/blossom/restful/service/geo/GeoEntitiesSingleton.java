package blossom.restful.service.geo;

import java.util.logging.Level;
import java.util.logging.Logger;

import blossom.restful.geo.entity.Feature;
import blossom.restful.geo.entity.GeoEntity;

public class GeoEntitiesSingleton {

    private static GeoEntitiesSingleton INSTANCE = null;

    private static final Logger LOGGER = Logger.getLogger(GeoEntitiesSingleton.class.getName());

    private GeoEntity entity;

    private GeoEntitiesSingleton() {
        if (this.entity == null) {
            this.entity = new GeoEntity();
        }
    }

    public static GeoEntitiesSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GeoEntitiesSingleton();
        }
        return INSTANCE;
    }

    public GeoEntity getEntity() {
        return this.entity;
    }

    public void setEntity(final GeoEntity entity) {
        this.entity = entity;
    }

    public void addFeature(final Feature feature) {
        LOGGER.log(Level.INFO, "adding entity " + entity.toString());
        this.entity.getFeatures().add(feature);
    }

}

package blossom.restful.service.geo;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import blossom.restful.service.geo.bean.CRS;
import blossom.restful.service.geo.bean.Feature;
import blossom.restful.service.geo.bean.GeoEntity;
import blossom.restful.service.geo.bean.Property;

public class GeoEntitiesSingleton {

    private static GeoEntitiesSingleton INSTANCE = null;

    private static final Logger LOGGER = Logger.getLogger(GeoEntitiesSingleton.class.getName());

    private GeoEntity entity;

    private GeoEntitiesSingleton() {
        if (this.entity == null) {
            this.entity = new GeoEntity();
            this.entity.setType("FeatureCollection");
            final Property property = new Property();
            property.setName("EPSG:3857");
            final CRS crs = new CRS();
            crs.setType("name");
            crs.setProperties(property);
            this.entity.setCrs(crs);
            this.entity.setFeatures(new ArrayList<Feature>());
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
        LOGGER.log(Level.INFO, "adding entity " + this.entity.toString());
        if (this.entity.getFeatures() == null) {
            this.entity.setFeatures(new ArrayList<Feature>());
        }
        this.entity.getFeatures().add(feature);
    }

}

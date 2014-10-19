package blossom.util.geo;

import java.util.Random;
import java.util.UUID;

import blossom.restful.service.business.geo.dto.Feature;
import blossom.restful.service.business.geo.dto.Property;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class GeoUtils {
    public static Geometry wktToGeometry(final String wktPoint) {
        final WKTReader fromText = new WKTReader();
        Geometry geom = null;
        try {
            geom = fromText.read(wktPoint);
        } catch (final ParseException e) {
            throw new RuntimeException("Not a WKT string:" + wktPoint);
        }
        return geom;
    }

    public static String fromFeature(final Feature feature) {
        return "POINT(" + feature.getGeometry().getCoordinates()[0] + " " + feature.getGeometry().getCoordinates()[1] + ")";
    }

    public static Feature randomFeature() {
        final Feature feature = new Feature();
        final Property properties = new Property();
        properties.setName(UUID.randomUUID().toString());
        final blossom.restful.service.business.geo.dto.Geometry geometry = new blossom.restful.service.business.geo.dto.Geometry();
        geometry.setType("Point");
        final double rangeMinLat = -90.000000;
        final double rangeMaxLat = 90.000000;
        final double rangeMinLon = -180.000000;
        final double rangeMaxLon = 180.000000;
        final Random r = new Random();
        final double randomLat = rangeMinLat + (rangeMaxLat - rangeMinLat) * r.nextDouble();
        final double randomLon = rangeMinLon + (rangeMaxLon - rangeMinLon) * r.nextDouble();
        geometry.setCoordinates(new Double[] { randomLon, randomLat });
        feature.setProperties(properties);
        feature.setGeometry(geometry);
        feature.setType("Feature");
        return feature;
    }
}

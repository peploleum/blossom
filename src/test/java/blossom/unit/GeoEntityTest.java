package blossom.unit;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import blossom.restful.service.business.geo.dto.CRS;
import blossom.restful.service.business.geo.dto.Coordinate;
import blossom.restful.service.business.geo.dto.Feature;
import blossom.restful.service.business.geo.dto.GeoEntity;
import blossom.restful.service.business.geo.dto.Geometry;
import blossom.restful.service.business.geo.dto.Property;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GeoEntityTest {
    private static final Logger LOGGER = Logger.getLogger(GeoEntityTest.class.getName());

    public static void main(final String[] args) {

        final Property property = new Property();
        property.setName("urn:ogc:def:crs:OGC:1.3:CRS84");
        final CRS crs = new CRS();
        crs.setType("name");
        crs.setProperties(property);
        final Coordinate coordinate1 = new Coordinate();
        coordinate1.setValue(-75.849253579389796);
        final Coordinate coordinate2 = new Coordinate();
        coordinate2.setValue(47.6434349837781);

        final Geometry geometry = new Geometry();
        geometry.setType("Point");
        geometry.setCoordinates(new Double[] { -75.849253579389796, 47.6434349837781 });
        final Property featureProperty = new Property();
        featureProperty.setName("Saguenay (Arrondissement Latterière)");

        final Feature feature = new Feature();
        feature.setType("Feature");
        feature.setProperties(featureProperty);
        feature.setGeometry(geometry);

        final GeoEntity geoEntity = new GeoEntity();
        geoEntity.setType("FeatureCollection");
        geoEntity.setCrs(crs);
        geoEntity.setFeatures(Arrays.asList(new Feature[] { feature }));

        final ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(System.out, geoEntity);
            // {"type":"FeatureCollection","crs":{"type":"name","properties":{"name":"urn:ogc:def:crs:OGC:1.3:CRS84"}},"features":[{"type":"Feature","properties":{"name":"Saguenay (Arrondissement Latterière)"},"geometry":{"type":"Point","coordinates":[-75.8492535793898,47.6434349837781]}}]}
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "failed to read json", e);
        }

    }
}

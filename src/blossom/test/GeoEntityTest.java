package blossom.test;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import blossom.restful.service.geo.dto.CRS;
import blossom.restful.service.geo.dto.Coordinate;
import blossom.restful.service.geo.dto.Feature;
import blossom.restful.service.geo.dto.GeoEntity;
import blossom.restful.service.geo.dto.Geometry;
import blossom.restful.service.geo.dto.Property;

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
        geometry.setCoordinates(new Object[] { -75.849253579389796, 47.6434349837781 });
        // geometry.setArguments(new Object[] { "a", "b" });
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

        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance(GeoEntity.class);
            final Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(MarshallerProperties.JSON_VALUE_WRAPPER, "coordinates");
            marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
            marshaller.marshal(geoEntity, System.out);
        } catch (final JAXBException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

    }
}

package blossom.unit;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import blossom.restful.service.business.geo.dto.PolygonGeometry;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GeometryTest {
    private static final Logger LOGGER = Logger.getLogger(GeometryTest.class.getName());

    public GeometryTest() {
    }

    @Test
    public void shouldPrintGeometry() {
        final ObjectMapper om = new ObjectMapper();
        PolygonGeometry readValue;
        try {
            readValue = om.readValue(GeometryTest.class.getResource("/geometry.json"), PolygonGeometry.class);
            System.out.println(readValue.getCoordinates());
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "failed to read geometry", e);
        }
    }
}
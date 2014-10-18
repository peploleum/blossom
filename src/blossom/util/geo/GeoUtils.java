package blossom.util.geo;

import blossom.restful.service.geo.dto.Feature;

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

}

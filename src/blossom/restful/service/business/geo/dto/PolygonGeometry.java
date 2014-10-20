package blossom.restful.service.business.geo.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PolygonGeometry {
    private String type;
    private List<List<List<Double>>> coordinates;

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public List<List<List<Double>>> getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(final List<List<List<Double>>> coordinates) {
        this.coordinates = coordinates;
    }

    public String toWkt() {
        // POLYGON((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2))
        final StringBuilder sb = new StringBuilder();
        sb.append("POLYGON(");
        int globcount = 0;
        for (final List<List<Double>> list : this.coordinates) {
            sb.append("(");
            int count = 0;
            for (final List<Double> list2 : list) {
                sb.append(String.valueOf(list2.get(0).doubleValue()));
                sb.append(" ");
                sb.append(String.valueOf(list2.get(1).doubleValue()));
                if (++count != list.size()) {
                    sb.append(",");
                }
            }
            sb.append(")");
            if (++globcount != this.coordinates.size()) {
                sb.append("),");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}

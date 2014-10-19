package blossom.restful.service.business.geo.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Feature {
    private String type;

    public void setType(final String type) {
        this.type = type;
    }

    public void setProperties(final Property properties) {
        this.properties = properties;
    }

    public void setGeometry(final Geometry geometry) {
        this.geometry = geometry;
    }

    private Property properties;

    private Geometry geometry;

    public String getType() {
        return this.type;
    }

    public Property getProperties() {
        return this.properties;
    }

    public Geometry getGeometry() {
        return this.geometry;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Feature [name:");
        sb.append(getProperties().getName());
        sb.append("][coords:");
        final Double[] coordinates = (Double[]) getGeometry().getCoordinates();
        for (final Double d : coordinates) {
            sb.append(d);
            sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }
}

package blossom.restful.geo.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Feature {
    private String type;

    public void setType(final String type) {
        this.type = type;
    }

    public void setProperties(final List<Property> properties) {
        this.properties = properties;
    }

    public void setGeometry(final Geometry geometry) {
        this.geometry = geometry;
    }

    private List<Property> properties;
    private Geometry geometry;

    public String getType() {
        return this.type;
    }

    public List<Property> getProperties() {
        return this.properties;
    }

    public Geometry getGeometry() {
        return this.geometry;
    }

}

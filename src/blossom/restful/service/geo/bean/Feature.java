package blossom.restful.service.geo.bean;

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

}

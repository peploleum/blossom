package blossom.restful.geo.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CRS {
    private String type;
    private Property properties;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Property getProperties() {
        return properties;
    }

    public void setProperties(Property properties) {
        this.properties = properties;
    }

}

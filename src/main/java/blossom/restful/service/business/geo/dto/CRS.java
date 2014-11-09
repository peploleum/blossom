package blossom.restful.service.business.geo.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CRS {
    private String type;
    private Property properties;

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Property getProperties() {
        return this.properties;
    }

    public void setProperties(final Property properties) {
        this.properties = properties;
    }

}

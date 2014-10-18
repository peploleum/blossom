package blossom.restful.service.geo.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Geometry {
    private String type;

//    @XmlAnyElement(lax = true)
    private Double[] coordinates;

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Double[] getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(final Double[] coordinates) {
        this.coordinates = coordinates;
    }
}

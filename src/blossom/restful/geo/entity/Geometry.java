package blossom.restful.geo.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Geometry {
    private String type;

    @XmlAnyElement(lax = true)
    private Object[] coordinates;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Object[] coordinates) {
        this.coordinates = coordinates;
    }
}

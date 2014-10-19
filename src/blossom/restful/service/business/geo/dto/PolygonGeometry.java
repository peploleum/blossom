package blossom.restful.service.business.geo.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PolygonGeometry {
    private String type;
    private Double[][][] coordinates;

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Double[][][] getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(Double[][][] coordinates) {
        this.coordinates = coordinates;
    }
}

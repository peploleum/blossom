package blossom.restful.service.geo.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A bean destined to be serialized to GeoJSON
 *
 * @author peploleum
 *
 */
@XmlRootElement
public class GeoEntity {
    private String type;
    private CRS crs;
    private List<Feature> features;

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public CRS getCrs() {
        return this.crs;
    }

    public void setCrs(final CRS crs) {
        this.crs = crs;
    }

    public List<Feature> getFeatures() {
        return this.features;
    }

    public void setFeatures(final List<Feature> features) {
        this.features = features;
    }

}

package blossom.restful.service.geo.bean;

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
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CRS getCrs() {
        return crs;
    }

    public void setCrs(CRS crs) {
        this.crs = crs;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

}

package blossom.restful.service.business.geo.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FeatureCollection {
    private List<Feature> features = new ArrayList<Feature>();

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FeatureCollection=[");
        for (Feature feature : features) {
            sb.append(feature.toString());
        }
        sb.append("]");
        return sb.toString();
    }
}

package blossom.restful.service.stat;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Stat {
    private List<StatItem> dataset;

    public List<StatItem> getDataset() {
        return this.dataset;
    }

    public void setDataset(final List<StatItem> dataset) {
        this.dataset = dataset;
    }

}

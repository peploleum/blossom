package blossom.restful.stat;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Stat {
    private List<StatItem> dataset;

    public List<StatItem> getDataset() {
        return dataset;
    }

    public void setDataset(List<StatItem> dataset) {
        this.dataset = dataset;
    }

}

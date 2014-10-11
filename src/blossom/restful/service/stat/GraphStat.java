package blossom.restful.service.stat;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GraphStat {
    private Integer max;
    private List<GraphStatItem> map;

    public Integer getMax() {
        return this.max;
    }

    public void setMax(final Integer max) {
        this.max = max;
    }

    public List<GraphStatItem> getMap() {
        return this.map;
    }

    public void setMap(final List<GraphStatItem> stats) {
        this.map = stats;
    }

    public GraphStatItem getByName(final String name) {
        for (final GraphStatItem graphStatItem : this.map) {
            if (name.equals(graphStatItem.getName())) {
                return graphStatItem;
            }
        }
        return null;
    }
}

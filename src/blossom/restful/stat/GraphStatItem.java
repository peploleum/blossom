package blossom.restful.stat;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GraphStatItem {

    private String name;
    private Integer stat;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getStat() {
        return stat;
    }

    public void setStat(final Integer stat) {
        this.stat = stat;
    }

}

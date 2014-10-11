package blossom.restful.stat;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GraphStatItem {

    private String name;
    private Integer stat;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getStat() {
        return this.stat;
    }

    public void setStat(final Integer stat) {
        this.stat = stat;
    }

}

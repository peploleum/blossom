package blossom.restful.graph;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LinkItem {
    private int source;
    private int target;

    public int getSource() {
        return source;
    }

    public void setSource(final int source) {
        this.source = source;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(final int target) {
        this.target = target;
    }

}

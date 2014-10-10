package blossom.restful.service.graph.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LinkItem {
    private int source;
    private int target;

    public int getSource() {
        return this.source;
    }

    public void setSource(final int source) {
        this.source = source;
    }

    public int getTarget() {
        return this.target;
    }

    public void setTarget(final int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("LinkItem[source=");
        sb.append(String.valueOf(this.source));
        sb.append("][target=");
        sb.append(this.target);
        sb.append("]");
        return sb.toString();
    }

}

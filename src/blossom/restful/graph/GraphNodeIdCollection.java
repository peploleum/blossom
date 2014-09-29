package blossom.restful.graph;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Convenient to transmit Collection of Items by their Ids with less bw
 * footprint
 * 
 * @author peploleum
 *
 */
@XmlRootElement
public class GraphNodeIdCollection {
    private String[] ids;

    public String[] getIds() {
        return this.ids;
    }

    public void setIds(final String[] ids) {
        this.ids = ids;
    }

}

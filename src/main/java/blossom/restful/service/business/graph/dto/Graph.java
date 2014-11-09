package blossom.restful.service.business.graph.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Graph {
    private List<NodeItem> nodes;
    private List<LinkItem> links;

    public List<NodeItem> getNodes() {
        if (this.nodes == null) {
            this.nodes = new ArrayList<NodeItem>();
        }
        return this.nodes;
    }

    public void setNodes(final List<NodeItem> nodes) {
        this.nodes = nodes;
    }

    public List<LinkItem> getLinks() {
        if (this.links == null) {
            this.links = new ArrayList<LinkItem>();
        }
        return this.links;
    }

    public void setLinks(final List<LinkItem> links) {
        this.links = links;
    }

}

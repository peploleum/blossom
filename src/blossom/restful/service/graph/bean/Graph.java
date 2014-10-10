package blossom.restful.service.graph.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Graph {
    private List<NodeItem> nodes;
    private List<LinkItem> links;

    public List<NodeItem> getNodes() {
        if (nodes == null) {
            nodes = new ArrayList<NodeItem>();
        }
        return nodes;
    }

    public void setNodes(final List<NodeItem> nodes) {
        this.nodes = nodes;
    }

    public List<LinkItem> getLinks() {
        if (links == null) {
            links = new ArrayList<LinkItem>();
        }
        return links;
    }

    public void setLinks(final List<LinkItem> links) {
        this.links = links;
    }

}

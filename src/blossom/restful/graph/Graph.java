package blossom.restful.graph;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Graph {
	private List<GraphItem> nodes;
	private List<LinkItem> links;

	public List<GraphItem> getNodes() {
		return nodes;
	}

	public void setNodes(List<GraphItem> nodes) {
		this.nodes = nodes;
	}

	public List<LinkItem> getLinks() {
		return links;
	}

	public void setLinks(List<LinkItem> links) {
		this.links = links;
	}

}

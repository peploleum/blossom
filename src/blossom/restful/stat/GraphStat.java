package blossom.restful.stat;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GraphStat {
	private Integer max;
	private List<GraphStatItem> map;

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public List<GraphStatItem> getMap() {
		return map;
	}

	public void setMap(List<GraphStatItem> stats) {
		this.map = stats;
	}

}

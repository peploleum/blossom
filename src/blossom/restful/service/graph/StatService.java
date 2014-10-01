package blossom.restful.service.graph;

import java.util.ArrayList;
import java.util.List;

import blossom.restful.stat.GraphStat;
import blossom.restful.stat.GraphStatItem;

public class StatService {
    public GraphStat getGraphStat() {
        final GraphStat gs = new GraphStat();
        gs.setMax(Integer.valueOf(2));
        final List<GraphStatItem> map = new ArrayList<GraphStatItem>();
        final GraphStatItem graphStatItem = new GraphStatItem();
        graphStatItem.setName("Leela");
        graphStatItem.setStat(Integer.valueOf(2));
        map.add(graphStatItem);
        gs.setMap(map);
        return gs;
    }
}
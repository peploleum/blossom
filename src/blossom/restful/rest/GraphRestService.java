package blossom.restful.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import blossom.restful.graph.Graph;
import blossom.restful.graph.LinkItem;
import blossom.restful.graph.NodeItem;
import blossom.restful.service.GraphSingleton;
import blossom.restful.stat.GraphStat;
import blossom.restful.stat.GraphStatItem;

@Path("/graph")
public class GraphRestService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Graph getGraph() {
        // to be able to return a marshalled json object based on a Jaxb bean it
        // seems we need to deploy heavy artillery with glassfish servlet + moxy
        // ...
        final GraphSingleton gs = GraphSingleton.getInstance();
        return gs.getGraph();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/stat")
    public GraphStat computeStats() {
        final Graph graph = GraphSingleton.getInstance().getGraph();
        final List<NodeItem> nodes = graph.getNodes();
        final GraphStat graphStat = new GraphStat();
        final List<GraphStatItem> map = new ArrayList<GraphStatItem>();
        graphStat.setMap(map);
        int max = 0;
        for (final NodeItem nodeItem : nodes) {
            final String name = nodeItem.getName();
            final GraphStatItem byName = graphStat.getByName(name);
            if (byName != null) {
                final Integer stat = byName.getStat();
                final Integer computedStat = (stat == null) ? Integer.valueOf(1) : Integer.valueOf(stat.intValue() + 1);
                max = Math.max(computedStat.intValue(), max);
                byName.setStat(computedStat);
            } else {
                final GraphStatItem graphStatItem = new GraphStatItem();
                graphStatItem.setName(name);
                graphStatItem.setStat(Integer.valueOf(1));
                graphStat.getMap().add(graphStatItem);
            }
        }
        return graphStat;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addNode")
    public void addNode(final NodeItem node) {
        final GraphSingleton gs = GraphSingleton.getInstance();
        gs.getGraph().getNodes().add(node);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addLink")
    public void addLink(final List<LinkItem> link) {
        final GraphSingleton gs = GraphSingleton.getInstance();
        for (final LinkItem linkItem : link) {
            gs.getGraph().getLinks().add(linkItem);
        }
    }

    // @PUT
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Path("/addLink")
    // public void addLink(final LinkItem link) {
    // final GraphSingleton gs = GraphSingleton.getInstance();
    // gs.getGraph().getLinks().add(link);
    // }

}

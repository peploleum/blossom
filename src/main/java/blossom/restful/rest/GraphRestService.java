package blossom.restful.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import blossom.exception.TopLevelBlossomException;
import blossom.restful.service.business.graph.GraphDao;
import blossom.restful.service.business.graph.dto.Graph;
import blossom.restful.service.business.graph.dto.GraphNodeIdCollection;
import blossom.restful.service.business.graph.dto.LinkItem;
import blossom.restful.service.business.graph.dto.NodeItem;
import blossom.restful.service.business.graph.model.GraphSingleton;
import blossom.restful.service.business.stat.dto.GraphStat;
import blossom.restful.service.business.stat.dto.GraphStatItem;

@Path("/graph")
public class GraphRestService {

    private static final Logger LOGGER = Logger.getLogger(GraphRestService.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Graph getGraph() throws TopLevelBlossomException {
        LOGGER.info("getting graph model");
        final GraphDao graphTransfer = new GraphDao();
        return graphTransfer.getGraph();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/stat")
    public GraphStat computeStats() throws TopLevelBlossomException {
        try {
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
        } catch (final Exception e) {
            throw new TopLevelBlossomException(e, "Failed to remove Nodes from database.");
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addNode")
    public void addNode(final NodeItem node) throws TopLevelBlossomException {
        LOGGER.info("adding node");
        final GraphDao graphTransfer = new GraphDao();
        graphTransfer.addNode(node);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addLink")
    public void addLink(final List<LinkItem> links) throws TopLevelBlossomException {
        LOGGER.info("adding link");
        final GraphDao graphTransfer = new GraphDao();
        graphTransfer.addLink(links);
    }

    @POST
    @Path("/persistGraph")
    public void persistGraph(final Graph graph) {
    }

    @DELETE
    @Path("/removeNode")
    public void removeNode(final String nodeId) throws TopLevelBlossomException {
        LOGGER.info("removing node");
        final GraphDao graphTransfer = new GraphDao();
        graphTransfer.removeSingleNode(nodeId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/removeNodes")
    public void removeNodes(final GraphNodeIdCollection ids) throws TopLevelBlossomException {
        LOGGER.info("removing nodes");
        final GraphDao graphTransfer = new GraphDao();
        graphTransfer.removeNodes(ids);
    }

}

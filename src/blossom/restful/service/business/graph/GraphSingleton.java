package blossom.restful.service.business.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import blossom.restful.service.business.graph.dto.Graph;
import blossom.restful.service.business.graph.dto.LinkItem;
import blossom.restful.service.business.graph.dto.NodeItem;

public class GraphSingleton {

    private static GraphSingleton INSTANCE = null;
    public static boolean debugMode = false;
    private static final Logger LOGGER = Logger.getLogger(GraphSingleton.class.getName());

    private Graph graph;

    /**
     * Initialize model singleton
     */
    private GraphSingleton() {
        this.graph = initGraph();
        if (this.graph == null) {
            this.graph = new Graph();
        }
    }

    /**
     * The graph model singleton
     *
     * @return {@link GraphSingleton}
     */
    public static GraphSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GraphSingleton();
        }
        return INSTANCE;
    }

    /**
     * The graph model
     *
     * @return {@link Graph}
     */
    public Graph getGraph() {
        return this.graph;
    }

    /**
     * Add a node to the graph model
     *
     * @param node
     *            {@link NodeItem} to add
     */
    public void addNode(final NodeItem node) {
        getGraph().getNodes().add(node);
    }

    /**
     * Remove a node from graph model
     *
     * @param nodeItem
     *            {@link NodeItem} to remove
     */
    public void removeNode(final NodeItem nodeItem) {
        getGraph().getNodes().remove(nodeItem);
    }

    /**
     * Add a link to the graph model
     *
     * @param link
     *            the {@link LinkItem} to add
     */
    public void addLink(final LinkItem link) {
        getGraph().getLinks().add(link);
    }

    /**
     * Initialize graph model using persistence layer
     *
     * @return {@link Graph}
     */
    private Graph initGraph() {
        final Graph g = new Graph();
        return g;
    }

    /**
     * Convienience method to retrieve a {@link Graph} {@link NodeItem} index from its unique
     * identifier
     *
     * @param id
     *            {@link String} the unique identifier
     * @return the {@link NodeItem} {@link Index} as an int
     */
    public int getNodeIndexByNodeId(final String id) {
        final List<NodeItem> nodes = getGraph().getNodes();
        for (final NodeItem nodeItem : nodes) {
            if (nodeItem.getId().equals(id)) {
                return nodes.indexOf(nodeItem);
            }
        }
        return -1;
    }

    /**
     * Convenience method to remove a {@link NodeItem} from the model using its unique identifier
     *
     * @param nodeId
     *            {@link String} the unique identifier
     */
    public void removeNodeById(final String nodeId) {
        final List<NodeItem> nodes = getGraph().getNodes();
        int indexToRemove = 0;
        boolean found = false;
        for (final NodeItem nodeItem : nodes) {
            if (nodeId.equals(nodeItem.getId())) {
                indexToRemove = nodes.indexOf(nodeItem);
                found = true;
                break;
            }
        }
        if (found) {
            LOGGER.log(Level.INFO, "removing node at index: " + indexToRemove);
            nodes.remove(indexToRemove);
            // we need to spot involved links ...
            final List<LinkItem> linksToRemove = new ArrayList<LinkItem>();
            final List<LinkItem> links = getGraph().getLinks();
            for (final LinkItem linkItem : links) {
                if (linkItem.getSource() == indexToRemove || linkItem.getTarget() == indexToRemove) {
                    LOGGER.log(Level.INFO, "marking link for removal: " + linkItem.toString());
                    linksToRemove.add(linkItem);
                }
            }
            getGraph().getLinks().removeAll(linksToRemove);
        }
    }

    /**
     * Convienience method to remove all links from or to a {@link NodeItem} based on its unique id
     *
     * @param nodeId
     *            {@link String} the unique node id
     */
    public void removeLinkById(final String nodeId) {
        final List<LinkItem> linksToRemove = new ArrayList<LinkItem>();
        final List<LinkItem> links = getGraph().getLinks();
        for (final LinkItem linkItem : links) {
            if (nodeId.equals(getGraph().getNodes().get(linkItem.getSource()).getId())
                    || nodeId.equals(getGraph().getNodes().get(linkItem.getTarget()).getId())) {
                linksToRemove.add(linkItem);
            }
        }
        getGraph().getLinks().removeAll(linksToRemove);
    }
}

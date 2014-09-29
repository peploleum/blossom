package blossom.restful.service.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import blossom.restful.graph.Graph;
import blossom.restful.graph.LinkItem;
import blossom.restful.graph.NodeItem;
import blossom.test.GraphTest;

public class GraphSingleton {

    private static GraphSingleton INSTANCE = null;
    public static boolean debugMode = false;
    private static final Logger LOGGER = Logger.getLogger(GraphSingleton.class.getName());

    private Graph graph;

    private GraphSingleton() {
        this.graph = initGraph();
        if (this.graph == null) {
            this.graph = new Graph();
        }
    }

    public static GraphSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GraphSingleton();
        }
        return INSTANCE;
    }

    public Graph getGraph() {
        return this.graph;
    }

    public void addNode(final NodeItem node) {
        getGraph().getNodes().add(node);
    }

    public void removeNode(final NodeItem nodeItem) {
        getGraph().getNodes().remove(nodeItem);
    }

    public void addLink(final LinkItem link) {
        getGraph().getLinks().add(link);
    }

    private Graph initGraph() {
        if (debugMode) {
            JAXBContext jc;
            Graph g = null;
            try {
                jc = JAXBContext.newInstance(Graph.class);
                final Unmarshaller unmarshaller = jc.createUnmarshaller();
                unmarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
                unmarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
                // this is so intuitive ... or not
                g = unmarshaller.unmarshal(new StreamSource(GraphTest.class.getResourceAsStream("data.json")), Graph.class).getValue();
                // anyway it works...
            } catch (final Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to load sample data. Will provide empty Graph. " + e.getMessage(), e);
                g = new Graph();
            }
            return g;
        } else {
            final Graph g = new Graph();
            return g;
        }
    }

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
}

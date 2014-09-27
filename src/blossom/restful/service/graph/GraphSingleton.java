package blossom.restful.service.graph;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import blossom.restful.graph.Graph;
import blossom.restful.graph.LinkItem;
import blossom.restful.graph.NodeItem;
import blossom.test.GraphTest;

public class GraphSingleton {

    private static GraphSingleton INSTANCE = null;
    
    private static final Logger LOGGER = Logger.getLogger(GraphSingleton.class.getName());

    private Graph graph;

    private GraphSingleton() {
        graph = initGraph();
        if (graph == null)
            graph = new Graph();
    }

    public static GraphSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GraphSingleton();
        }
        return INSTANCE;
    }

    public Graph getGraph() {
        return graph;
    }

    public void addNode(final NodeItem node) {
        getGraph().getNodes().add(node);
    }

    public void addLink(final LinkItem link) {
        getGraph().getLinks().add(link);
    }

    private Graph initGraph() {
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
        } catch (final JAXBException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return g;
    }
}

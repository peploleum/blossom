package blossom.test;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import blossom.restful.service.graph.bean.Graph;
import blossom.restful.service.graph.bean.LinkItem;
import blossom.restful.service.graph.bean.NodeItem;

public class GraphTest {

    private static final Logger LOGGER = Logger.getLogger(GraphTest.class.getName());

    private GraphTest() {

    }

    public static void main(final String[] args) {
        writeGraph();
        readGraph();
        // JSONConfiguration.mapped().
    }

    private static void writeGraph() {
        final NodeItem gi1 = new NodeItem();
        gi1.setId("15766ad3-db4a-469f-95ca-7f61b49c7443");
        gi1.setName("zoidberg");
        gi1.setSize(70);
        gi1.setCatchphrase("Woowoowooowoo!");

        final NodeItem gi2 = new NodeItem();
        gi2.setId("15766ad3-db4a-469f-95ca-7f61b49c7444");
        gi2.setName("fry");
        gi2.setSize(40);
        gi2.setCatchphrase("I'm walking on sunshine!");

        final NodeItem gi3 = new NodeItem();
        gi3.setId("15766ad3-db4a-469f-95ca-7f61b49c7445");
        gi3.setName("amy");
        gi3.setSize(50);
        gi3.setCatchphrase("I'm wearing a bellyparka!");

        final LinkItem li1 = new LinkItem();
        li1.setSource(0);
        li1.setTarget(1);
        final LinkItem li2 = new LinkItem();
        li2.setSource(1);
        li2.setTarget(2);
        final LinkItem li3 = new LinkItem();
        li3.setSource(2);
        li3.setTarget(0);

        final Graph g = new Graph();

        g.setNodes(Arrays.asList(new NodeItem[] { gi1, gi2, gi3 }));
        g.setLinks(Arrays.asList(new LinkItem[] { li1, li2, li3 }));

        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance(Graph.class);
            final Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
            marshaller.marshal(g, System.out);
        } catch (final JAXBException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static void readGraph() {

        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance(Graph.class);
            final Unmarshaller unmarshaller = jc.createUnmarshaller();
            unmarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
            unmarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
            // this is so intuitive ... or not
            final Graph g = unmarshaller.unmarshal(new StreamSource(GraphTest.class.getResourceAsStream("data.json")), Graph.class).getValue();
            // anyway it works...
            g.getNodes();
        } catch (final JAXBException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}

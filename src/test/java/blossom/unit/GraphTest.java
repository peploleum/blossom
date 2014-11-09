package blossom.unit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import blossom.restful.service.business.graph.dto.Graph;
import blossom.restful.service.business.graph.dto.LinkItem;
import blossom.restful.service.business.graph.dto.NodeItem;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GraphTest {

    private static final Logger LOGGER = Logger.getLogger(GraphTest.class.getName());

    public GraphTest() {

    }

    @Test
    public void writeGraph() throws JsonGenerationException, JsonMappingException, IOException {
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

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(System.out, g);
    }

    @Test
    public void readGraph() {
        final ObjectMapper om = new ObjectMapper();
        Graph readValue;
        try {
            readValue = om.readValue(GraphTest.class.getResource("/data.json"), Graph.class);
            assertNotNull(readValue);
            System.out.println(readValue.getNodes());
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "failed to read graph", e);
        }
    }
}

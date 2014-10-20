package blossom.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import blossom.restful.service.business.graph.dto.GraphNodeIdCollection;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GraphNodeIdCollectionTest {
    private static final Logger LOGGER = Logger.getLogger(GraphNodeIdCollectionTest.class.getName());

    public static void main(String[] args) {
        GraphNodeIdCollection coll = new GraphNodeIdCollection();
        coll.setIds(new String[] { "aaaa", "bbbb" });
        final ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(System.out, coll);
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "failed to read json", e);
        }
    }
}

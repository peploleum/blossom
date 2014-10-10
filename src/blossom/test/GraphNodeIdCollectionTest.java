package blossom.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import blossom.restful.service.graph.bean.GraphNodeIdCollection;

public class GraphNodeIdCollectionTest {
    private static final Logger LOGGER = Logger.getLogger(GraphNodeIdCollectionTest.class.getName());

    public static void main(String[] args) {
        GraphNodeIdCollection coll = new GraphNodeIdCollection();
        coll.setIds(new String[] { "aaaa", "bbbb" });
        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance(GraphNodeIdCollection.class);
            final Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
            marshaller.marshal(coll, System.out);
        } catch (final JAXBException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}

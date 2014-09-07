package blossom.test;

import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import blossom.restful.graph.Graph;
import blossom.restful.graph.GraphItem;
import blossom.restful.graph.LinkItem;

public class GraphTest {
	public static void main(final String[] args) {
		final GraphItem gi1 = new GraphItem();
		gi1.setId("15766ad3-db4a-469f-95ca-7f61b49c7443");
		gi1.setName("zoidberg");
		gi1.setSize(70);
		gi1.setCatchphrase("Woowoowooowoo!");

		final GraphItem gi2 = new GraphItem();
		gi2.setId("15766ad3-db4a-469f-95ca-7f61b49c7444");
		gi2.setName("fry");
		gi2.setSize(40);
		gi2.setCatchphrase("I'm walking on sunshine!");

		final GraphItem gi3 = new GraphItem();
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

		Graph g = new Graph();

		g.setNodes(Arrays.asList(new GraphItem[] { gi1, gi2, gi3 }));
		g.setLinks(Arrays.asList(new LinkItem[] { li1, li2, li3 }));

		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(Graph.class);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
			marshaller.marshal(g, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		// JSONConfiguration.mapped().
	}
}

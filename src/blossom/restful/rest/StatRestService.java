package blossom.restful.rest;

import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import blossom.restful.graph.Graph;
import blossom.restful.graph.GraphItem;
import blossom.restful.graph.LinkItem;

@Path("/graphstat")
public class StatRestService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Graph getGraphStat() {
		// to be able to return a marshalled json object based on a Jaxb bean it
		// seems we need to deploy jeavy artillery with glassfish servlet + moxy
		// ...
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

		final Graph g = new Graph();
		g.setNodes(Arrays.asList(new GraphItem[] { gi1, gi2, gi3 }));
		g.setLinks(Arrays.asList(new LinkItem[] { li1, li2, li3 }));
		return g;
	}
}

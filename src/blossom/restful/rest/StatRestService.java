package blossom.restful.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import blossom.restful.graph.Graph;
import blossom.restful.service.GraphSingleton;

@Path("/graphstat")
public class StatRestService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Graph getGraphStat() {
        // to be able to return a marshalled json object based on a Jaxb bean it
        // seems we need to deploy jeavy artillery with glassfish servlet + moxy
        // ...
        final GraphSingleton gs = GraphSingleton.getInstance();
        return gs.getGraph();
    }
}

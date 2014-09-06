package blossom.restful.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import blossom.restful.service.StatService;
import blossom.restful.stat.GraphStat;

@Path("/graphstat")
public class StatRestService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public GraphStat getGraphStat() {
		// this seems to be a way not to care about json generation
		// it allows us not to build it manually and only rely directly on
		// service objects
		final StatService ss = new StatService();
		return ss.getGraphStat();
	}
}

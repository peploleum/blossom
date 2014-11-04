package blossom.restful.rest;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import blossom.exception.TopLevelBlossomException;
import blossom.restful.service.business.stat.StatDao;
import blossom.restful.service.business.stat.dto.Stat;

@Path("/stat")
public class StatRestService {
    private final static Logger LOGGER = Logger.getLogger(StatRestService.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public Stat getStat() throws TopLevelBlossomException {
        LOGGER.info("Getting stats");
        final StatDao statDao = new StatDao();
        return statDao.getStats();
    }
}

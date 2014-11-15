package blossom.restful.rest;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import blossom.exception.TopLevelBlossomException;
import blossom.restful.service.tech.entity.dto.CharacterBean;
import blossom.restful.service.tech.table.TableDao;
import blossom.restful.service.tech.table.dto.TableRowCollection;

@Path("/table")
public class TableRestService {

    private static final Logger LOGGER = Logger.getLogger(TableRestService.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TableRowCollection getRows() throws TopLevelBlossomException {
        LOGGER.info("Getting all rows");
        TableDao tableTransfer = new TableDao();
        return tableTransfer.getTableRows();
    }
    
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("{id}")
    public CharacterBean getEntityById(@javax.ws.rs.PathParam("id") final String id) throws TopLevelBlossomException {
        final TableDao tableTransfer = new TableDao();
        return tableTransfer.getEntityById(id);
    }
}
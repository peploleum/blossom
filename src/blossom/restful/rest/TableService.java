package blossom.restful.rest;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import blossom.exception.TopLevelBlossomException;
import blossom.restful.service.table.dto.TableRowCollection;
import blossom.restful.service.table.transfer.TableTransfer;

@Path("/table")
public class TableService {

    private static final Logger LOGGER = Logger.getLogger(TableService.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getdata")
    public TableRowCollection getRows() throws TopLevelBlossomException {
        LOGGER.info("Getting all rows");
        TableTransfer tableTransfer = new TableTransfer();
        return tableTransfer.getTableRows();
    }
}

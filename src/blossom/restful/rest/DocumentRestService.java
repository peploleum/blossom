package blossom.restful.rest;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import blossom.exception.TopLevelBlossomException;
import blossom.restful.service.business.document.BlossomDocumentService;
import blossom.restful.service.business.document.dto.BlossomDocument;

@Path("/annotation/{docId}")
public class DocumentRestService {
    private static final Logger LOGGER = Logger.getLogger(DocumentRestService.class.getName());

    @GET
    public BlossomDocument getDocumentById(@PathParam("docId") final String docId) throws TopLevelBlossomException {
        LOGGER.info("fetch doc by id: " + docId);
        try {
            final BlossomDocumentService bds = new BlossomDocumentService();
            return bds.getBlossomDocumentById(docId);
        } catch (final Exception e) {
            throw new TopLevelBlossomException("unable to fetch doc for id: " + docId);
        }
    }
}

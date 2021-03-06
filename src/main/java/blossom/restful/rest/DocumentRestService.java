package blossom.restful.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import blossom.exception.TopLevelBlossomException;
import blossom.restful.service.business.document.BlossomDocumentDao;
import blossom.restful.service.business.document.dto.BlossomDocument;
import blossom.restful.service.business.document.dto.TaggedEntity;

@Path("/annotation")
public class DocumentRestService {
    private static final Logger LOGGER = Logger.getLogger(DocumentRestService.class.getName());

    @GET
    @Path("{docId}")
    public BlossomDocument getDocumentById(@PathParam("docId") final String docId) throws TopLevelBlossomException {
        LOGGER.info("fetch doc by id: " + docId);
        try {
            final BlossomDocumentDao bds = new BlossomDocumentDao();
            return bds.getBlossomDocumentById(docId);
        } catch (final Exception e) {
            throw new TopLevelBlossomException(e, "unable to fetch doc for id: " + docId);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{docId}")
    public BlossomDocument addTaggedEntity(@PathParam("docId") final String docId, final TaggedEntity taggedEntity) throws TopLevelBlossomException {
        LOGGER.log(Level.SEVERE, "adding tagged entity");
        try {
            final BlossomDocumentDao bds = new BlossomDocumentDao();
            final BlossomDocument taggedDocument = bds.addTag(taggedEntity);
            return taggedDocument;
        } catch (final Exception e) {
            throw new TopLevelBlossomException(e, "unable to add tagged entity");
        }
    }
}

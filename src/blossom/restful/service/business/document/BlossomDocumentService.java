package blossom.restful.service.business.document;

import java.util.logging.Logger;

import blossom.restful.service.business.document.dto.BlossomDocument;
import blossom.restful.service.business.document.dto.TaggedEntity;
import blossom.restful.service.business.document.model.DocumentsSingleton;

public class BlossomDocumentService {

    private final static Logger LOGGER = Logger.getLogger(BlossomDocumentService.class.getName());

    public BlossomDocumentService() {

    }

    public BlossomDocument getBlossomDocumentById(final String id) {
        LOGGER.info("getting document " + id);
        final DocumentsSingleton documentModel = DocumentsSingleton.getINSTANCE();
        final BlossomDocument first = documentModel.getDocuments().iterator().next();
        return first;
    }

    public void createBlossomDocument(final BlossomDocument bd) {

    }

    public BlossomDocument addTag(final TaggedEntity taggedEntity) {
        LOGGER.info("adding tagged entity ");
        final DocumentsSingleton documentModel = DocumentsSingleton.getINSTANCE();
        final BlossomDocument first = documentModel.getDocuments().iterator().next();
        first.getTags().add(taggedEntity);
        return first;
    }
}

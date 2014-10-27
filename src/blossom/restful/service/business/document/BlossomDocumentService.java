package blossom.restful.service.business.document;

import blossom.restful.service.business.document.dto.BlossomDocument;

public class BlossomDocumentService {

    public BlossomDocumentService() {

    }

    public BlossomDocument getBlossomDocumentById(final String id) {
        final BlossomDocument bd = new BlossomDocument();
        bd.setContent("<p><b>Hello</b> new document<p>");
        bd.setTitle("Hello new document title");
        return bd;
    }

    public void createBlossomDocument(final BlossomDocument bd) {

    }
}

package blossom.restful.service.business.document.model;

import java.io.InputStream;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

import blossom.restful.service.business.document.BlossomDocumentDao;
import blossom.restful.service.business.document.dto.BlossomDocument;
import blossom.util.BlossomUtils;

public class DocumentsSingleton {

    private static final Logger LOGGER = Logger.getLogger(DocumentsSingleton.class.getName());

    private static DocumentsSingleton INSTANCE;

    private final CopyOnWriteArraySet<BlossomDocument> documents = new CopyOnWriteArraySet<BlossomDocument>();

    private DocumentsSingleton() {
        final InputStream resourceAsStream = BlossomDocumentDao.class.getResourceAsStream("/sample.txt");
        String slurp = BlossomUtils.slurp(resourceAsStream, 1024);
        slurp = slurp.replaceAll("\n", "").replaceAll("\r", "");
        final BlossomDocument bd = new BlossomDocument();
        bd.setContent(slurp);
        bd.setDecoratedContent(slurp);
        bd.setTitle("Hello new document title");
        this.documents.add(bd);
    }

    public static DocumentsSingleton getINSTANCE() {
        if (INSTANCE == null) {
            LOGGER.info("Instanciate " + DocumentsSingleton.class.getName());
            INSTANCE = new DocumentsSingleton();
        }
        return INSTANCE;
    }

    public CopyOnWriteArraySet<BlossomDocument> getDocuments() {
        return this.documents;
    }
}

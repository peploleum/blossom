package blossom.restful.service.business.document.model;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

import blossom.restful.service.business.document.BlossomDocumentDao;
import blossom.restful.service.business.document.dto.BlossomDocument;

public class DocumentsSingleton {

    private static final Logger LOGGER = Logger.getLogger(DocumentsSingleton.class.getName());

    private static DocumentsSingleton INSTANCE;

    private final CopyOnWriteArraySet<BlossomDocument> documents = new CopyOnWriteArraySet<BlossomDocument>();

    private DocumentsSingleton() {
        final InputStream resourceAsStream = BlossomDocumentDao.class.getResourceAsStream("sample.txt");
        final String slurp = slurp(resourceAsStream, 1024);
        final BlossomDocument bd = new BlossomDocument();
        bd.setContent(slurp);
        bd.setDecoratedContent(slurp);
        bd.setTitle("Hello new document title");
        this.documents.add(bd);
    }

    public static DocumentsSingleton getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new DocumentsSingleton();
        }
        return INSTANCE;
    }

    public static String slurp(final InputStream is, final int bufferSize) {
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        try {
            final Reader in = new InputStreamReader(is, "UTF-8");
            try {
                for (;;) {
                    final int rsz = in.read(buffer, 0, buffer.length);
                    if (rsz < 0) {
                        break;
                    }
                    out.append(buffer, 0, rsz);
                }
            } finally {
                in.close();
            }
        } catch (final Exception ex) {
            LOGGER.log(Level.SEVERE, "failed to read sample file", ex);
        }
        return out.toString();
    }

    public CopyOnWriteArraySet<BlossomDocument> getDocuments() {
        return this.documents;
    }
}

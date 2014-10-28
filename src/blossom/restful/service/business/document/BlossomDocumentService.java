package blossom.restful.service.business.document;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

import blossom.restful.service.business.document.dto.BlossomDocument;

public class BlossomDocumentService {

    private final static Logger LOGGER = Logger.getLogger(BlossomDocumentService.class.getName());

    public BlossomDocumentService() {

    }

    public BlossomDocument getBlossomDocumentById(final String id) {
        final InputStream resourceAsStream = BlossomDocumentService.class.getResourceAsStream("sample.txt");
        final String slurp = slurp(resourceAsStream, 1024);
        final BlossomDocument bd = new BlossomDocument();
        bd.setContent(slurp);
        bd.setTitle("Hello new document title");
        return bd;
    }

    public void createBlossomDocument(final BlossomDocument bd) {

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
}

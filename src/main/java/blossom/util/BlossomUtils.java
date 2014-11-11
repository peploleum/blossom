package blossom.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlossomUtils {

    private static final Logger LOGGER = Logger.getLogger(BlossomUtils.class.getName());

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

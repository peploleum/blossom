package blossom.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

import blossom.exception.TopLevelBlossomException;

/**
 *
 * @author yo <br>
 *         building solr requests to try it out
 */
public class SolrSandbox {

    private static final String DEFAULT_POST_URL = "http://localhost:8080/solr/collection1/update";
    private static final Logger LOGGER = Logger.getLogger(SolrSandbox.class.getName());

    private SolrSandbox() {

    }

    public static void main(final String[] args) throws TopLevelBlossomException {
        final String requestUrl = DEFAULT_POST_URL;

        final File file = new File("c:/data/solr.xml");
        final StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(requestUrl);
        // need extract with parameters for fancy files with tricky mimetypes
        URL url = null;
        try {
            LOGGER.info("url : " + urlBuilder.toString());
            url = new URL(urlBuilder.toString());
        } catch (final MalformedURLException e) {
            throw new TopLevelBlossomException(e.getMessage());
        }
        if (url != null) {
            if (!file.exists()) {
                LOGGER.log(Level.WARNING, "No file");
            }
            FileInputStream is = null;

            try {
                is = new FileInputStream(file);

                final OutputStream os = System.out;

                postData(is, os, url);
                commit();
            } catch (final FileNotFoundException e) {
                LOGGER.log(Level.SEVERE, "Can't read file", e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (final Exception e2) {
                        // no big deal
                        LOGGER.log(Level.WARNING, "Can't read file", e2);
                    }
                }

            }
        }

    }

    /**
     * reads from data, posts, retrieves response in output
     *
     * @return true if success
     */
    public static boolean postData(final InputStream data, final OutputStream output, final URL url) {
        boolean success = true;
        LOGGER.info("posting data");
        HttpURLConnection urlConnection = null;
        try {
            try {
                urlConnection = connect(url);
            } catch (final IOException e) {
                LOGGER.log(Level.SEVERE, "Can't connect", e);
                success = false;
            }

            try (final OutputStream out = urlConnection.getOutputStream()) {
                pipe(data, out);
            } catch (final IOException e) {
                LOGGER.log(Level.SEVERE, "Can't connect", e);
                success = false;
            }

            try {
                success &= (urlConnection.getResponseCode() == 200);
                try (final InputStream in = urlConnection.getInputStream()) {
                    pipe(in, output);
                }
            } catch (final IOException e) {
                try {
                    pipe(urlConnection.getErrorStream(), output);
                } catch (final IOException e1) {
                    LOGGER.log(Level.WARNING, "Can't read response", e1);
                }
                LOGGER.log(Level.WARNING, "Can't read response", e);
                success = false;
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return success;
    }

    private static HttpURLConnection connect(final URL url) throws IOException {
        HttpURLConnection urlConnection;
        urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setRequestMethod("POST");
        } catch (final ProtocolException e) {
            LOGGER.log(Level.SEVERE, "HTTP url connection not handled", e);
        }
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setAllowUserInteraction(false);
        urlConnection.setRequestProperty("Content-type", "application/xml");
        if (url.getUserInfo() != null) {
            final String encoding = DatatypeConverter.printBase64Binary(url.getUserInfo().getBytes(StandardCharsets.US_ASCII));
            urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
        }
        urlConnection.connect();
        return urlConnection;
    }

    /**
     * Read source pipes into dest
     */
    private static void pipe(final InputStream source, final OutputStream dest) throws IOException {
        final byte[] buf = new byte[1024];
        int read = 0;
        while ((read = source.read(buf)) >= 0) {
            if (null != dest) {
                dest.write(buf, 0, read);
            }
        }
        if (null != dest) {
            dest.flush();
        }
    }

    public static void commit() {
        LOGGER.info("committing");
        try {
            final URL commitUrl = new URL(DEFAULT_POST_URL + "?commit=true");
            final HttpURLConnection connection = (HttpURLConnection) commitUrl.openConnection();
            connection.connect();
            pipe(connection.getInputStream(), System.out);
        } catch (final MalformedURLException e) {
            LOGGER.log(Level.SEVERE, "Failed to commit", e);
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to commit", e);
        }
    }
}

package blossom.restful.service.tech.geo;

import java.io.StringWriter;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import blossom.exception.TopLevelBlossomException;
import blossom.restful.service.business.geo.dto.Feature;
import blossom.websocket.BusinessLayerEndpointConfiguration;

public class GeoConsumerService {

    private final Queue<Feature> queue;
    private final static Logger LOGGER = Logger.getLogger(GeoConsumerService.class.getName());

    private final AtomicBoolean stopped = new AtomicBoolean(Boolean.FALSE);
    private final Runnable consumeRunnable;

    public GeoConsumerService(final Queue<Feature> queue) {
        this.queue = queue;

        this.consumeRunnable = new Runnable() {

            @Override
            public void run() {

                while (!stopped()) {
                    final Feature poll = GeoConsumerService.this.queue.poll();
                    if (poll == null) {
                        askForStop();
                    } else { // we can consume the polled Feature provided it's not null
                        try {
                            consume(poll);
                        } catch (TopLevelBlossomException e) {
                            LOGGER.log(Level.SEVERE, "Consumer failed to consume polled feature " + poll);
                        }
                    }
                }
            }
        };
    }

    public boolean stopped() {
        return this.stopped.get();
    }

    public void askForStop() {
        this.stopped.set(true);
    }

    public void startConsuming() {
        final Thread thread = new Thread(this.consumeRunnable);
        thread.start();
    }

    private void consume(final Feature feature) throws TopLevelBlossomException {

        JAXBContext jc;
        // we need to marshall it to send it through the websocket pipe
        try {
            final StringWriter writer = new StringWriter();
            jc = JAXBContext.newInstance(Feature.class);
            final Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(MarshallerProperties.JSON_VALUE_WRAPPER, "coordinates");
            marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
            marshaller.marshal(feature, writer);
            BusinessLayerEndpointConfiguration.getEndPointSingleton().onMessage(writer.getBuffer().toString(), null);
            // final GeoEntitiesSingleton ges = GeoEntitiesSingleton.getInstance();
        } catch (final JAXBException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new TopLevelBlossomException(e, "failed to add feature to geo entites");
        }
    }
}

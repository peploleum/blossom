package blossom.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/maps/businesslayerupdates", configurator = BusinessLayerEndpointConfiguration.class)
public class BusinessLayerEndPoint {
    private static final Logger LOGGER = Logger.getLogger(BusinessLayerEndPoint.class.getName());
    private final Set<Session> userSessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(final Session userSession) {
        LOGGER.info("opening websocket session " + userSession.getId());
        this.userSessions.add(userSession);
    }

    @OnClose
    public void onClose(final Session userSession) {
        LOGGER.info("closing websocket session " + userSession.getId());
        this.userSessions.remove(userSession);
    }

    @OnMessage
    public void onMessage(final String message, final Session userSession) {

        LOGGER.info("message Received: from[" + (userSession != null ? userSession.getId() : "no user session") + "] content: [" + message + "].");
        synchronized (this.userSessions) {
            for (final Session session : this.userSessions) {
                LOGGER.log(Level.INFO, "Sending to " + session.getId());
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Failed to send text from websocket ", e);
                }
            }
        }
    }

    @OnError
    public void onError(final Throwable throwable, final Session userSession) {
        LOGGER.log(Level.WARNING, throwable.getMessage(), throwable);
    }

}

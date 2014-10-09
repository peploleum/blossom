package blossom.websocket;

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
        LOGGER.info("message Received: from[" + userSession.getId() + "] content: [" + message + "].");
        for (final Session session : this.userSessions) {
            System.out.println("Sending to " + session.getId());
            session.getAsyncRemote().sendText(message);
        }
    }

//    @OnMessage
//    public void onServerGeneratedMessage(final Object message) {
//        LOGGER.info("message sent from server [ content: [" + message.toString() + "].");
//        for (final Session session : this.userSessions) {
//            System.out.println("Sending to " + session.getId());
//            session.getAsyncRemote().sendObject(message);
//        }
//    }

    @OnError
    public void onError(final Throwable throwable, final Session userSession) {
        LOGGER.log(Level.WARNING, throwable.getMessage(), throwable);
    }

}

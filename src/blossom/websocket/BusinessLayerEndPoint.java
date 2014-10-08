package blossom.websocket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/maps/businesslayerupdates", configurator = BusinessLayerEndpointConfiguration.class)
public class BusinessLayerEndPoint {
    private static final Logger LOGGER = Logger.getLogger(BusinessLayerEndPoint.class.getName());
    private Set<Session> userSessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session userSession) {
        LOGGER.info("opening websocket session " + userSession.getId());
        userSessions.add(userSession);
    }

    @OnClose
    public void onClose(Session userSession) {
        LOGGER.info("closing websocket session " + userSession.getId());
        userSessions.remove(userSession);
    }

    @OnMessage
    public void onMessage(String message, Session userSession) {
        LOGGER.info("essage Received: from[" + userSession.getId() + "] content: [" + message + "].");
        for (Session session : userSessions) {
            System.out.println("Sending to " + session.getId());
            session.getAsyncRemote().sendText(message);
        }
    }
}

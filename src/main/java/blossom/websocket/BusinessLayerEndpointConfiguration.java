package blossom.websocket;

import javax.websocket.server.ServerEndpointConfig.Configurator;

public class BusinessLayerEndpointConfiguration extends Configurator {

    private static BusinessLayerEndPoint businessLayerServer = new BusinessLayerEndPoint();

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getEndpointInstance(final Class<T> endpointClass) throws InstantiationException {
        return (T) businessLayerServer;
    }

    public static BusinessLayerEndPoint getEndPointSingleton() {
        return businessLayerServer;
    }
}

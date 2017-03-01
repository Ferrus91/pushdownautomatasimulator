package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 13/08/13
 * Time: 21:52
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionAddEvent implements EventObject {
    static Logger logger = Logger.getLogger(ConnectionAddEvent.class);
    @Getter
    private final String fromNodeId;
    @Getter
    private final String toNodeId;

    public ConnectionAddEvent(String fromNodeId, String toNodeId) {
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
        logger.trace(this.getClass().getSimpleName() + " fired! With fromNode "
                + fromNodeId + " and tonode " + toNodeId);
    }

    @Override
    public EventBusInstance getInstanceToken() {
        return EventBusInstance.DRAWING;
    }
}

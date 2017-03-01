package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 15/08/13
 * Time: 12:51
 * To change this template use File | Settings | File Templates.
 */
public class RequestConnectionDetailsEvent implements EventObject {
    static Logger logger = Logger.getLogger(RequestConnectionDetailsEvent.class);

    @Getter
    private final String toLabel;
    @Getter
    private final String fromLabel;
    @Getter
    private String connectionId;

    public RequestConnectionDetailsEvent(String connectionId, String toLabel, String fromLabel) {
        this.connectionId = connectionId;
        this.toLabel = toLabel;
        this.fromLabel = fromLabel;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! With id: " + connectionId);
        return EventBusInstance.DRAWING;
    }
}

package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 04/08/13
 * Time: 22:39
 * To change this template use File | Settings | File Templates.
 */
public class NodeCreationEvent implements EventObject {
    static Logger logger = Logger.getLogger(NodeCreationEvent.class);

    @Getter
    private final Double x;
    @Getter
    private final Double y;

    public NodeCreationEvent(Double x, Double y)
    {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! On x: " + x + " and y: " + y);
        return EventBusInstance.DRAWING;
    }
}

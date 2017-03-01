package EventObjects;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/08/13
 * Time: 18:09
 * To change this template use File | Settings | File Templates.
 */
public class RequestConnectionEvent implements EventObject {
    static Logger logger = Logger.getLogger(RequestConnectionEvent.class);

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired!");
        return EventBusInstance.DATABASE;
    }
}

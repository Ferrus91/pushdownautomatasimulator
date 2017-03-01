package EventObjects;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 21/07/13
 * Time: 14:35
 * To change this template use File | Settings | File Templates.
 */
public class NextTransitionEvent implements EventObject {
    static Logger logger = Logger.getLogger(NextTransitionEvent.class);

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired!");
        return EventBusInstance.DRAWING;
    }
}

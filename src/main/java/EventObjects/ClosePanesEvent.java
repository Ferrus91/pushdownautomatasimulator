package EventObjects;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 20/08/13
 * Time: 00:43
 * To change this template use File | Settings | File Templates.
 */
public class ClosePanesEvent implements EventObject {
    static Logger logger = Logger.getLogger(ClosePanesEvent.class);

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired!");
        return EventBusInstance.DRAWING;
    }
}

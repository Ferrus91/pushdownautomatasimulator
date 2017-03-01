package EventObjects;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 18/08/13
 * Time: 21:43
 * To change this template use File | Settings | File Templates.
 */
public class DeselectAllEvent implements EventObject {
    static Logger logger = Logger.getLogger(DeselectAllEvent.class);

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired!");
        return EventBusInstance.DRAWING;
    }
}

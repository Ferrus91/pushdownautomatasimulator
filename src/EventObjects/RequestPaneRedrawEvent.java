package EventObjects;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 11/08/13
 * Time: 22:08
 * To change this template use File | Settings | File Templates.
 */
public class RequestPaneRedrawEvent implements EventObject {
    static Logger logger = Logger.getLogger(RequestPaneRedrawEvent.class);

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired!");
        return EventBusInstance.DRAWING;
    }
}

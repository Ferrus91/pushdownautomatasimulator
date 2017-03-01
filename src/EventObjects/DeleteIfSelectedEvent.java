package EventObjects;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 20/08/13
 * Time: 23:19
 * To change this template use File | Settings | File Templates.
 */
public class DeleteIfSelectedEvent implements EventObject {
    static Logger logger = Logger.getLogger(DeleteByIdEvent.class);

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired!");
        return EventBusInstance.DRAWING;
    }
}

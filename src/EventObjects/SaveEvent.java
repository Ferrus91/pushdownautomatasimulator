package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 22/08/13
 * Time: 23:51
 * To change this template use File | Settings | File Templates.
 */
public class SaveEvent implements EventObject {
    static Logger logger = Logger.getLogger(SaveEvent.class);

    @Getter
    private final boolean isClose;

    public SaveEvent(boolean isClose) {
        this.isClose = isClose;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! Is being closed? " + isClose);
        return EventBusInstance.DATABASE;
    }
}

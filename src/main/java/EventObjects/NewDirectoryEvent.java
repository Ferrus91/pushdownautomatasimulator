package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 25/08/13
 * Time: 00:00
 * To change this template use File | Settings | File Templates.
 */
public class NewDirectoryEvent implements EventObject {
    static Logger logger = Logger.getLogger(NewDirectoryEvent.class);

    @Getter
    private final String directoryName;

    public NewDirectoryEvent(String directoryName) {
        logger.trace("NewDirectoryEvent fired");
        this.directoryName = directoryName;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        return EventBusInstance.DATABASE;
    }
}

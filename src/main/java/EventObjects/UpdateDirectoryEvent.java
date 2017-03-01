package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 26/08/13
 * Time: 10:20
 * To change this template use File | Settings | File Templates.
 */
public class UpdateDirectoryEvent implements EventObject {
    static Logger logger = Logger.getLogger(UpdateDirectoryEvent.class);

    @Getter
    private String name;
    @Getter
    private Integer directoryId;

    public UpdateDirectoryEvent(String name, Integer directoryId) {
        this.name = name;
        this.directoryId = directoryId;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! Id: " + directoryId + " name: " + name);
        return EventBusInstance.DATABASE;
    }
}

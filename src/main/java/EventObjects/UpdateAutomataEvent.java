package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 26/08/13
 * Time: 10:21
 * To change this template use File | Settings | File Templates.
 */
public class UpdateAutomataEvent implements EventObject {
    static Logger logger = Logger.getLogger(UpdateAutomataEvent.class);

    @Getter
    private String name;
    @Getter
    private Integer automataId;

    public UpdateAutomataEvent(String name, Integer automataId) {
        this.name = name;
        this.automataId = automataId;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! Id: " + automataId + " name: " + name);
        return EventBusInstance.DATABASE;
    }
}

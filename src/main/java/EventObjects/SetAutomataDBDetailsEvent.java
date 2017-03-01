package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 31/08/13
 * Time: 05:23
 * To change this template use File | Settings | File Templates.
 */
public class SetAutomataDBDetailsEvent implements EventObject {
    static Logger logger = Logger.getLogger(SetAutomataDBDetailsEvent.class);

    @Getter
    private Integer newAutomataId;

    public SetAutomataDBDetailsEvent(Integer newAutomataId) {
        this.newAutomataId = newAutomataId;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! Id: "
                + newAutomataId);
        return EventBusInstance.DATABASE;
    }
}

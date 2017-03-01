package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 09/09/13
 * Time: 01:59
 * To change this template use File | Settings | File Templates.
 */
public class MoveAutomataEvent implements EventObject {
    static Logger logger = Logger.getLogger(MoveAutomataEvent.class);

    @Getter
    private Integer id;
    @Getter
    private Integer automataId;

    public MoveAutomataEvent(Integer id, Integer automataId) {
        this.id = id;
        this.automataId = automataId;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! With id of directory:  " + id
                + " for automaton: " + automataId);
        return EventBusInstance.DATABASE;
    }
}

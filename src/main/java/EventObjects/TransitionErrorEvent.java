package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 21/07/13
 * Time: 14:42
 * To change this template use File | Settings | File Templates.
 */
public class TransitionErrorEvent implements EventObject {
    static Logger logger = Logger.getLogger(TransitionErrorEvent.class);

    @Getter
    private final String errorMessage;

    @Override
    public EventBusInstance getInstanceToken() {
        return EventBusInstance.DRAWING;
    }

    public TransitionErrorEvent(String errorMessage) {
        this.errorMessage = errorMessage;
        logger.trace(this.getClass().getSimpleName() + " fired!");
    }
}

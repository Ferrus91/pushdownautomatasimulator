package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 21/07/13
 * Time: 14:44
 * To change this template use File | Settings | File Templates.
 */
public class PDACompleteEvent implements EventObject {
    static Logger logger = Logger.getLogger(PDACompleteEvent.class);

    @Getter
    private Boolean isSuccessful;
    @Getter
    private String processedInput;

    @Override
    public EventBusInstance getInstanceToken() {
        return EventBusInstance.DRAWING;
    }

    public PDACompleteEvent(Boolean isSuccessful, String processedInput) {
        logger.trace(this.getClass().getSimpleName() + " fired! Successful? " + isSuccessful);
        this.isSuccessful = isSuccessful;
        this.processedInput = processedInput;
    }
}

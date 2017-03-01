package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 26/08/13
 * Time: 23:40
 * To change this template use File | Settings | File Templates.
 */
public class NonDeterministicJumpEvent implements EventObject {
    static Logger logger = Logger.getLogger(NonDeterministicJumpEvent.class);

    @Getter
    private final String message;
    @Getter
    private String label;
    @Getter
    private String stack;
    @Getter
    private String processedInput;

    public NonDeterministicJumpEvent(String label, String stack, String processedInput, String message) {
        logger.trace(this.getClass().getSimpleName() + " fired! On label: " + label + " and stack: "
                + stack + " and processed " + processedInput);
        this.label = label;
        this.stack = stack;
        this.processedInput = processedInput;
        this.message = message;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        return EventBusInstance.ANIMATION;
    }
}

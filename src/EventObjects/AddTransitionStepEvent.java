package EventObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 15/08/13
 * Time: 14:21
 * To change this template use File | Settings | File Templates.
 */
@AllArgsConstructor
public class AddTransitionStepEvent implements EventObject {
    static Logger logger = Logger.getLogger(AddTransitionStepEvent.class);
    @Getter
    Character transitionChar;
    @Getter
    String pushString;
    @Getter
    Character popChar;
    @Getter
    String id;

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired!");
        return EventBusInstance.DRAWING;
    }
}

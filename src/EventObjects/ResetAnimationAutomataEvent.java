package EventObjects;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 26/08/13
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
public class ResetAnimationAutomataEvent implements EventObject {
    static Logger logger = Logger.getLogger(ResetAnimationAutomataEvent.class);

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired!");
        return EventBusInstance.ANIMATION;
    }
}

package EventObjects;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 29/08/13
 * Time: 10:15
 * To change this template use File | Settings | File Templates.
 */
public class DeactivateAnimationButtonsEvent implements EventObject {
    static Logger logger = Logger.getLogger(DeactivateAnimationButtonsEvent.class);

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired!");
        return EventBusInstance.ANIMATION;
    }
}

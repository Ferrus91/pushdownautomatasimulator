package EventObjects;

import Controller.ControllerLogic.TransitionMousePressContext;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 12/08/13
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionCreationEvent implements EventObject {
    static Logger logger = Logger.getLogger(ConnectionCreationEvent.class);
    @Getter
    TransitionMousePressContext transitionMousePressContext;

    public ConnectionCreationEvent(TransitionMousePressContext transitionmousepresscontext) {
        this.transitionMousePressContext = transitionmousepresscontext;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! With connection "
                + transitionMousePressContext.getStartUUID() + "-" + transitionMousePressContext.getEndUUID());
        return EventBusInstance.DRAWING;
    }
}

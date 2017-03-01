package EventObjects;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/07/13
 * Time: 00:37
 * To change this template use File | Settings | File Templates.
 */
public class ResetAutomataEvent implements EventObject {
    static Logger logger = Logger.getLogger(ResetAutomataEvent.class);

    public Blink blink;
    public ResetAutomataEvent(Blink blink)
    {
        this.blink = blink;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired!");
        return EventBusInstance.DRAWING;
    }
}

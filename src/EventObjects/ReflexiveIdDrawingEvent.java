package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 15/08/13
 * Time: 21:31
 * To change this template use File | Settings | File Templates.
 */
public abstract class ReflexiveIdDrawingEvent implements EventObject {
    static Logger logger = Logger.getLogger(ReflexiveIdDrawingEvent.class);
    @Getter
    private final String id;

    public ReflexiveIdDrawingEvent(String id) {
        this.id = id;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        return EventBusInstance.DRAWING;
    }

    public abstract String methodName();
}

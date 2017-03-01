package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 21/08/13
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */
public class CanvasResizeEvent implements EventObject {
    static Logger logger = Logger.getLogger(CanvasResizeEvent.class);

    @Getter
    private final Integer canvasSize;

    public CanvasResizeEvent(Integer canvasSize) {
        this.canvasSize = canvasSize;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! With canvasSize" + canvasSize);
        return EventBusInstance.DRAWING;
    }
}

package EventObjects;

import Model.DrawingModel.IDrawingLists;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 05/08/13
 * Time: 23:29
 * To change this template use File | Settings | File Templates.
 */
public class CurrentDrawEvent implements EventObject {
    static Logger logger = Logger.getLogger(CurrentDrawEvent.class);

    @Getter
    IDrawingLists IDrawingLists;

    public CurrentDrawEvent(IDrawingLists IDrawingLists) {
        this.IDrawingLists = IDrawingLists;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired!");
        return EventBusInstance.DRAWING;
    }
}

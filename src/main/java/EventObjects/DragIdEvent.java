package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 19/08/13
 * Time: 21:20
 * To change this template use File | Settings | File Templates.
 */
public class DragIdEvent implements EventObject {
    static Logger logger = Logger.getLogger(DragIdEvent.class);

    @Getter
    private String selectId;
    @Getter
    private Integer x;
    @Getter
    private Integer y;

    public DragIdEvent(String selectId, Integer x, Integer y) {
        this.selectId = selectId;
        this.x = x;
        this.y = y;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! With x: " + x + " and " + y + " for:" + selectId);
        return EventBusInstance.DRAWING;
    }
}

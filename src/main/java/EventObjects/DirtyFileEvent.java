package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 29/08/13
 * Time: 11:08
 * To change this template use File | Settings | File Templates.
 */
public class DirtyFileEvent implements EventObject {
    static Logger logger = Logger.getLogger(DirtyFileEvent.class);

    @Getter
    private Integer id;

    public DirtyFileEvent(Integer id) {
        this.id = id;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! With id: " + id);
        return EventBusInstance.DRAWING;
    }
}

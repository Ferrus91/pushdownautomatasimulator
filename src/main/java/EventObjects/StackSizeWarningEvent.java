package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 12/08/13
 * Time: 00:35
 * To change this template use File | Settings | File Templates.
 */
public class StackSizeWarningEvent implements EventObject {
    static Logger logger = Logger.getLogger(StackSizeWarningEvent.class);

    @Getter
    private Integer size;

    public StackSizeWarningEvent(Integer size) {
       this.size = size;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! Size: "
                + size);
        return EventBusInstance.ANIMATION;
    }
}

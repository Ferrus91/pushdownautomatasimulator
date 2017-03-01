package EventObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 21/07/13
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */

@AllArgsConstructor
public class NewStringEvent implements EventObject {
    static Logger logger = Logger.getLogger(NewStringEvent.class);

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! For new string of " + newString);
        return EventBusInstance.DRAWING;
    }


    @Getter
    private String newString;
}

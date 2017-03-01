package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 15/08/13
 * Time: 12:49
 * To change this template use File | Settings | File Templates.
 */
public class RequestNodeDetailsEvent implements EventObject {
    static Logger logger = Logger.getLogger(RequestNodeDetailsEvent.class);

    @Getter
    private
    String label;
    @Getter
    private
    String editID;

    public RequestNodeDetailsEvent(String editID, String lable) {
        this.editID = editID;
        this.label = label;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! With id " + editID);
        return EventBusInstance.DRAWING;
    }
}

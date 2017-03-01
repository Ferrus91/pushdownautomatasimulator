package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 15/08/13
 * Time: 21:11
 * To change this template use File | Settings | File Templates.
 */
public class EditNodeEvent implements EventObject {
    static Logger logger = Logger.getLogger(EditNodeEvent.class);

    @Getter
    String id;
    @Getter
    String label;
    @Getter
    Boolean isStart;
    @Getter
    Boolean isFinal;
    @Getter
    Boolean isLoopDown;

    public EditNodeEvent(String id, String label, boolean startNodeCheckBoxSelected, boolean finalNodeCheckBoxSelected,
                         boolean loopDownselected) {
        this.id = id;
        this.label = label;
        this.isStart = startNodeCheckBoxSelected;
        this.isFinal = finalNodeCheckBoxSelected;
        this.isLoopDown = loopDownselected;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! With id:  " + id);
        return EventBusInstance.DRAWING;
    }
}

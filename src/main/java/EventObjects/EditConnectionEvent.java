package EventObjects;

import Model.DrawingModel.EditTransitions;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 15/08/13
 * Time: 21:20
 * To change this template use File | Settings | File Templates.
 */
public class EditConnectionEvent implements EventObject {
    static Logger logger = Logger.getLogger(EditConnectionEvent.class);
    @Getter
    private final String id;
    @Getter
    private final ObservableList<EditTransitions> connections;

    public EditConnectionEvent(String id, ObservableList<EditTransitions> connections) {
        this.id = id;
        this.connections = connections;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! With id:  " + id);
        return EventBusInstance.DRAWING;
    }
}

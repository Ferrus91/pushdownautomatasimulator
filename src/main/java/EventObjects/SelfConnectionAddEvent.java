package EventObjects;

import Model.DrawingModel.EditTransitions;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 15/08/13
 * Time: 23:15
 * To change this template use File | Settings | File Templates.
 */
public class SelfConnectionAddEvent implements EventObject {
    static Logger logger = Logger.getLogger(SelfConnectionAddEvent.class);

    @Getter
    private final String idValue;
    @Getter
    private final  ObservableList<EditTransitions>  data;

    public SelfConnectionAddEvent(String idValue, ObservableList<EditTransitions> data) {
        this.idValue = idValue;
        this.data = data;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! Id: "
                + idValue + " Data: " + data.toString());
        return EventBusInstance.DRAWING;
    }
}

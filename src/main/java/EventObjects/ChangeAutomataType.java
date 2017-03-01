package EventObjects;

import Model.AutomataType;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 22/08/13
 * Time: 10:36
 * To change this template use File | Settings | File Templates.
 */
public class ChangeAutomataType implements EventObject {
    static Logger logger = Logger.getLogger(ChangeAutomataType.class);

    @Getter
    private final AutomataType machineType;

    public ChangeAutomataType(AutomataType machineType) {
        this.machineType = machineType;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName()
                + " fired! With machine type: " + machineType);
        return EventBusInstance.DRAWING;
    }
}

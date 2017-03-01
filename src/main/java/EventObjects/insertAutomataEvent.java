package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 25/08/13
 * Time: 20:44
 * To change this template use File | Settings | File Templates.
 */
public class InsertAutomataEvent implements EventObject {
    static Logger logger = Logger.getLogger(InsertAutomataEvent.class);

    @Getter
    private final boolean isClose;
    @Getter
    private String xmlAutomata;

    public InsertAutomataEvent(String xmlAutomata, boolean isClose) {
        this.xmlAutomata = xmlAutomata;
        this.isClose = isClose;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! With xml:  " + xmlAutomata);
        return EventBusInstance.DATABASE;
    }
}

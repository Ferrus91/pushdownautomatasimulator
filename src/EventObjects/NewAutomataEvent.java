package EventObjects;

import Database.DatabaseDirectory;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 26/08/13
 * Time: 00:31
 * To change this template use File | Settings | File Templates.
 */
public class NewAutomataEvent implements EventObject {
    static Logger logger = Logger.getLogger(NewAutomataEvent.class);

    @Getter
    private final Boolean isCloseEvent;
    @Getter
    private String xmlString;
    @Getter
    private String automataName;
    @Getter
    private final DatabaseDirectory selectedItem;

    public NewAutomataEvent(String automataName, DatabaseDirectory selectedItem, String xmlString, Boolean isCloseEvent) {
        this.automataName = automataName;
        this.selectedItem = selectedItem;
        this.xmlString = xmlString;
        this.isCloseEvent = isCloseEvent;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! With automaton name:  " + automataName
                + " for xml: " + xmlString + " is this close? " + isCloseEvent);
        return EventBusInstance.DATABASE;
    }
}

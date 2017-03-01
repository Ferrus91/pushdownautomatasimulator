package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 27/08/13
 * Time: 23:15
 * To change this template use File | Settings | File Templates.
 */
public class LoadAutomataEvent implements EventObject {
    static Logger logger = Logger.getLogger(LoadAutomataEvent.class);

    @Getter
    private Integer id;
    @Getter
    private String xml;

    public LoadAutomataEvent(String xml, Integer id) {
        this.xml = xml;
        this.id = id;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! With xml:  " + xml + " for: " + id);
        return EventBusInstance.DATABASE;
    }
}

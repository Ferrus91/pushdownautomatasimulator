package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 29/08/13
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
public class ReloadAutomataEvent implements EventObject {
        static Logger logger = Logger.getLogger(ReloadAutomataEvent.class);

    @Getter
    private String xml;
    @Getter
    private Integer id;

    public ReloadAutomataEvent(String xml, Integer id) {
        this.xml = xml;
        this.id = id;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! With id: " + id + " and xml: "
        + xml);
        return EventBusInstance.DATABASE;
    }
}

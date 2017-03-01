package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 29/08/13
 * Time: 10:52
 * To change this template use File | Settings | File Templates.
 */
public class UpdateAutomataXMLEvent implements EventObject {
    static Logger logger = Logger.getLogger(UpdateAutomataXMLEvent.class);

    @Getter
    private Integer id;
    @Getter
    private String xml;

    public UpdateAutomataXMLEvent(Integer id, String xml) {
        this.id = id;
        this.xml = xml;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! Id: " + id + " xml: " + xml);
        return EventBusInstance.DATABASE;
    }
}

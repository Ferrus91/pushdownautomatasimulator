package EventObjects;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 17/08/13
 * Time: 23:37
 * To change this template use File | Settings | File Templates.
 */
public class SelectIdEvent extends ReflexiveIdDrawingEvent {
    static Logger logger = Logger.getLogger(SelectIdEvent.class);

    public SelectIdEvent(String id) {
        super(id);
        logger.trace(this.getClass().getSimpleName() + " fired! Id: " + id);
    }

    @Override
    public String methodName() {
        return "select";
    }
}

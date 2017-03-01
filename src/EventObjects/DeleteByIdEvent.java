package EventObjects;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 17/08/13
 * Time: 23:31
 * To change this template use File | Settings | File Templates.
 */
public class DeleteByIdEvent extends ReflexiveIdDrawingEvent {
    static Logger logger = Logger.getLogger(DeleteByIdEvent.class);
    public DeleteByIdEvent(String id) {
        super(id);
        logger.trace(this.getClass().getSimpleName() + " fired! With deletion id: "
                + id);
    }

    @Override
    public String methodName() {
        return "delete";
    }
}

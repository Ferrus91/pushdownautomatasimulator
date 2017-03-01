package EventObjects;

import lombok.Getter;
import org.apache.log4j.Logger;

import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/08/13
 * Time: 17:35
 * To change this template use File | Settings | File Templates.
 */
public class RedrawTreeViewEvent implements EventObject {
    static Logger logger = Logger.getLogger(RedrawTreeViewEvent.class);

    @Getter
    private final Connection connection;

    public RedrawTreeViewEvent(Connection connection) {
        this.connection = connection;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired!");
        return EventBusInstance.DATABASE;
    }
}

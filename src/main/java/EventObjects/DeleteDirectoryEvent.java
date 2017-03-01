package EventObjects;

import Database.TreeNodeObject;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 25/08/13
 * Time: 20:25
 * To change this template use File | Settings | File Templates.
 */
public class DeleteDirectoryEvent implements EventObject {
    static Logger logger = Logger.getLogger(DeleteDirectoryEvent.class);

    @Getter
    private TreeNodeObject selectedItem;

    public DeleteDirectoryEvent(TreeNodeObject selectedItem) {
          this.selectedItem = selectedItem;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! With directory name: " +
            selectedItem.getName());
        return EventBusInstance.DATABASE;
    }
}

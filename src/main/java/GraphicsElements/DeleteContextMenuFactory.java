package GraphicsElements;

import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.DeleteByIdEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 20/08/13
 * Time: 22:45
 * To change this template use File | Settings | File Templates.
 */
public class DeleteContextMenuFactory {
    static Logger logger = Logger.getLogger(DeleteContextMenuFactory.class);

    public static ContextMenuWithID create(final String id, final IEventBusServer eventBus)
    {
        final ContextMenuWithID contextMenuWithID = new ContextMenuWithID(id);
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                eventBus.post(new DeleteByIdEvent(id));
            }
        });

        contextMenuWithID.getItems().add(delete);

        return contextMenuWithID;
    }
}

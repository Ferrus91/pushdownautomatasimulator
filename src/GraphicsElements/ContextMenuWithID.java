package GraphicsElements;

import javafx.scene.control.ContextMenu;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 20/08/13
 * Time: 22:47
 * To change this template use File | Settings | File Templates.
 */
public class ContextMenuWithID extends ContextMenu {
    static Logger logger = Logger.getLogger(ContextMenuWithID.class);

    @Getter
    @Setter
    String deleteId;

    ContextMenuWithID(String deleteId)
    {
        super();
        this.deleteId = deleteId;
        logger.trace("Get delete menu for id: " + deleteId);
    }
}

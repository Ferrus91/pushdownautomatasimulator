package DrawingTemplate;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 18/08/13
 * Time: 16:53
 * To change this template use File | Settings | File Templates.
 */
@AllArgsConstructor
public class CircleEventHandler implements EventHandler<MouseEvent> {
    static Logger logger = Logger.getLogger(CircleEventHandler.class);

    private Node pane;

    /**
     * gets circle object
     * @param mouseEvent
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        pane.requestFocus();
        pane.fireEvent(mouseEvent);
    }
}

package DrawingTemplate;

import Controller.ControllerLogic.NodeConnections;
import javafx.geometry.Point2D;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 17/07/13
 * Time: 22:14
 * An object that generates the necessary co-ordinate locations.
 */
public class TransitionDrawingObject extends AbstractTransitionObject<NodeConnections> {
    static Logger logger = Logger.getLogger(TransitionDrawingObject.class);

    public TransitionDrawingObject(NodeConnections nodeConnections) {
        initialise(nodeConnections);
        constructDrawingObject();
    }

    public TransitionDrawingObject(NodeConnections nodeConnections, boolean isUpNode) {
        initialise(nodeConnections);
        constructLoopObject(isUpNode);
    }

    private void initialise(NodeConnections nodeConnections) {
        start = new Point2D(nodeConnections.getFromNode().getX() + NODEDIAMETER/2,nodeConnections.getFromNode().getY() + NODEDIAMETER/2);
        end = new Point2D(nodeConnections.getToNode().getX() + NODEDIAMETER/2,nodeConnections.getToNode().getY() + NODEDIAMETER/2);
        logger.trace(nodeConnections.getFromNode().getId() + " is from node " +
                "initialised at x: " + start.getX() + " and y: " + start.getY());
        logger.trace(nodeConnections.getFromNode().getId() + " is to node " +
                "initialised at x: " + end.getX() + " and y: " + end.getY());
    }

}

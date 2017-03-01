package DrawingTemplate;

import PDAGraphicElements.PDANode;
import javafx.geometry.Point2D;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 14/08/13
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
public class TransitionEditObject extends AbstractTransitionObject<ArrayList<PDANode>> {
    static Logger logger = Logger.getLogger(TransitionEditObject.class);

    public TransitionEditObject(ArrayList<PDANode> pdaNodes) {
        initialise(pdaNodes);
        constructDrawingObject();
    }

    public TransitionEditObject(ArrayList<PDANode> pdaNodes, boolean isUpNode) {
        initialise(pdaNodes);
        constructLoopObject(isUpNode);
    }

    private void initialise(ArrayList<PDANode> pdaNodes) {
        start = new Point2D(pdaNodes.get(RelevantNodeFactory.FROMINDEX).getCenterX(), pdaNodes.get(RelevantNodeFactory.FROMINDEX).getCenterY());
        end = new Point2D(pdaNodes.get(RelevantNodeFactory.TOINDEX).getCenterX(), pdaNodes.get(RelevantNodeFactory.TOINDEX).getCenterY());
        logger.trace(pdaNodes.get(RelevantNodeFactory.FROMINDEX).getUuid() + " is from node " +
                "initialised at x: " + start.getX() + " and y: " + start.getY());
        logger.trace(pdaNodes.get(RelevantNodeFactory.TOINDEX).getUuid() + " is to node " +
                "initialised at x: " + end.getX() + " and y: " + end.getY());
    }
}

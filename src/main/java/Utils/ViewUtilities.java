package Utils;

import Controller.ControllerLogic.NodeConnection;
import PDAGraphicElements.PDANode;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 22/07/13
 * Time: 22:07
 * Miscellaneous util functions used in drawing process
 */
public class ViewUtilities {
    static Logger logger = Logger.getLogger(ViewUtilities.class);

    /**
     * Resizes a scroll pane
     * @param scrollPane
     * @param initialWidth
     * @param initialHight
     */
    public static void setToInitialProportions(ScrollPane scrollPane, double initialWidth, double initialHight)
    {
        scrollPane.resize(initialWidth, initialHight);
    }

    /**
     *
     * @param node
     * @param circleNode
     * @return used for determining if two circles are intersecting by calculating the distance between their centres
     */
    public static Double circleCentreDistances(Circle node, Circle circleNode) {
        logger.trace("Get circle centre distances");
        return Math.sqrt((node.getCenterX() - circleNode.getCenterX())*(node.getCenterX() - circleNode.getCenterX())
                + (node.getCenterY() - circleNode.getCenterY())*(node.getCenterY() - circleNode.getCenterY()));
    }

    /**
     *      Sets scroll pane up
     */

    public static void setUpCanvas(ScrollPane scrollPane, Canvas canvas)
    {
        logger.trace("Set up canvas");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        canvas.heightProperty().setValue(scrollPane.getHeight());
        canvas.widthProperty().setValue(scrollPane.getWidth());
    }

    /**
     * Resets the vector graphic canvas
     * @param canvas
     */

    public static void reset(Canvas canvas) {
        logger.trace("Reset canvas");
        // This function is taken from: http://docs.oracle.com/javafx/2/canvas/jfxpub-canvas.htm#BCFDFDFH
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth()*100, canvas.getHeight()*100);
    }

    /**
     *
     * @param vectorX the co-ordinates making up the x parts of the vector
     * @param vectorY  the co-ordinates making up the y parts of the vector
     * @param tipLocationX the offset from origin in x
     * @param tipLocationY the offset from origin in y
     * @param angle  the angle to rotate by
     */

    public static void rotateVector(double[] vectorX, double[] vectorY, double tipLocationX, double tipLocationY, double angle)
    {
        logger.trace("Rotating vector");
        for(int i = 0; i< vectorX.length && i <vectorY.length; i++)
        {
            double tempXLoc = tipLocationX + (vectorX[i] - tipLocationX)*Math.cos(angle) -
                    (vectorY[i] - tipLocationY)*Math.sin(angle);
            double tempYLoc = tipLocationY + (vectorX[i] - tipLocationX)*Math.sin(angle)
                    + (vectorY[i] - tipLocationY)*Math.cos(angle);
            vectorX[i] = tempXLoc;
            vectorY[i] = tempYLoc;
        }
    }

    /**
     *
     * @param transitionOptions
     * @return the string which will be displayed by the textbox
     */
    public static String drawTextBoxSetUp(ArrayList<NodeConnection> transitionOptions) {
        logger.trace("Drawing textbox");
        if(transitionOptions == null || transitionOptions.size() == 0)
            return "";
        NodeConnection topNode = transitionOptions.get(0);
        String drawText;
        if(transitionOptions.size() > 1)
            return drawText = topNode.toString() + "...";
        else
            return drawText = topNode.toString();
    }

    /**
     * Get a pdanode by ID
     * @param children
     * @param Id the UUID of the node
     */
    public static PDANode searchForNode(ObservableList<Node> children, String Id) {
        logger.trace("Searching for node with id: " + Id);
        for(Node node : children)
        {
            if (node instanceof PDANode)
            {
                PDANode pdaNode = (PDANode) node;
                if(pdaNode.getUuid().equals(Id))
                {
                    return pdaNode;
                }
            }
        }
        return null;
    }
}

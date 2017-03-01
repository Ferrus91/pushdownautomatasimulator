package Controller.RunnerActionStrategies;

import DrawingTemplate.TransitionDrawingObject;
import PDAGraphicElements.PDANode;
import PDAGraphicElements.PDATransRelation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 10/09/13
 * Time: 19:17
 * To change this template use File | Settings | File Templates.
 */
public class CircleStrategies implements ICircleStrategies {
    static final Integer NODEDIAMETER;

    static Logger logger = Logger.getLogger(CircleStrategies.class);

    @Setter
    private Pane pane;

    static
    {
        Properties properties = new Properties();
        try {
            properties.load(TransitionDrawingObject.class.getClassLoader()
                    .getResourceAsStream("Properties/pdaapp.properties"));
        } catch (IOException e) {e.printStackTrace();}

        NODEDIAMETER = Integer.parseInt(properties.getProperty("nodediameter"));
    }

    @Override
    public boolean checkIntersection(double x, double y) {
        for(Node node : pane.getChildren())
        {
            if(node instanceof Circle)
            {
                if(centreDistances((Circle) node, x,y) < NODEDIAMETER/2)
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkIntersectionCircleExcludingId(double x, double y, String id) {
        for(Node node : pane.getChildren())
        {
            if(node instanceof PDANode)
            {
                PDANode pdaNode = (PDANode)node;
                if(centreDistances(pdaNode, x,y) < NODEDIAMETER/2 && !pdaNode.getUuid().equals(id))
                    return true;
            }
        }
        return false;
    }

    @Override
    public String intersectionID(double x, double y) {
        logger.trace("Get intersection ID");
        for(Node node : pane.getChildren())
        {
            if(node instanceof PDANode)
            {
                PDANode pdaNode = (PDANode)node;
                if(centreDistances(pdaNode, x,y) < NODEDIAMETER/2)
                    return pdaNode.getUuid();
            }
            else if (node instanceof PDATransRelation)
            {
                PDATransRelation pdaTransRelation = (PDATransRelation)node;
                if(pdaTransRelation.getBoundsInLocal().contains(x,y))
                {
                    return pdaTransRelation.getUuid();
                }
            }
        }
        return null;
    }

    private Double centreDistances(Circle node, double x, double y) {
        return Math.sqrt((node.getCenterX() - x)*(node.getCenterX() - x)
                + (node.getCenterY() - y)*(node.getCenterY() - y));
    }
}

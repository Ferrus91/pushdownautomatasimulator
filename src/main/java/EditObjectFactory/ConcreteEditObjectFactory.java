package EditObjectFactory;

import DrawingTemplate.ConcreteDrawingTemplate;
import PDAGraphicElements.PDANode;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 10/08/13
 * Time: 17:05
 * To change this template use File | Settings | File Templates.
 */
public class ConcreteEditObjectFactory implements IEditObjectFactory {

    private static final Integer NODEDIAMETER;

    static Logger logger = Logger.getLogger(ConcreteEditObjectFactory.class);

    static
    {
        Properties properties = new Properties();
        try {
            properties.load(ConcreteDrawingTemplate.class.getClassLoader()
                    .getResourceAsStream("Properties/pdaapp.properties"));
        } catch (IOException e) {e.printStackTrace();}
        NODEDIAMETER = Integer.parseInt(properties.getProperty("nodediameter"));
    }

    /**
     *
     * @param uuid ID of node
     * @param x X co-ordinate of node
     * @param y Y co-ordinate of node
     * @return returns a standard node with specified details
     */

    @Override
    public PDANode createPDANode(String uuid, double x, double y, Boolean isDownNode) {
        PDANode returnNode = new PDANode(uuid, isDownNode);
        returnNode.setCenterX(x);
        returnNode.setCenterY(y);
        returnNode.setRadius(NODEDIAMETER/2);
        returnNode.setFill(Color.WHITE);
        returnNode.setStroke(Color.BLACK);

        logger.trace("Node created at x: " + x + " and y: " + y);

        return returnNode;
    }
}

package SugiyamaAdapter;

import Model.DrawingModel.IDrawingLists;
import Model.INode;
import javafx.geometry.Point2D;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 15/08/13
 * Time: 17:53
 * To change this template use File | Settings | File Templates.
 */
public class PointCollator {
    static Logger logger = Logger.getLogger(PointCollator.class);

    public static IDrawingLists pointsCollator (IDrawingLists IDrawingLists, HashMap<String,Point2D> points)
    {
        logger.trace("Collating points");
        for(INode node : IDrawingLists.getNodes().values())
        {
            node.setX((int)points.get(node.getId()).getX());
            node.setY((int)points.get(node.getId()).getY());
        }

        return IDrawingLists;
    }
}

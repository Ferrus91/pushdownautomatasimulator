package PDAGraphicElements;

import javafx.geometry.Point2D;
import javafx.geometry.Point2DBuilder;
import javafx.scene.shape.Circle;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 10/08/13
 * Time: 16:36
 * To change this template use File | Settings | File Templates.
 */
public class PDANode extends Circle implements UUIDDecoratedShape {
    static Logger logger = Logger.getLogger(PDANode.class);

    /**
     * adds id to Circle as a PDA Node
     * @param uuid
     * @param isDownNode
     */
    public PDANode(String uuid, Boolean isDownNode)
    {
        this.uuid = uuid;
        this.isDownNode = isDownNode;
    }

    /**
     * checks if a point intersects with the node
     * @param x
     * @param y
     * @return
     */
    public Boolean pointIntersects(Double x, Double y)
    {
        logger.trace("Checking for intersection on " + this.getUuid());
        Point2D point = Point2DBuilder.create()
                .x(x)
                .y(y)
                .build();
        Point2D nodeCentre = Point2DBuilder.create()
                .x(this.getCenterX())
                .y(this.getCenterY())
                .build();
        return point.distance(nodeCentre) < this.getRadius();
    }

    @Getter
    public String uuid;
    @Getter
    public Boolean isDownNode;
}

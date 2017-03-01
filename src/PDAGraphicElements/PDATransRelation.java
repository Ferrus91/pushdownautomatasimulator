package PDAGraphicElements;

import javafx.scene.paint.Color;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 10/08/13
 * Time: 16:50
 * To change this template use File | Settings | File Templates.
 */
public class PDATransRelation extends Path implements UUIDDecoratedShape {
    static Logger logger = Logger.getLogger(PDATransRelation.class);

    /**
     * adds id to path for pda app
     * @param uuid
     * @param startX
     * @param startY
     * @param controlX
     * @param controlY
     * @param endX
     * @param endY
     */
    public PDATransRelation(String uuid, double startX,
                            double startY,
                            double controlX,
        double controlY,
        double endX,
        double endY)
        {
            // inspired by http://docs.oracle.com/javafx/2/api/javafx/scene/shape/Path.html

            super();

        super.setStroke(Color.BLACK);

        MoveTo moveTo = new MoveTo();
        moveTo.setX(startX);
        moveTo.setY(startY);

        QuadCurveTo quadCurveTo = new QuadCurveTo();
        quadCurveTo.setX(endX);
        quadCurveTo.setY(endY);
        quadCurveTo.setControlX(controlX);
        quadCurveTo.setControlY(controlY);

        super.getElements().add(moveTo);
        super.getElements().add(quadCurveTo);

        this.uuid = uuid;

            logger.trace("Creating PDA loop with start and end point X: " + startX
                    + "start and end Y" + startY
                    + " mid point X " + controlX + " mid point Y "
                    + controlY + " end point X: " +
                    endX + " end point Y " + endY);

        }

    @Getter
    public String uuid;
}

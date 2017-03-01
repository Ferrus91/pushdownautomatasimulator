package PDAGraphicElements;

import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 10/08/13
 * Time: 16:37
 * To change this template use File | Settings | File Templates.
 */
public class PDALoopRelation extends Path implements UUIDDecoratedShape {
    static Logger logger = Logger.getLogger(PDALoopRelation.class);

    /**
     * adds id to self-loop for pda nodes
     * @param uuid
     * @param startX
     * @param startY
     * @param firstX
     * @param firstY
     * @param secondX
     * @param secondY
     */
    public PDALoopRelation(String uuid, double startX, double startY, double firstX, double firstY, double secondX, double secondY)
    {
        // inspired by http://docs.oracle.com/javafx/2/api/javafx/scene/shape/Path.html

        super();

        super.setStroke(Color.BLACK);

        MoveTo moveTo = new MoveTo();
        moveTo.setX(startX);
        moveTo.setY(startY);

        CubicCurveTo cubicCurveTo = new CubicCurveTo();
        cubicCurveTo.setX(startX);
        cubicCurveTo.setY(startY);
        cubicCurveTo.setControlX1(firstX);
        cubicCurveTo.setControlY1(firstY);
        cubicCurveTo.setControlX2(secondX);
        cubicCurveTo.setControlY2(secondY);

        super.getElements().add(moveTo);
        super.getElements().add(cubicCurveTo);

        this.uuid = uuid;

        logger.trace("Creating PDA loop with start and end X: " + startX
                + "start and end Y" + startY
            + " bulge point 1 X " + firstX + " bulge point 2 Y "
                + firstY + " bulge point 2 X: " +
            secondX + " bulge point 2 Y " + secondY);
    }

    @Getter
    public String uuid;
}
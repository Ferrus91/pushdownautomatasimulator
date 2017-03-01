package Controller.ControllerLogic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 11/08/13
 * Time: 23:29
 * To change this template use File | Settings | File Templates.
 */
public class OverlayBoxStrategy {
    static Logger logger = Logger.getLogger(OverlayBoxStrategy.class);

    private static final double DISPLAYBOXEDGE = 5;

    /**
     *
     * @param gc - vector graphic context
     * @param hiddenStateBox - the particular box of transitions that is to be drawn
     */
    public void drawOverlayBox(GraphicsContext gc, FullDisplayBox hiddenStateBox)
    {
        gc.save();
        gc.setFill(Color.YELLOW);
        Text textBox = new Text(hiddenStateBox.getFullText());
        gc.strokeRect(hiddenStateBox.getBottomX() - textBox.getLayoutBounds().getWidth() - DISPLAYBOXEDGE,
                hiddenStateBox.getBottomY() - textBox.getLayoutBounds().getHeight() / 2 - DISPLAYBOXEDGE,
                textBox.getLayoutBounds().getWidth() + DISPLAYBOXEDGE,
                textBox.getLayoutBounds().getHeight() / 2 + DISPLAYBOXEDGE);
        gc.fillRect(hiddenStateBox.getBottomX() -  textBox.getLayoutBounds().getWidth() - DISPLAYBOXEDGE,
                hiddenStateBox.getBottomY() - textBox.getLayoutBounds().getHeight()/2 - DISPLAYBOXEDGE,
                textBox.getLayoutBounds().getWidth() + DISPLAYBOXEDGE,
                textBox.getLayoutBounds().getHeight()/2 + DISPLAYBOXEDGE);
        logger.trace("Drwaing overlay box at x: " + hiddenStateBox.getBottomX() +
                " and y : " + hiddenStateBox.getBottomY());
        gc.restore();
        gc.fillText(hiddenStateBox.getFullText(), hiddenStateBox.getTopX(), hiddenStateBox.getTopY());
    }

    /**
     *
     * @param fullDisplayBox - the box which is being tested for intersection
     * @param mouseEvent - current mouse event triggering the test
     * @param zoomValue - current zoom factor so that the bounding boxes are in synch
     * @return Boolean value of whether item is in intersection
     */

    public Boolean validBox(FullDisplayBox fullDisplayBox, MouseEvent mouseEvent, Double zoomValue)
    {
        logger.trace("Valid box at x: " + mouseEvent.getX() + " " + mouseEvent.getY() );
        return mouseEvent.getY() > zoomValue*fullDisplayBox.getTopY()
                && mouseEvent.getY() < zoomValue*fullDisplayBox.getBottomY()
                && mouseEvent.getX() > zoomValue*fullDisplayBox.getTopX()
                && mouseEvent.getX() < zoomValue*fullDisplayBox.getBottomX();
    }
}

package DrawingTemplate;

import Controller.ControllerLogic.FullDisplayBox;
import Controller.ControllerLogic.NodeConnection;
import Controller.ControllerLogic.NodeConnections;
import Model.AnimationModel.IInstantaneousDescription;
import Model.AnimationModel.ITransition;
import Model.INode;
import Utils.ViewUtilities;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 17/07/13
 * Time: 20:02
 * The template for the animation part of the application.
 */
public class ConcreteDrawingTemplate extends IDrawingTemplate
{
    static Logger logger = Logger.getLogger(ConcreteDrawingTemplate.class);

    @Getter
    @Setter
    private ITransition lastSelectedTransition;

    private static final Integer NODEDIAMETER;
    private static final Integer ARROWHEADSIZE;
    private static final Double SQRT3 = Math.sqrt(3);
    private static final Double LOOPARROWHEADKINK;
    private static final Double FINALNODEINPER;
    static
    {
        Properties properties = new Properties();
        try {
            properties.load(ConcreteDrawingTemplate.class.getClassLoader()
                    .getResourceAsStream("Properties/pdaapp.properties"));
        } catch (IOException e) {e.printStackTrace();}
        NODEDIAMETER = Integer.parseInt(properties.getProperty("nodediameter"));
        ARROWHEADSIZE = Integer.parseInt(properties.getProperty("arrowheadsize"));
        LOOPARROWHEADKINK = Double.parseDouble(properties.getProperty("looparrowheadkink"));
        FINALNODEINPER = Double.parseDouble(properties.getProperty("finalnodeinper"));
    }
    private GraphicsContext gc;

    @Inject
    ConcreteDrawingTemplate(@Assisted GraphicsContext gc)
    {
        this.gc = gc;
    }

    /**
     * Draws the node as a vector graphic
     * @param node
     * @param iInstantaneousDescription
     */
    @Override
    public void drawNode(INode node, IInstantaneousDescription iInstantaneousDescription) {
        if(iInstantaneousDescription.getCurrNode() == node)
        {
            logger.trace("Drawing current node: " + node.getId());
            // linear gradient effect green to black
            LinearGradient lg
                    = LinearGradient.valueOf("linear-gradient(from 0% 0% to 100% 200%, green  0% , black 100%)");
            gc.setFill(lg);
        }
    else
        {
            logger.trace("Drawing non-current node: " + node.getId());
            gc.setFill(javafx.scene.paint.Color.WHITE);
        }
    gc.fillOval(node.getX(),node.getY(),NODEDIAMETER,NODEDIAMETER);
    gc.strokeOval(node.getX(), node.getY(), NODEDIAMETER, NODEDIAMETER);


        Text nodeLabel = new Text(labelCutter(node.getLabel()));

        gc.save();
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Tahoma",15));
        gc.fillText(nodeLabel.getText(), node.getX() + NODEDIAMETER/2 - nodeLabel.getLayoutBounds().getWidth()/2,
                node.getY() + NODEDIAMETER/2 + nodeLabel.getLayoutBounds().getHeight()/2);
        gc.restore();

        // Final nodes get a double ring of concentric circles as in standard diagrams
    if(node.getIsFinalNode())
    {
        logger.trace("Drawing final node on: " + node.getId());
            gc.strokeOval(node.getX() + NODEDIAMETER/2*(1-FINALNODEINPER),
        node.getY() + NODEDIAMETER/2*(1-FINALNODEINPER), NODEDIAMETER*FINALNODEINPER,
    NODEDIAMETER*FINALNODEINPER);
    }
}

    /**
     * a start node has a special arrow pointing out the side as in most standard diagrams
     * @param node
     * @param iInstantaneousDescription
     */
    @Override
    public void drawStartNode(INode node, IInstantaneousDescription iInstantaneousDescription) {
        StartWedgeCoordinates startWedgeCoordinates = new StartWedgeCoordinates(node.getX(), node.getY());

        gc.strokeLine(startWedgeCoordinates.tipX, startWedgeCoordinates.tipY, startWedgeCoordinates.firstDashX[0],
                startWedgeCoordinates.firstDashY[0]);
        gc.strokeLine(startWedgeCoordinates.tipX2, startWedgeCoordinates.tipY2,
                startWedgeCoordinates.secondDashX[0], startWedgeCoordinates.secondDashY[0]);

        drawNode(node, iInstantaneousDescription);
    }

    /**
     * Draws the relations between two nodes by determining if transition is to itself or somewhere else and passing on
     * to helper function
     * @param nodeConnections
     * @param fullTextDisplays
     */
    @Override
    public void drawRelations(NodeConnections nodeConnections, ArrayList<FullDisplayBox> fullTextDisplays) {
        if(nodeConnections.containsLastSelectedTransition(lastSelectedTransition))
        {
            logger.debug("Drawing last selected transition: " + lastSelectedTransition.toString());
            gc.setStroke(Color.GOLD);
            gc.setFill(Color.GOLD);
        }
        else
        {
            gc.setStroke(Color.BLACK);
            gc.setFill(Color.BLACK);
        }
        if(nodeConnections.getToNode() != nodeConnections.getFromNode())
        {
            drawTransNode(gc, nodeConnections, fullTextDisplays);
        }
        else
        {
            drawLoopNode(gc, nodeConnections, fullTextDisplays);
        }
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);
    }

    /**
     * draws a loop to itself as a cubic bezier curve with two points attached to itself and another two in a trapezium
     * location
     * @param gc
     * @param nodeConnections
     * @param fullTextDisplays
     */
    private void drawLoopNode(GraphicsContext gc, NodeConnections nodeConnections,
                              ArrayList<FullDisplayBox> fullTextDisplays) {
        logger.trace("Drawing transition on same nodes: " + nodeConnections.toString() +
        " on " + nodeConnections.getFromNode().getId());

        Boolean isDownNode = nodeConnections.getFromNode().getIsNodeDown();

        ILoopCoords transitionDrawingObject = new TransitionDrawingObject(nodeConnections,
                isDownNode);

        gc.beginPath();
        gc.moveTo(transitionDrawingObject.getStartX(), transitionDrawingObject.getStartY());

        gc.bezierCurveTo(transitionDrawingObject.getFirstX(), transitionDrawingObject.getFirstY(),
                transitionDrawingObject.getSecondX(), transitionDrawingObject.getSecondY(),
                transitionDrawingObject.getStartX(), transitionDrawingObject.getStartY());
        if(isDownNode)
            drawArrowhead(gc, transitionDrawingObject.getStartX(), transitionDrawingObject.getStartY(),
                    LOOPARROWHEADKINK + Math.atan2(transitionDrawingObject.getSecondX() - transitionDrawingObject.getStartY(),
                            transitionDrawingObject.getSecondX() - transitionDrawingObject.getStartX()));
        else
            drawArrowhead(gc, transitionDrawingObject.getStartX(), transitionDrawingObject.getStartY(),
                    -LOOPARROWHEADKINK + Math.PI + Math.atan2(transitionDrawingObject.getSecondX() - transitionDrawingObject.getStartY(),
                            transitionDrawingObject.getSecondX() - transitionDrawingObject.getStartX()));
        gc.stroke();
        gc.closePath();
        drawTextBoxLoop(gc, (transitionDrawingObject.getFirstX() + transitionDrawingObject.getSecondX())/2,
                transitionDrawingObject.getSecondY(), nodeConnections.getTransitionOptions(),
                nodeConnections.getToNode().getIsNodeDown(), nodeConnections, fullTextDisplays);
    }

    /**
     * Draws a transition between two nodes as a quadratic bezier, with a bulge between the two as the central
     * point
     * @param gc
     * @param nodeConnections
     * @param fullTextDisplays
     */
    private void drawTransNode(GraphicsContext gc, NodeConnections nodeConnections,
                               ArrayList<FullDisplayBox> fullTextDisplays) {
        logger.trace("Drawing transition between nodes: " + nodeConnections.toString()
                + " on " + nodeConnections.getFromNode() + " to " + nodeConnections.getToNode());
        ITransCoords transitionDrawingObject = new TransitionDrawingObject(nodeConnections);

        gc.beginPath();
        gc.moveTo(transitionDrawingObject.getXAngleOnFirstCircle(), transitionDrawingObject.getYAngleOnFirstCircle());

        gc.quadraticCurveTo(transitionDrawingObject.getXBulge(), transitionDrawingObject.getYBulge(),
                transitionDrawingObject.getXAngleOnSecondCircle(), transitionDrawingObject.getYAngleOnSecondCircle());
        gc.stroke();

        if(transitionDrawingObject.getStart().getY() == transitionDrawingObject.getEnd().getY()
                && transitionDrawingObject.getYDiff() < 0 && transitionDrawingObject.getXDiff()  < 0)
            drawArrowhead(gc, transitionDrawingObject.getXAngleOnFirstCircle(),
                    transitionDrawingObject.getYAngleOnFirstCircle(), transitionDrawingObject.getAnglesInDegress() +
                    Math.atan2(transitionDrawingObject.getYDiff(),transitionDrawingObject.getXDiff()));
        else
        {
            if(transitionDrawingObject.getXDiff() > 0 && transitionDrawingObject.getYDiff() < 0
                    || transitionDrawingObject.getXDiff() < 0 && transitionDrawingObject.getYDiff() > 0)
                drawArrowhead(gc, transitionDrawingObject.getXAngleOnFirstCircle(),
                        transitionDrawingObject.getYAngleOnFirstCircle(), transitionDrawingObject.getAnglesInDegress()
                        + Math.PI/2 + Math.atan2(transitionDrawingObject.getYDiff(),transitionDrawingObject.getXDiff()));
            else
                drawArrowhead(gc, transitionDrawingObject.getXAngleOnFirstCircle(),
                        transitionDrawingObject.getYAngleOnFirstCircle(), transitionDrawingObject.getAnglesInDegress()
                        + Math.PI + Math.atan2(transitionDrawingObject.getYDiff(),transitionDrawingObject.getXDiff()));
        }
        gc.closePath();
        drawTextBox(gc, transitionDrawingObject.getXAngleOnFirstCircle(),
                transitionDrawingObject.getYAngleOnFirstCircle(),
                transitionDrawingObject.getXBulge(), transitionDrawingObject.getYBulge(),
                nodeConnections.getTransitionOptions(), nodeConnections, fullTextDisplays);
    }

    /**
     * draws the text box that gives information on the transition parameters
     * @param gc
     * @param firstX
     * @param firstY
     * @param secondX
     * @param secondY
     * @param transitionOptions
     * @param nodeConnections
     * @param fullTextDisplays
     */

    private void drawTextBox(GraphicsContext gc, double firstX, double firstY, double secondX, double secondY, ArrayList<NodeConnection> transitionOptions, NodeConnections nodeConnections, ArrayList<FullDisplayBox> fullTextDisplays) {
        gc.setFill(Color.BLACK);
        Text drawText = new Text(ViewUtilities.drawTextBoxSetUp(transitionOptions));
        drawText.setFont(gc.getFont());
        TextBoxBoundsGenerator textBoxBoundsGenerator = new TextBoxBoundsGenerator(drawText,firstX,secondX,firstY,secondY);
        logger.trace("Drawing textbox between nodes: " + drawText.getText());

        if(transitionOptions.size() > 1)
                    fullTextDisplays.add(new FullDisplayBox(textBoxBoundsGenerator.labelX1,
                            textBoxBoundsGenerator.labelY1, textBoxBoundsGenerator.labelX2,
                            textBoxBoundsGenerator.labelY2, nodeConnections.toString()));
                gc.fillText(drawText.getText(), textBoxBoundsGenerator.labelX1,
                        textBoxBoundsGenerator.labelY1);
    }

    /**
     * draws textbox got self-looping node
     * @param gc
     * @param x
     * @param y
     * @param transitionOptions
     * @param isNodeDown
     * @param nodeConnections
     * @param fullTextDisplays
     */

    private void drawTextBoxLoop(GraphicsContext gc, double x, double y, ArrayList<NodeConnection> transitionOptions, Boolean isNodeDown, NodeConnections nodeConnections, ArrayList<FullDisplayBox> fullTextDisplays) {
        gc.setFill(Color.BLACK);
        Text drawText = new Text(ViewUtilities.drawTextBoxSetUp(transitionOptions));
        drawText.setFont(gc.getFont());
        Double textWidth =  drawText.getLayoutBounds().getWidth();
        Double textHeight = drawText.getLayoutBounds().getHeight();

        logger.trace("Drawing textbox loop: " + drawText.getText());

        if(transitionOptions.size() > 1)
            fullTextDisplays.add(new FullDisplayBox(x - textWidth/2,y - textHeight/2,
                    x + textWidth/2,y + textHeight/2,
                    nodeConnections.toString()));
        gc.fillText(drawText.getText(),x - textWidth/2,y + textHeight/2);
    }

    /**
     * code to draw an arrow head on a line (not supported by JavaFx yet)
     * @param gc
     * @param tipLocationX
     * @param tipLocationY
     * @param angle
     */

    private void drawArrowhead(GraphicsContext gc, double tipLocationX, double tipLocationY, double angle)
    {
        double[] xLocations = {tipLocationX, tipLocationX - ARROWHEADSIZE/SQRT3, tipLocationX + ARROWHEADSIZE/SQRT3};
        double[] yLocations = {tipLocationY, tipLocationY - ARROWHEADSIZE, tipLocationY - ARROWHEADSIZE};

        logger.trace("Rotating vector by: " + angle);
        ViewUtilities.rotateVector(xLocations,yLocations,tipLocationX,tipLocationY,angle);

        gc.fillPolygon(xLocations,yLocations,3);
    }

}

package DrawingTemplate;

import javafx.geometry.Point2D;
import lombok.Getter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 14/08/13
 * Time: 14:02
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractTransitionObject<T> implements ITransitionObject<T>, ILoopCoords, ITransCoords {
    static Logger logger = Logger.getLogger(AbstractTransitionObject.class);

    static final Integer NODEDIAMETER;
    static final Double ARCRADIUS;
    static final Double LOOPRADIUS;
    static final Double LOOPBULGERATIO;

    @Getter
    Double xBulge;
    @Getter
    Double yBulge;
    @Getter
    Double deltaX;
    @Getter
    Double deltaY;
    @Getter
    Double anglesInDegress;
    @Getter
    Integer xSlope;
    @Getter
    Integer ySlope;
    @Getter
    Double xAngleOnFirstCircle;
    @Getter
    Double yAngleOnFirstCircle;
    @Getter
    Double xAngleOnSecondCircle;
    @Getter
    Double yAngleOnSecondCircle;
    @Getter
    Point2D start;
    @Getter
    Point2D end;
    @Getter
    boolean isForward;
    @Getter
    Double xDiff;
    @Getter
    Double yDiff;
    @Getter
    Double startY;
    @Getter
    Double startX;
    @Getter
    Double firstY;
    @Getter
    Double firstX;
    @Getter
    Double secondY;
    @Getter
    Double secondX;
    @Getter
    Integer upCoefficient;

    static
    {
        Properties properties = new Properties();
        try {
            properties.load(TransitionDrawingObject.class.getClassLoader()
                    .getResourceAsStream("Properties/pdaapp.properties"));
        } catch (IOException e) {e.printStackTrace();}

        NODEDIAMETER = Integer.parseInt(properties.getProperty("nodediameter"));
        ARCRADIUS = Double.parseDouble(properties.getProperty("arcradius"));
        LOOPRADIUS = Double.parseDouble(properties.getProperty("loopradius"));
        LOOPBULGERATIO = Double.parseDouble(properties.getProperty("loopbulgeratio"));
    }

    /**
     * uses the calculated angles between two nodes to generate points where a transition starts and ends between them.
     * Note that these points get flipped 180 degrees when the transition is in reverse
     * @param isForward
     */
    void setUpAngles(boolean isForward)
    {
        if(isForward)
        {
            xAngleOnFirstCircle = end.getX() + xSlope*NODEDIAMETER/2*Math.cos(-Math.PI -(anglesInDegress));
            yAngleOnFirstCircle = end.getY() + ySlope*NODEDIAMETER/2*Math.sin(-Math.PI -(anglesInDegress));
            xAngleOnSecondCircle = start.getX() - xSlope*NODEDIAMETER/2*Math.cos(-Math.PI -(anglesInDegress) );
            yAngleOnSecondCircle = start.getY() - ySlope*NODEDIAMETER/2*Math.sin(-Math.PI -(anglesInDegress));
        }
        else
        {
            xAngleOnFirstCircle = end.getX() + xSlope*NODEDIAMETER/2*Math.cos(anglesInDegress);
            yAngleOnFirstCircle = end.getY() + ySlope*NODEDIAMETER/2*Math.sin(anglesInDegress);
            xAngleOnSecondCircle = start.getX() - xSlope*NODEDIAMETER/2*Math.cos(anglesInDegress);
            yAngleOnSecondCircle = start.getY() - ySlope*NODEDIAMETER/2*Math.sin(anglesInDegress);
        }
        logger.trace("Node 1 x boundary: " + xAngleOnFirstCircle +
                " Node 1 y boundary: " + yAngleOnFirstCircle +
                " Node 2 x boundary: " + xAngleOnSecondCircle +
                " Node 2 y boundary: " + yAngleOnSecondCircle);
    }

    /**
     * calculates the angle between two nodes as a radian. Note when the node is below the other node the angle is
     * reversed. ySlope and xSlope retain three-valued logic on the position of the nodes relative to each other
     * @param deltaX
     * @param deltaY
     */
    void setUpDelta(Double deltaX, Double deltaY) {
        if(deltaY >= 0)
            anglesInDegress = Math.atan2(deltaY, deltaX);
        else
            anglesInDegress = Math.atan2(-deltaY, -deltaX);
        xSlope = deltaX > 0 ? 1 : (deltaX != 0 ? -1 : 0);
        ySlope = deltaY > 0 ? -1 : (deltaY != 0 ? 1 : 0);
        logger.trace("Angles between nodes are: " + anglesInDegress);
    }

    /**
     * Is the path from one node to another going forward
     */
    boolean isForwardPath(double deltaX, double deltaY)
    {
        boolean forwardPath = (deltaX >= 0 && deltaY >=0) || (deltaX < 0 && deltaY < 0);
        logger.trace("Forward path: " + forwardPath);
        return forwardPath;
    }


    /**
     * Creates the point used to generate the 'bulge' in the quadratic bezier
     */
    void setupDiffs() {
        xBulge = (xAngleOnFirstCircle + xAngleOnSecondCircle)/2 + ySlope*ARCRADIUS
                *LOOPBULGERATIO*start.distance(end);
        yBulge = (yAngleOnFirstCircle + yAngleOnSecondCircle)/2 + xSlope*ARCRADIUS
                *LOOPBULGERATIO*start.distance(end);
        xDiff = xBulge - xAngleOnSecondCircle;
        yDiff = yBulge - yAngleOnSecondCircle;
        logger.trace("Bulge x location = " + xBulge);
        logger.trace("Bulge y location = " + yBulge);
        logger.trace("Difference between bulge and start x location = " + xDiff);
        logger.trace("Difference between bulge and start y location = " + yDiff);    }

    public void constructDrawingObject()
    {
        deltaX = end.getX() - start.getX();
        deltaY = end.getY() - start.getY();

        setUpDelta(deltaX, deltaY);
        isForward = isForwardPath(deltaX,deltaY);
        setUpAngles(isForward);
        setupDiffs();
    }

    /** initialise variables
     *
     * @param upNode
     */
    void constructLoopObject(boolean upNode) {
        upCoefficient = upNode ? 1 : -1;
        startX =  start.getX();
        startY =  start.getY() + upCoefficient*NODEDIAMETER/2;
        firstX = start.getX() + LOOPRADIUS;
        secondX =  start.getX() - LOOPRADIUS;
        firstY = start.getY() + upCoefficient*NODEDIAMETER/2 + upCoefficient*LOOPRADIUS;
        secondY = start.getY() + upCoefficient*NODEDIAMETER/2 + upCoefficient*LOOPRADIUS;
        logger.trace("Starting x: " + startX + "Starting y: " + startY);
        logger.trace("First bulge x: " + firstX + "First bulge y: " + firstY);
        logger.trace("Second bulge x: " + secondX + "Second bulge y: " + secondY);
    }
}

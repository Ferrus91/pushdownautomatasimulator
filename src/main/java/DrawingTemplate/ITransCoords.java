package DrawingTemplate;

import javafx.geometry.Point2D;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 18/08/13
 * Time: 15:27
 * To change this template use File | Settings | File Templates.
 */
public interface ITransCoords {
    Double getXAngleOnFirstCircle();
    Double getXBulge();
    Double getXAngleOnSecondCircle();
    Double getYAngleOnFirstCircle();
    Double getYBulge();
    Double getYAngleOnSecondCircle();
    Double getAnglesInDegress();
    Double getXDiff();
    Double getYDiff();
    Point2D getStart();
    Point2D getEnd();
}

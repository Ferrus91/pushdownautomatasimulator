package Controller.RunnerActionStrategies;

import javafx.scene.layout.Pane;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 11/09/13
 * Time: 00:52
 * To change this template use File | Settings | File Templates.
 */
public interface ICircleStrategies {
    public boolean checkIntersection(double x, double y);
    public boolean checkIntersectionCircleExcludingId(double x, double y, String id);
    public String intersectionID(double x, double y);
    public void setPane(Pane pane);
}

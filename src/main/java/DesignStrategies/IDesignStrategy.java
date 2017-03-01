package DesignStrategies;

import Controller.RunnerActionStrategies.ICircleStrategies;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 04/08/13
 * Time: 18:35
 * To change this template use File | Settings | File Templates.
 */
public interface IDesignStrategy {
    void fireClickEvent(double x, double y, ICircleStrategies circleStrategies);
    void firePressEvent(double x, double y, ICircleStrategies circleStrategies);
    void fireDragEvent(double x, double y, ICircleStrategies circleStrategies, Pane drawingBoard);
    void fireReleaseEvent(MouseEvent mouseEvent, ICircleStrategies editTemplate);
}

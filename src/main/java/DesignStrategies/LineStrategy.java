package DesignStrategies;

import Controller.ControllerLogic.TransitionMousePressContext;
import Controller.RunnerActionStrategies.ICircleStrategies;
import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.ConnectionCreationEvent;
import EventObjects.RequestPaneRedrawEvent;
import PDAGraphicElements.ArrowHeadBuilder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.scene.shape.Polygon;
import org.apache.log4j.Logger;

//import PDAGraphicElements.ArrowHeadBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 04/08/13
 * Time: 18:36
 * The strategy used by the transition button.
 */
public class LineStrategy implements IDesignStrategy{
    static Logger logger = Logger.getLogger(LineStrategy.class);

    @Inject
    private IEventBusServer eventBus;

    private static TransitionMousePressContext TRANSITIONMOUSEPRESSCONTEXT = new TransitionMousePressContext();

    public LineStrategy() {
        Injector injector = Guice.createInjector(new EventBusServerModule(this));
        injector.injectMembers(this);
    }

    @Override
    public void fireClickEvent(double x, double y, ICircleStrategies circleStrategies) {
    }

    /**
     * sets up transition context
     * @param x
     * @param y
     * @param circleStrategies
     */
    @Override
    public void firePressEvent(double x, double y, ICircleStrategies circleStrategies) {
        logger.debug("Setting up transition mouse press context");
      TRANSITIONMOUSEPRESSCONTEXT.setIsActive(true);
      TRANSITIONMOUSEPRESSCONTEXT.setStartX(x);
      TRANSITIONMOUSEPRESSCONTEXT.setStartY(y);
    }

    /**
     * uses transition context to draw temporary grey line to current pointer location
     * @param x
     * @param y
     * @param circleStrategies
     * @param drawingBoard
     */
    @Override
    public void fireDragEvent(double x, double y, ICircleStrategies circleStrategies
            , Pane drawingBoard) {
        eventBus.post(new RequestPaneRedrawEvent());

        logger.trace("Drawing grey line");

        if(TRANSITIONMOUSEPRESSCONTEXT.getIsActive())
        {
            Line line = LineBuilder
                    .create()
                    .startX(TRANSITIONMOUSEPRESSCONTEXT.getStartX())
                    .startY(TRANSITIONMOUSEPRESSCONTEXT.getStartY())
                    .endX(x)
                    .endY(y)
                    .stroke(Color.LIGHTGREY)
                    .build();

            Double diffX = x - TRANSITIONMOUSEPRESSCONTEXT.getStartX();
            Double diffY = y - TRANSITIONMOUSEPRESSCONTEXT.getStartY();

            Polygon arrowHead = ArrowHeadBuilder
                    .create(x, y, diffX, diffY);
            arrowHead.setFill(Color.DARKGREY);

            drawingBoard.getChildren().addAll(line, arrowHead);
        }
    }

    /**
     * transition context finalised and used to construct a putative transition
     * @param mouseEvent
     * @param circleStrategies
     */

    @Override
    public void fireReleaseEvent(MouseEvent mouseEvent, ICircleStrategies circleStrategies) {
        logger.debug("Tearing down transition mouse context");
        TRANSITIONMOUSEPRESSCONTEXT.setEndX(mouseEvent.getX());
        TRANSITIONMOUSEPRESSCONTEXT.setEndY(mouseEvent.getY());
        eventBus.post(new RequestPaneRedrawEvent());
        eventBus.post(new ConnectionCreationEvent(TRANSITIONMOUSEPRESSCONTEXT));
        TRANSITIONMOUSEPRESSCONTEXT.reset();
    }
}

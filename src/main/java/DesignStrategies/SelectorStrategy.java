package DesignStrategies;

import Controller.RunnerActionStrategies.ICircleStrategies;
import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.ClosePanesEvent;
import EventObjects.DragIdEvent;
import EventObjects.SelectIdEvent;
import GraphicsElements.ContextMenuWithID;
import GraphicsElements.DeleteContextMenuFactory;
import Utils.DialogueUtils;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 04/08/13
 * Time: 18:37
 * The st
 */
public class SelectorStrategy implements IDesignStrategy {
    static Logger logger = Logger.getLogger(NodeStrategy.class);

    @Inject
    private IEventBusServer eventBus;

    public SelectorStrategy() {
        Injector injector = Guice.createInjector(new EventBusServerModule(this));
        injector.injectMembers(this);
    }

    private String selectId;
    private double originalX;
    private double originalY;


    @Override
    public void fireClickEvent(double x, double y, ICircleStrategies circleStrategies) {
    }

    @Override
    public void firePressEvent(double x, double y, ICircleStrategies circleStrategies) {
        logger.trace("Selection fired");
        originalX = x;
        originalY = y;
        selectId = circleStrategies.intersectionID(x,y);
        if(selectId == null)
            eventBus.post(new ClosePanesEvent());
        eventBus.post(new SelectIdEvent(selectId));
    }

    @Override
    public void fireDragEvent(double x, double y, ICircleStrategies circleStrategies, Pane pane) {
       eventBus.post(new DragIdEvent(selectId,(int)x,(int)y));
    }

    /**
     * moves nodes around so they can be dragged
     * @param mouseEvent
     * @param circleStrategies
     */
    @Override
    public void fireReleaseEvent(MouseEvent mouseEvent, ICircleStrategies circleStrategies) {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
        if(circleStrategies.checkIntersectionCircleExcludingId(mouseEvent.getX(), mouseEvent.getY(),
                circleStrategies.intersectionID(mouseEvent.getX(), mouseEvent.getY())))
        {
            eventBus.post(new DragIdEvent(selectId,(int)originalX,(int)originalY));
            DialogueUtils.warningMessage("Nodes must be spatially separated");
            logger.error("Separation error");
        }
        }
        else
        if(mouseEvent.getButton() == MouseButton.SECONDARY)
        {
            logger.trace("Load context menu");
            ContextMenuWithID contextMenuWithID = DeleteContextMenuFactory.create(selectId, eventBus);
            contextMenuWithID.show((Node)mouseEvent.getSource(), mouseEvent.getX(),mouseEvent.getY());
        }


        selectId = "";

    }
}

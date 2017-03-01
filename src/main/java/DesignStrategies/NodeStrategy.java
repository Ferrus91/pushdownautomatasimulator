package DesignStrategies;

import Controller.RunnerActionStrategies.ICircleStrategies;
import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.NodeCreationEvent;
import Utils.DialogueUtils;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 04/08/13
 * Time: 18:36
 * The strategy used b
 */
public class NodeStrategy implements IDesignStrategy {
    static Logger logger = Logger.getLogger(NodeStrategy.class);

    @Inject
    private IEventBusServer eventBus;

    public NodeStrategy() {
        Injector injector = Guice.createInjector(new EventBusServerModule(this));
        injector.injectMembers(this);
    }

    /**
     * adds nodes and checks for intersection
     * @param x
     * @param y
     * @param circleStrategy
     */
    @Override
    public void fireClickEvent(double x, double y, ICircleStrategies circleStrategy) {
        if(!circleStrategy.checkIntersection(x,y))
        {
            logger.trace("Node creation fired");
            eventBus.post(new NodeCreationEvent(x,y));
        }
       else
        {
           logger.error("Lack of spatial separation");
           DialogueUtils.warningMessage("Nodes must be spatially separated");
        }
    }

    @Override
    public void firePressEvent(double x, double y, ICircleStrategies circleStrategies) {
    }

    @Override
    public void fireDragEvent(double x, double y, ICircleStrategies circleStrategies, Pane drawingBoard) {
    }

    @Override
    public void fireReleaseEvent(MouseEvent mouseEvent, ICircleStrategies circleStrategies) {
    }
}

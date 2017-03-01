package Controller;

import Controller.ControllerLogic.FullDisplayBox;
import Controller.ControllerLogic.NodeConnections;
import Controller.ControllerLogic.OverlayBoxStrategy;
import DependencyInjection.DrawingTemplateModule;
import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import DrawingTemplate.ConcreteArrowMapBuilder;
import DrawingTemplate.IDrawingFactory;
import DrawingTemplate.IDrawingTemplate;
import EventObjects.AutomataChangeEvent;
import EventObjects.Blink;
import EventObjects.ResetAutomataEvent;
import EventObjects.ZoomedEvent;
import Model.AnimationModel.IInstantaneousDescription;
import Model.AnimationModel.IPushDownNode;
import Model.AnimationModel.ITransition;
import Model.AnimationModel.NodeList;
import Model.INode;
import Utils.ViewUtilities;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 26/06/13
 * Time: 21:19
 * To change this template use File | Settings | File Templates.
 */
public class RunController implements Initializable {
    static Logger logger = Logger.getLogger(RunController.class);

    private static final double ZOOM_IN_FACTOR = 1.2;
    private static final double ZOOM_OUT_FACTOR = 0.8;
    private static final Double BUFFER = 150.0;

    private static double INITIALWIDTH;
    private static double INITIALHEIGHT;

    private IDrawingFactory drawingFactory;
    private IDrawingTemplate drawingStrategy;
    private IEventBusServer eventBus;
    private ArrayList<FullDisplayBox> fullTextDisplays;
    private Boolean fullDisplayBoxSet = false;
    private AutomataChangeEvent currentAutomataEvent;
    private Double zoomValue = 1.0;
    private OverlayBoxStrategy overlayBoxStrategy = new OverlayBoxStrategy();
    private ConcreteArrowMapBuilder arrowMapBuilder = new ConcreteArrowMapBuilder();

    @FXML
    private ScrollPane scrollPaneRun;

    @Inject
    public void setEventBus(IEventBusServer eventBus)
    {
        this.eventBus = eventBus;
    }

    @Inject
    public void setDrawingFactory(IDrawingFactory drawingFactory)
    {
        this.drawingFactory = drawingFactory;
    }

    @FXML
    private Canvas canvas;

    /**
     * Main context loop for drawing
     * @param event - this specifies that the automata has changed and needs to be redrawn
     */

    @Subscribe
    public void handleAutomataChange(AutomataChangeEvent event) {
        logger.trace("Automata changed, redrawing");
        NodeList nodes = event.getNodeList();
        IInstantaneousDescription iInstantaneousDescription = event.getCurrInstanteneousDescription();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        fullTextDisplays.clear();
        ViewUtilities.reset(canvas);

        drawingStrategy.setLastSelectedTransition(iInstantaneousDescription.getLastSelectedTransition());

        for(INode node : nodes)
        {
            if(node.getIsStartNode())
                drawingStrategy.drawStartNode(node, iInstantaneousDescription);
            else
                drawingStrategy.drawNode(node, iInstantaneousDescription);
        }

        HashMap<ITransition, NodeConnections> arrowMap = arrowMapBuilder.build(nodes.getAllTransitions());

        for(NodeConnections nodeConnections : arrowMap.values())
                drawingStrategy.drawRelations(nodeConnections, fullTextDisplays);

        Integer maxX = getMaxX(nodes);
        Integer maxY = getMaxY(nodes);

        if(zoomValue*maxX.doubleValue() + zoomValue*BUFFER > INITIALWIDTH)
            canvas.setWidth(zoomValue*maxX.doubleValue() + zoomValue*BUFFER);
        else
            canvas.setWidth(INITIALWIDTH);

        if(zoomValue*maxY.doubleValue() + zoomValue*BUFFER > INITIALHEIGHT)
            canvas.setHeight(zoomValue*maxY.doubleValue() + zoomValue*BUFFER);
        else
            canvas.setHeight(INITIALHEIGHT);

        // allows redrawing of screen without recourse to messaging the model
        currentAutomataEvent = event;
    }

    /**
     *
     * @param nodes  - nodelist
     * @return The maximum x co-ordinate of the nodes so that the vector drawing canvas can be resized
     */

    private Integer getMaxX(NodeList nodes) {
        Integer maxX = 0;

        for(IPushDownNode node : nodes)
        {
            maxX = maxX >= node.getX() ? maxX : node.getX();
        }

        logger.trace("Drawing pane max width is: " + maxX);

        return maxX;
    }

    /**
     *
     * @param nodes  node list
     * @return The maximum y co-ordinate of the nodes so that the vector drawing canvas can be resized
     */

    private Integer getMaxY(NodeList nodes) {
            Integer maxY = 0;

            for(IPushDownNode node : nodes)
            {
                maxY = maxY >= node.getY() ? maxY : node.getY();
            }

        logger.trace("Drawing pane max depth is: " + maxY);

        return maxY;
    }

    /**
     *
     * @param event - automata change request
     * @param blink - does this automata change specify a blink motion, as used by some transition
     */

    public void handleAutomataChange(AutomataChangeEvent event, Blink blink) {
        if(blink == Blink.BLINK)
        {
            logger.trace("Blink action");
            event.getCurrInstanteneousDescription().resetLastSelectedTransition();
        }
        handleAutomataChange(event);
    }

    /**
     * rescales vector, used by zoom buttons. Zoomvalue persists the general size
     * @param zoomFactor
     */
    public void scaleAutomata(double zoomFactor)
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.scale(zoomFactor, zoomFactor);
        zoomValue *= zoomFactor;
        handleAutomataChange(currentAutomataEvent);
    }

    /**
     * Sends zoom in and out requests to specified event using static fields
     * @param zoomedEvent - a zoom request called by other controllers
     */

    @Subscribe
    public void handleZoomedEvent(ZoomedEvent zoomedEvent)
    {
        if(zoomedEvent == ZoomedEvent.ZOOM_IN)
            scaleAutomata(ZOOM_IN_FACTOR);
        else if(zoomedEvent == ZoomedEvent.ZOOM_OUT)
        {
            scaleAutomata(ZOOM_OUT_FACTOR);
        }
    }

    @Subscribe
    public void handleResetAutomataEvent(ResetAutomataEvent resetAutomata)
    {
        handleAutomataChange(currentAutomataEvent, resetAutomata.blink);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        INITIALWIDTH = canvas.getWidth();
        INITIALHEIGHT = canvas.getHeight();

        scrollPaneRun.setStyle("-fx-background-color:white;");
        ViewUtilities.setToInitialProportions(scrollPaneRun, INITIALWIDTH, INITIALHEIGHT);
        ViewUtilities.setUpCanvas(scrollPaneRun, canvas);

        fullTextDisplays = new ArrayList<FullDisplayBox>();

        /* Determines if the current mouse location is inside the intersection of one of the bounding boxes
           and draws it if necessary using the strategy pattern
         */

        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                GraphicsContext gc = canvas.getGraphicsContext2D();

                if(fullDisplayBoxSet)
                    handleAutomataChange(currentAutomataEvent);

                if(!fullTextDisplays.isEmpty())
                    for(FullDisplayBox hiddenStateBox : fullTextDisplays)
                    {
                        if(overlayBoxStrategy.validBox(hiddenStateBox, mouseEvent, zoomValue))
                        {
                          fullDisplayBoxSet = true;
                            overlayBoxStrategy.drawOverlayBox(gc, hiddenStateBox);
                        }
                    }
            }

        });

        Injector injector = Guice.createInjector(new EventBusServerModule(this), new DrawingTemplateModule());
        injector.injectMembers(this);
        drawingStrategy = drawingFactory.create(canvas.getGraphicsContext2D());
    }

}

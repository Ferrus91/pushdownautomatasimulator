package Controller;

import Controller.ControllerLogic.TransitionMousePressContext;
import Controller.PopUpControllers.NodeEditPopUpController;
import Controller.PopUpControllers.TransitionEditPopUpController;
import Controller.RunnerActionStrategies.ICircleStrategies;
import DependencyInjection.CircleStrategiesModule;
import DependencyInjection.EditTemplateModule;
import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import DesignStrategies.IDesignStrategy;
import DesignStrategies.LineStrategy;
import DesignStrategies.NodeStrategy;
import DesignStrategies.SelectorStrategy;
import DrawingTemplate.IEditFactory;
import DrawingTemplate.IEditTemplate;
import DrawingTemplate.RelevantNodeFactory;
import EventObjects.*;
import Model.AutomataType;
import Model.DrawingModel.DrawingNode;
import Model.DrawingModel.EditTransitions;
import Model.DrawingModel.IDrawingLists;
import Model.DrawingModel.Relation;
import Model.ILink;
import Model.INode;
import PDAGraphicElements.PDANode;
import Utils.DialogueUtils;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 28/06/13
 * Time: 22:27
 * To change this template use File | Settings | File Templates.
 */
public class PDADesignerController implements Initializable {
    static Logger logger = Logger.getLogger(PDADesignerController.class);

    // Three design strategies that can be called.
    static private IDesignStrategy NODESTRATEGY = new NodeStrategy();
    static private IDesignStrategy LINESTRATEGY = new LineStrategy();
    static private IDesignStrategy SELECTORSTRATEGY = new SelectorStrategy();
    static private Double STARTWIDTH;
    static private Double STARTHEIGHT;

    private IEventBusServer eventBus;
    private IEditFactory editFactory;
    private IEditTemplate editTemplate;
    private IDesignStrategy designStrategy;

    private RelevantNodeFactory relevantNodeFactory = new RelevantNodeFactory();

    @Inject
    private ICircleStrategies circleStrategies;

    @Getter
    @Setter
    private Integer canvasSize = 1;

    @Getter
    @Setter
    private Boolean nodeSelected = false;

    @Getter
    @Setter
    private Boolean transitionSelected = false;

    @FXML
    private Button expandButton;

    @FXML
    private Button clearButton;

    @FXML
    private ScrollPane transitionScroll;

    @FXML
    private ScrollPane nodeScroll;

    @FXML
    @Getter
    private TitledPane nodePane;

    @FXML
    private ToggleGroup successRadio;

    @FXML
    private RadioButton stackRadio;

    @FXML
    private RadioButton finalRadio;

    @FXML
    private AnchorPane nodeDetails;

    @FXML
    private NodeEditPopUpController nodeDetailsController;

    @FXML
    private AnchorPane transitionDetails;

    @FXML
    TransitionEditPopUpController transitionDetailsController;

    @FXML
    @Getter
    private TitledPane transitionPane;

    @FXML
    private Accordion designerAccordion;

    @FXML
    private AnchorPane content;

    @FXML
    private Pane drawingBoard;

    @FXML
    private VBox designerOption;

    @FXML
    private ToggleButton node;

    @FXML
    private ToggleButton line;

    @FXML
    private ToggleButton selector;

    @FXML
    private ToggleGroup  designerToggle;

    @Inject
    public void setEventBus(IEventBusServer eventBus)
    {
        this.eventBus = eventBus;
    }

    @Inject
    public void setEditFactory(IEditFactory editFactory)
    {
        this.editFactory = editFactory;
    }

    /**
     * this method is called when an event ask for the transition accordion details to load.
     * @param id
     * @param label
     * @param editTransitions
     */
    public void connectionAccordionExpand(String id, String label, ArrayList<EditTransitions> editTransitions)
    {
        logger.trace("Expanding connection accordion for: " + id);
        eventBus.post(new RequestPaneRedrawEvent());
        transitionPane.setExpanded(true);
        transitionSelected = true;
        nodeSelected = false;
        transitionScroll.setVvalue(0);
        transitionDetailsController.reset();
        transitionDetailsController.setLabelString(label);
        transitionDetailsController.setId(new SimpleStringProperty(id));
        transitionDetailsController.resetData();
        if(editTransitions != null)
            transitionDetailsController.addAll(editTransitions.toArray(new EditTransitions[editTransitions.size()]));
        transitionSelected = true;
        transitionDetailsController.setPdaDesignerController(this);
    }

    /**
     * this method is called when an event causes the asks for the node accordion details to load.
     * @param id
     * @param label
     * @param isStartNode
     * @param isFinalNode
     * @param isNodeDown
     * @param editTransitions
     */
    public void nodeAccordionExpand(String id, String label, Boolean isStartNode, Boolean isFinalNode, Boolean isNodeDown, ArrayList<EditTransitions> editTransitions)
    {
        logger.trace("Expanding node accordion for: " + id);
        nodePane.setExpanded(true);
        nodeScroll.setVvalue(0);
        nodeSelected = true;
        transitionSelected = false;
        nodeDetailsController.reset();
        nodeDetailsController.getLabelTextBox().setText(label);
        nodeDetailsController.getStartNodeCheckBox().setSelected(isStartNode);
        nodeDetailsController.getFinalNodeCheckBox().setSelected(isFinalNode);
        nodeDetailsController.getLoopDownCheckBox().setSelected(isNodeDown);
        if(editTransitions != null)
            nodeDetailsController.addAll(editTransitions.toArray(new EditTransitions[editTransitions.size()]));
        nodeDetailsController.setPdaDesignerController(this);
        nodeDetailsController.setLabelString(label);
        nodeDetailsController.resetData();
        nodeDetailsController.setId(new SimpleStringProperty(id));
    }

    private void setPanes() {
        nodePane.setExpanded(nodeSelected);
        transitionPane.setExpanded(transitionSelected);
    }

    /**
     * Opens a node pane with the relevant details
     * @param openNodeAccordionEvent
     */
    @Subscribe
    public void handleOpenNodeAccordionEvent(OpenNodeAccordionEvent openNodeAccordionEvent)
    {
        DrawingNode editNode = openNodeAccordionEvent.getNode();
        nodeAccordionExpand(editNode.getId(), editNode.getLabel(), editNode.getIsStartNode(),
                editNode.getIsFinalNode(), editNode.getIsNodeDown(), openNodeAccordionEvent.getEditTransitions());
    }

    /**
     * Opens a relation pane with the relevant details
     * @param openRelationAccordionEvent
     */
    @Subscribe
    public void handleOpenRelationAccordionEvent(OpenRelationAccordionEvent openRelationAccordionEvent)
    {
        Relation relation = openRelationAccordionEvent.getRelation();
        connectionAccordionExpand(relation.getId(), relation.getFromNode().getLabel() + " to "
                + relation.getToNode().get(0).getLabel(), openRelationAccordionEvent.getEditTransitions());
    }

    /**
     * gets the node details from the model and opens pane
     * @param requestNodeDetails
     */
    @Subscribe
    public void handleRequestNodeDetailsEvent(RequestNodeDetailsEvent requestNodeDetails)
    {
        logger.trace("Requesting node details for: " + requestNodeDetails.getEditID());
        nodeAccordionExpand(requestNodeDetails.getEditID(), requestNodeDetails.getLabel(),
                false, false, false, null);
    }


    /**
     * gets the relation details from the model and opens pane
     * @param requestConnectionDetails
     */
    @Subscribe
    public void handleRequestConnectionDetailsEvent(RequestConnectionDetailsEvent requestConnectionDetails)
    {
        logger.trace("Requesting connection details for: " + requestConnectionDetails.getConnectionId());
        connectionAccordionExpand(requestConnectionDetails.getConnectionId(),
                requestConnectionDetails.getToLabel() + " to " + requestConnectionDetails.getFromLabel(), null);
    }

    /**
     * closes the all panes and shuts them tight
     * @param closePanesEvent
     */
    @Subscribe
    public void handleClosePanesEvent(ClosePanesEvent closePanesEvent)
    {
        logger.trace("Closing all panes");
        nodeSelected = false;
        transitionSelected = false;
        designerAccordion.setExpandedPane(null);
    }
    /**
     * Finds the nodes affected by the connection creation event (if any) and passes IDs to the model
     * @param connectionCreationEvent passes the button press parameters in
     */
    @Subscribe
    public void handleConnectionCreationEvent(ConnectionCreationEvent connectionCreationEvent)
    {
        String fromNodeId  = "";
        String toNodeId = "";
        TransitionMousePressContext transitionMousePressContext
                = connectionCreationEvent.getTransitionMousePressContext();

        for(Node node : drawingBoard.getChildren())
        {
            if(node instanceof PDANode)
            {
                PDANode pdaNode = (PDANode)node;
                if(pdaNode.pointIntersects(transitionMousePressContext.getStartX(),
                        transitionMousePressContext.getStartY()))
                    fromNodeId = pdaNode.getUuid();
                else if(pdaNode.pointIntersects(transitionMousePressContext.getEndX(),
                        transitionMousePressContext.getEndY()))
                    toNodeId = pdaNode.getUuid();
                logger.trace("Self connection created for: " + pdaNode.getUuid());
            }
        }
        if(!fromNodeId.equals("") && !toNodeId.equals(""))
        {
            logger.trace("Connection created between" + fromNodeId + " and " + toNodeId);
            eventBus.post(new ConnectionAddEvent(fromNodeId, toNodeId));
        }
    }

    /**
     * This method is designed to draw the items on the screen
     *
     * @param currentDrawEvent - the current pane on which the drawing objects are placed
     */

    @Subscribe
    public void handleCurrentDrawEvent(CurrentDrawEvent currentDrawEvent)
    {
        logger.trace("Redrawing drawing model");
        drawingBoard.getChildren().clear();

        IDrawingLists IDrawingLists = currentDrawEvent.getIDrawingLists();

        for(INode node : IDrawingLists.getNodes().values())
        {
            if(node.getIsStartNode())
                editTemplate.drawStartNode(node, IDrawingLists);
            else
                editTemplate.drawNode(node, IDrawingLists);
        }

        for(ILink link : IDrawingLists.getRelations().values())
        {
            ArrayList<PDANode> relevantNodes = relevantNodeFactory.build(link, drawingBoard);
            if(relevantNodes != null)
                editTemplate.drawRelations((Relation)link, relevantNodes);
        }
    }

    /**
     * closes panes and redraws pane when error eventhappens
     */
    private void toggleAction()
    {
        designerAccordion.setExpandedPane(null);
        nodeSelected = false;
        transitionSelected = false;
        if(transitionDetailsController.getData().size() == 0)
        {
            eventBus.post(new DeleteByIdEvent(transitionDetailsController.getId().getValue()));
        }
        eventBus.post(new DeselectAllEvent());
        eventBus.post(new RequestPaneRedrawEvent());
    }

    /**
     * resizes the canvas in response to a user action
     */
    private void resizeCanvas() {
        drawingBoard.setPrefWidth(canvasSize * STARTWIDTH);
        drawingBoard.setPrefHeight(canvasSize * STARTHEIGHT);
        logger.trace("Doubling canvas size");
    }

    private void fireResize(){
        eventBus.post(new CanvasResizeEvent(canvasSize));
        resizeCanvas();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        STARTWIDTH = drawingBoard.getPrefWidth();
        STARTHEIGHT = drawingBoard.getPrefHeight();

        Injector injector = Guice.createInjector(new EventBusServerModule(this),
                new EditTemplateModule(), new CircleStrategiesModule());
        injector.injectMembers(this);
        editTemplate = editFactory.create(drawingBoard);
        circleStrategies.setPane(drawingBoard);

        // Default graphics. Doesn't work in FXML under present version of JavaFX2

        node.setGraphic(new ImageView(new Image(
                PDARunnerController.class.getClassLoader().getResourceAsStream("View/Node.png"))));
        line.setGraphic(new ImageView(new Image(
                PDARunnerController.class.getClassLoader().getResourceAsStream("View/Transition.png"))));
        selector.setGraphic(new ImageView(new Image(
                PDARunnerController.class.getClassLoader().getResourceAsStream("View/cursor.png"))));

        // Code for radiobutton-esque toggle buttons from: http://www.coderanch.com/t/602633/JavaFX/java/ToggleButton-RadioButton

        designerToggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            public void changed(ObservableValue<? extends Toggle> observable,
                                final Toggle oldValue, final Toggle newValue) {
                if ((newValue == null)) {
                    Platform.runLater(new Runnable() {

                        public void run() {
                            designerToggle.selectToggle(oldValue);
                        }
                    });
                }
            }
        });

        /* The system holds a reference to at most one design strategy at any one time. Which one of these are
           selected depends on the toggle buttons. Dynamic dispatch then handles the appropriate behaviour
         */

        drawingBoard.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {

            @Override
            public void handle(MouseEvent mouseEvent) {
                logger.debug("Mouse click event in drawing pane");
                drawingBoard.requestFocus();
                if(designStrategy != null)
                {
                    designStrategy.fireClickEvent(mouseEvent.getX(),mouseEvent.getY(), circleStrategies);
                }
            }
        });

        drawingBoard.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent) {
                logger.debug("Mouse press event in drawing pane");
                drawingBoard.requestFocus();
                if(designStrategy != null)
                {
                    designStrategy.firePressEvent(mouseEvent.getX(), mouseEvent.getY(), circleStrategies);
                }
            }
        });

        drawingBoard.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent) {
                logger.debug("Mouse drag event in drawing pane");
                drawingBoard.requestFocus();
                if(designStrategy != null)
                {
                    designStrategy.fireDragEvent(mouseEvent.getX(), mouseEvent.getY(),
                            circleStrategies, drawingBoard);
                }
            }
        });

        drawingBoard.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                logger.trace("Mouse release event in drawing pane");
                if (designStrategy != null) {
                    designStrategy.fireReleaseEvent(mouseEvent, circleStrategies);
                }
            }
        });

        node.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                logger.debug("Node strategy selected");
                designStrategy = NODESTRATEGY;
                toggleAction();
            }
        });

        line.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                logger.debug("Line strategy selected");
                designStrategy = LINESTRATEGY;
                toggleAction();
            }
        });

        selector.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                logger.debug("Selector strategy selected");
                designStrategy = SELECTORSTRATEGY;
                toggleAction();
            }
        });

        content.setStyle("-fx-background-color:black;");
        drawingBoard.setStyle("-fx-background-color:white;");

        transitionPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setPanes();
            }});

        nodePane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setPanes();
            }});


        // allows delete key to trigger delete
        drawingBoard.addEventHandler(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.DELETE)
                {
                    eventBus.post(new DeleteIfSelectedEvent());
                }
            }
        });

        // clears canvas
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                logger.trace("Redraw action");
                if(DialogueUtils.choiceBox("Are you sure you want to delete all objects?"))
                {
                    eventBus.post(new ClearDrawingScreenEvent());
                    canvasSize = 1;
                    fireResize();
                }
            }
        });

        // expands canvas
        expandButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                canvasSize *= 2;
                logger.trace("New canvas size = " + canvasSize);
                fireResize();
            }
        });

        // changes automaton type
        successRadio.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle oldRadio, Toggle newRadio) {
                RadioButton selectedRadio = (RadioButton)newRadio;
                logger.trace("Automaton type is Lmachine? " + finalRadio);
                if(selectedRadio == finalRadio)
                    eventBus.post(new ChangeAutomataType(AutomataType.LMACHINE));
                else
                    eventBus.post(new ChangeAutomataType(AutomataType.MMACHINE));
            }
        });

        //shuts panes
        designerAccordion.setExpandedPane(null);
    }
}
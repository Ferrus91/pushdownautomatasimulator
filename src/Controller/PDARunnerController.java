package Controller;

import Controller.RunnerActionStrategies.IActionStrategy;
import DependencyInjection.ActionStrategyModule;
import DependencyInjection.AnimationModule;
import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import DependencyInjection.Fastforward;
import DependencyInjection.Forward;
import EventObjects.*;
import GraphicsElements.RightClickableCell;
import Utils.DialogueUtils;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.animation.Timeline;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 28/06/13
 * Time: 22:29
 * To change this template use File | Settings | File Templates.
 */
public class PDARunnerController implements Initializable {
    static Logger logger = Logger.getLogger(PDARunnerController.class);
    static private final String LINEBREAK = System.lineSeparator();

    private ObservableList<String> inputItems = FXCollections.observableArrayList();
    private IActionStrategy iActionStrategy;

    @FXML
    public AnchorPane content;

    private Property<Boolean> pdaFinished = new SimpleBooleanProperty();
    private Timeline timelineFastForward = new Timeline();
    private Timeline timelineForward = new Timeline();
    private IEventBusServer eventBus;

    @FXML
    private Button zoomIn;

    @FXML
    private Button zoomOut;

    @FXML
    private Slider speedSlider;

    @FXML
    private Button pause;

    @FXML
    private Button fastForward;

    @FXML
    private Button forward;

    @FXML
    private Button stop;

    @FXML
    ListView<String> inputStrings;

    @FXML
    TextArea textBox;

    @FXML
    private Button addQueue;

    @FXML
    private Button addBatch;

    @FXML
    private TextField userInputBox;

    @Inject
    public void setEventBus(IEventBusServer eventBus)
    {
        this.eventBus = eventBus;
    }

    @Inject
    public void setActionStrategy(IActionStrategy iActionStrategy)
    {
        this.iActionStrategy = iActionStrategy;
    }

    @Inject
    public void setTimelineFastForward(@Fastforward Timeline timelineFastForward)
    {
        this.timelineFastForward = timelineFastForward;
    }

    @Inject
    public void setTimelineForward(@Forward Timeline timelineForward)
    {
        this.timelineForward = timelineForward;
    }

    @Subscribe
    public void handleDeactivateAnimationButtonsEvent(DeactivateAnimationButtonsEvent deactivateAnimationButtonsEvent)
    {
        logger.trace("Disable animation buttons");
        addQueue.setDisable(true);
        addBatch.setDisable(true);
    }

    @Subscribe
    public void handleActivateAnimationButtonsEvent(ActivateAnimationButtonsEvent activateAnimationButtonsEvent)
    {
        logger.trace("Activate animation buttons");
        addQueue.setDisable(false);
        addBatch.setDisable(false);
    }

    /**
     * This gives context appropriate information in the View to display to the user when a PDA has finished
     * computation
     * @param pdaCompleteEvent - event passed by model
     */

    @Subscribe
    public void handlePDACompleteEvent(PDACompleteEvent pdaCompleteEvent)
    {
        logger.trace("Pda complete event handled");
        if(pdaCompleteEvent.getIsSuccessful())
            textBox.appendText("The string " + pdaCompleteEvent.getProcessedInput()
                    + " has been successfully processed by this automata" + LINEBREAK);
        else
            textBox.appendText("The string " + pdaCompleteEvent.getProcessedInput()
                    + " has not been successfully processed by this automata" + LINEBREAK);
        this.pdaFinished.setValue(true);
    }

    /**
     * Shifts the exceptions away from the main program flow and reads their messages into the View
     * @param transitionErrorEvent - event passed by model which contains information on the error
     */

    @Subscribe
    public void handleTransitionErrorEvent(TransitionErrorEvent transitionErrorEvent)
    {
        logger.error("Error message of type:" + transitionErrorEvent.getErrorMessage());
        textBox.appendText(transitionErrorEvent.getErrorMessage() + LINEBREAK);
        this.pdaFinished.setValue(true);
    }

    /**
     * The observable boolean pdaFinished is attached to this change listener. If this value is true
     * an animation is in process. This can be changed by the pause button, which changes the boolean
     * and thus pauses the animation
     */

    private void pdaFinishedInitialise() {
        logger.trace("PDA finished");
        pdaFinished.setValue(true);
        pdaFinished.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                if(aBoolean2)
                    timelineFastForward.stop();
            }
        });
    }

    @Subscribe
    public void handleStackSizeWarningEvent(StackSizeWarningEvent stackSizeWarningEvent)
    {
        logger.warn("Stack getting large handled");
        timelineFastForward.pause();
        textBox.appendText("Number of automata jumps has reached " + stackSizeWarningEvent.getSize() + " this may" +
                " cause memory issues" + LINEBREAK);
    }

    @Subscribe
    public void handleNonDeterministicJumpEvent(NonDeterministicJumpEvent nonDeterministicJumpEvent)
    {
        logger.trace("NonDeterministic jump handled");
        textBox.appendText("Automata has jumped to node " + nonDeterministicJumpEvent.getLabel()
                + " with stack " + nonDeterministicJumpEvent.getStack() + " and processed string "
                + nonDeterministicJumpEvent.getProcessedInput()
                + nonDeterministicJumpEvent.getMessage() + LINEBREAK);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pdaFinishedInitialise();

        inputStrings.setItems(inputItems);

        textBox.setEditable(false);

        // Custom cells in View table allow entries to be deleted from the stack

        inputStrings.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> stringListView) {
                return new RightClickableCell(inputItems);
            }
        });

        // Sets animation off

        fastForward.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                iActionStrategy.fastForward(pdaFinished, inputItems, textBox, timelineFastForward, eventBus);
            }
        });

        // Jumps one step

        forward.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                iActionStrategy.forward(pdaFinished, inputItems, textBox, timelineForward, eventBus);
            }
        });

        //resets automaton

        stop.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent) {
                pdaFinished.setValue(true);
                eventBus.post(new ResetAnimationAutomataEvent());
            }
        });

        // Adds item to process queue

        addQueue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                iActionStrategy.addToQueue(userInputBox, inputItems);
            }
        });

        // Extracts CSV information

        addBatch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                iActionStrategy.extractFile(inputItems);
            }
        });

        userInputBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                iActionStrategy.addToQueue(userInputBox, inputItems);
            }
        });

        zoomIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                logger.trace("Zooming in");
                eventBus.post(ZoomedEvent.ZOOM_IN);
            }
        });

        zoomOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                logger.trace("Zooming out");
                eventBus.post(ZoomedEvent.ZOOM_OUT);
            }
        });

        pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                timelineFastForward.pause();
            }
        });

        content.setStyle("-fx-background-color:black;");

        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                logger.trace("New speedup factor: " + number2);
                timelineFastForward.setRate((Double)number2);
            }
        });

        speedSlider.setValue(1.0);

        Injector injector = Guice.createInjector(new EventBusServerModule(this), new AnimationModule(), new ActionStrategyModule());
        injector.injectMembers(this);
    }
}

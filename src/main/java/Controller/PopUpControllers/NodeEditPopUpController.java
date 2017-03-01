package Controller.PopUpControllers;

import Controller.ControllerLogic.TransitionTableBuilder;
import Controller.PDADesignerController;
import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.AddTransitionStepEvent;
import EventObjects.EditConnectionEvent;
import EventObjects.EditNodeEvent;
import EventObjects.SelfConnectionAddEvent;
import Model.DrawingModel.EditTransitions;
import Utils.DialogueUtils;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 15/08/13
 * Time: 11:56
 * To change this template use File | Settings | File Templates.
 */
public class NodeEditPopUpController implements Initializable {
    static Logger logger = Logger.getLogger(NodeEditPopUpController.class);

    @Getter
    @Setter
    ObservableStringValue id = new SimpleStringProperty();

    @FXML
    @Getter
    private TextField labelTextBox;

    @FXML
    private TableColumn transCharCol;

    @FXML
    private TableColumn pushStringCol;

    @FXML
    private TableColumn popCharCol;


    @FXML
    private  AnchorPane anchorPane;

    @FXML
    private Label label;

    @FXML
    private Button applyButton;

    @FXML
    private Button addTransitionButton;

    @FXML
    private Button deleteTransitionButton;

    @FXML
    private TableView transitionTable;

    @FXML
    @Getter
    private CheckBox startNodeCheckBox;

    @FXML
    @Getter
    private CheckBox finalNodeCheckBox;

    @FXML
    @Getter
    private CheckBox loopDownCheckBox;
    private IEventBusServer eventBus;

    @FXML
    private Label startLabel;

    @FXML
    private Label finalLabel;

    @FXML
    private Label loopLabel;

    @Getter
    @Setter
    private PDADesignerController pdaDesignerController;

    @Delegate
    private ObservableList<EditTransitions> data = FXCollections.observableArrayList();

    @Inject
    public void setEventBus(IEventBusServer eventBus)
    {
        this.eventBus = eventBus;
    }


    public void setLabelString(String id)
    {
        label.setText(id);
    }

    public void resetData()
    {
        transitionTable.setItems(data);
    }

    @Subscribe
    public void handleAddTransitionStepEvent(AddTransitionStepEvent addTransitionStep)
    {
        logger.trace(addTransitionStep.getId() + "is being added");
        if(addTransitionStep.getId().equals(id.getValue()))
        {
            Character transitionChar = addTransitionStep.getTransitionChar();

            if(transitionChar == '\u03B5')
            {
                DialogueUtils.warningMessage("Can't have empty transition char for same node");
                return;
            }

            for(EditTransitions editTransition : data)
            {
                if(editTransition.getTransitionChar() == transitionChar)
                {
                    DialogueUtils.warningMessage("Can't have two transitions with the same name");
                    return;
                }
            }

            data.add(new EditTransitions(transitionChar, addTransitionStep.getPushString(), addTransitionStep.getPopChar()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Injector injector = Guice.createInjector(new EventBusServerModule(this));
        injector.injectMembers(this);

        transitionTable.setPlaceholder(new Pane());
        transitionTable.setItems(data);

        // edits an existing node and updates the model
        applyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                eventBus.post(new EditNodeEvent(id.getValue(),
                        labelTextBox.getText() != null ? labelTextBox.getText() : "",
                        startNodeCheckBox.isSelected(),
                        finalNodeCheckBox.isSelected(),
                        loopDownCheckBox.isSelected()));
                if(data.size() > 0)
                    eventBus.post(new SelfConnectionAddEvent(id.getValue(), data));
                eventBus.post(new EditConnectionEvent(id.getValue() + "-" + id.getValue(), data));
                pdaDesignerController.getNodePane().setExpanded(false);
                pdaDesignerController.setNodeSelected(false);
            }
        });

        // adds selected transition, loads popup
        addTransitionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/DesignerPopUps/transitiondetailspopup.fxml"));
                try {
                    Parent newRoot = (Parent)loader.load();
                    Stage newStage = new Stage();
                    TransitionDetailsController controller = (TransitionDetailsController)loader.getController();
                    controller.setStage(newStage);
                    controller.setId(id.getValue());
                    newStage.setTitle("Add Transition");
                    newStage.setScene(new Scene(newRoot, 600, 255));
                    newStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // deletes selected transition
        deleteTransitionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(data.size() > 0)
                {
                // inspired by   http://stackoverflow.com/questions/13393301/read-selection-from-tableview-in-javafx-2-0
                TableView.TableViewSelectionModel selectionModel = transitionTable.getSelectionModel();
                ObservableList selectedCells = selectionModel.getSelectedCells();
                TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                int row = tablePosition.getRow(); // yields the row that the currently selected cell is in
                logger.trace("Row " + row + " is being deleted");
                data.remove(row);
                transitionTable.setItems(data);
                }
            }
        });

        transitionTable = TransitionTableBuilder.build(transitionTable,transCharCol,pushStringCol,popCharCol,data);

        label.setTextFill(Color.WHITE);
        startLabel.setTextFill(Color.WHITE);
        finalLabel.setTextFill(Color.WHITE);
        loopLabel.setTextFill(Color.WHITE);
    }

    public void reset() {
        logger.trace("Resetting pop up controller");
        label.setText("");
        data.clear();
        transitionTable.setItems(data);
        startNodeCheckBox.setSelected(false);
        finalNodeCheckBox.setSelected(false);
        loopDownCheckBox.setSelected(false);
        labelTextBox.setText("");
    }
}

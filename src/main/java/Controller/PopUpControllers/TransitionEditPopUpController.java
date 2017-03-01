package Controller.PopUpControllers;

import Controller.ControllerLogic.TransitionTableBuilder;
import Controller.PDADesignerController;
import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.AddTransitionStepEvent;
import EventObjects.EditConnectionEvent;
import EventObjects.RequestPaneRedrawEvent;
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
import javafx.scene.control.cell.PropertyValueFactory;
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
 * Time: 11:57
 * To change this template use File | Settings | File Templates.
 */
public class TransitionEditPopUpController implements Initializable {
    static Logger logger = Logger.getLogger(NodeEditPopUpController.class);

    @Getter
    @Setter
    ObservableStringValue id  = new SimpleStringProperty();

    @FXML
    private TableColumn transCharCol;

    @FXML
    private Label idLabel;

    @FXML
    private TableColumn pushStringCol;

    @FXML
    private TableColumn popCharCol;

    @FXML
    private  AnchorPane anchorPane;

    @FXML
    private TableView transitionTable;

    @FXML
    private Button applyButton;

    @FXML
    private Button deleteTransitionButton;

    @FXML
    private Button addTransitionButton;

    @Getter
    @Setter
    private Stage childStage;

    @Delegate
    @Getter
    private ObservableList<EditTransitions> data = FXCollections.observableArrayList();

    private IEventBusServer eventBus;
    private String labelString;
    @Getter
    private PDADesignerController pdaDesignerController;

    @Inject
    public void setEventBus(IEventBusServer eventBus)
    {
        this.eventBus = eventBus;
    }

    @Subscribe
    public void handleAddTransitionStepEvent(AddTransitionStepEvent addTransitionStep)
    {
        logger.trace("Creating transition screen for " + addTransitionStep.getId());

        if(addTransitionStep.getId().equals(id.getValue()))
        {
            Character transitionChar = addTransitionStep.getTransitionChar();

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
        logger.trace("Loading transition eidt screen");

        Injector injector = Guice.createInjector(new EventBusServerModule(this));
        injector.injectMembers(this);
        eventBus.register(this);

        eventBus.post(new RequestPaneRedrawEvent());

        transCharCol.setCellValueFactory(new PropertyValueFactory<EditTransitions, Character>("transitionChar"));
        pushStringCol.setCellValueFactory(new PropertyValueFactory<EditTransitions, Character>("pushString"));
        popCharCol.setCellValueFactory(new PropertyValueFactory<EditTransitions, Character>("popChar"));

        transitionTable.setPlaceholder(new Pane());

        applyButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {
                eventBus.post(new EditConnectionEvent(id.getValue(), data));
                pdaDesignerController.getTransitionPane().setExpanded(false);
                pdaDesignerController.setTransitionSelected(false);
            }
        });

        //Loads pop up and adds transition
        addTransitionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/DesignerPopUps/transitiondetailspopup.fxml"));
                try {
                    logger.trace("Loading transition popup");
                    Parent newRoot = (Parent)loader.load();
                    Stage newStage = new Stage();
                    TransitionDetailsController controller = (TransitionDetailsController)loader.getController();
                    childStage = newStage;
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

                    logger.trace("Deleting row " + row);
                    data.remove(row);
                    transitionTable.setItems(data);
                }
            }

        });
        idLabel.setTextFill(Color.WHITE);

        transitionTable = TransitionTableBuilder.build(transitionTable, transCharCol, pushStringCol, popCharCol, data);

        transitionTable.setItems(data);
            }

    public void setLabelString(String labelString) {
        idLabel.setText(labelString);
    }

    public String getLabelString() {
        return labelString;
    }

    public void close() {
        eventBus.post(new EditConnectionEvent(id.getValue(), data));
    }

    public void reset() {
        data.clear();
        transitionTable.setItems(data);
        labelString = "";
    }

    public void setPdaDesignerController(PDADesignerController pdaDesignerController) {
        this.pdaDesignerController = pdaDesignerController;
    }

    public void resetData() {
        transitionTable.setItems(data);
    }
}

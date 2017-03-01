package Controller.PopUpControllers;

import Database.DatabaseDirectory;
import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.NewAutomataEvent;
import Utils.DialogueUtils;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 25/08/13
 * Time: 21:15
 * To change this template use File | Settings | File Templates.
 */
public class AddAutomataDialogController implements Initializable {
    static Logger logger = Logger.getLogger(AddAutomataDialogController.class);

    @FXML
    private AnchorPane content;
    @FXML
    private Button addButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField textInput;
    @FXML
    private ComboBox<DatabaseDirectory> directoryChoiceBox;

    @Getter
    @Setter
    Boolean isCloseEvent;

    @Getter
    @Setter
    private String xmlString;

    private ObservableList<DatabaseDirectory> choiceBoxList = FXCollections.observableArrayList();

    public void setList(ArrayList<DatabaseDirectory> directories)
    {
        logger.trace("Creating combo box");
        choiceBoxList.addAll(directories);
        directoryChoiceBox.setItems(choiceBoxList);
        directoryChoiceBox.getSelectionModel().selectFirst();
    }

    @Inject
    IEventBusServer eventBus;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.trace("Loading add automata view");

        Injector injector = Guice.createInjector(new EventBusServerModule(this));
        injector.injectMembers(this);

        // sets a custom directory cell
        directoryChoiceBox.setCellFactory(new Callback<ListView<DatabaseDirectory>, ListCell<DatabaseDirectory>>() {
            @Override
            public ListCell<DatabaseDirectory> call(ListView<DatabaseDirectory> databaseDirectoryListView) {
                return new DirectoryListCell();
            }
        });

        // has logic to prevent null named automaton
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(textInput.getCharacters() == null
                        || textInput.getCharacters().toString().equals(""))
                {
                    logger.error("Empty textbox entered");
                    DialogueUtils.warningMessage("Can't add empty automata");
                }
                else
                {
                    eventBus.post(new NewAutomataEvent(textInput.getCharacters().toString(),
                            directoryChoiceBox.getSelectionModel().getSelectedItem(),
                            getXmlString(), isCloseEvent));
                    Stage stage = (Stage) cancelButton.getScene().getWindow();
                    stage.close();
                }
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
            }
        });
    }

    private class DirectoryListCell extends ListCell<DatabaseDirectory> {
        @Override protected void updateItem(DatabaseDirectory directory, boolean empty) {
            super.updateItem(directory, empty);
            if (directory != null) {
                setText(directory.getName());
            }
        }
    }
}

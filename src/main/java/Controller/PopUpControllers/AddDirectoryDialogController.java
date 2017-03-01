package Controller.PopUpControllers;

import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.NewDirectoryEvent;
import Utils.DialogueUtils;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/08/13
 * Time: 23:51
 * To change this template use File | Settings | File Templates.
 */
public class AddDirectoryDialogController implements Initializable {
    static Logger logger = Logger.getLogger(AddDirectoryDialogController.class);
    @FXML
    private AnchorPane content;
    @FXML
    private Button addButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField textInput;

    @Inject
    IEventBusServer eventBus;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.trace("Add directory view opened");
        Injector injector = Guice.createInjector(new EventBusServerModule(this));
        injector.injectMembers(this);

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(textInput.getCharacters() == null
                    || textInput.getCharacters().toString().equals(""))
                {
                    logger.error("Empty directory name given");
                    DialogueUtils.warningMessage("Can't add empty directory");
                }
                else
                {
                    eventBus.post(new NewDirectoryEvent(textInput.getCharacters().toString()));
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
}

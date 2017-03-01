package Controller;

import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.SaveEvent;
import Utils.DialogueUtils;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 26/06/13
 * Time: 21:40
 * Mainscreen controller
 */
public class MainController implements Initializable {
    static Logger logger = Logger.getLogger(MainController.class);
    private IEventBusServer eventBus;

    @Inject
    public void setEventBus(IEventBusServer eventBus)
    {
        this.eventBus = eventBus;
    }

    @FXML
    private MenuItem saveItem;

    @FXML
    private MenuItem closeItem;

    @FXML
    private MenuItem helpItem;

    @FXML
    private MenuItem aboutItem;

    @FXML
    private Tab runTab;

    @FXML
    private AnchorPane pdaRunner;

    @FXML
    private PDARunnerController pdaRunnerController;

    @FXML
    private Tab designTab;

    @FXML
    private AnchorPane pdaDesigner;

    @FXML
    private PDADesignerController pdaDesignerController;

    @FXML
    private Tab databaseTab;

    @FXML
    private AnchorPane pdaDatabase;

    @FXML
    private TabPane tabPane;

    @FXML
    private PDADatabaseController pdaDatabaseController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Injector injector = Guice.createInjector(new EventBusServerModule(this));
        injector.injectMembers(this);

        closeItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                logger.trace("Closing application");
                if(DialogueUtils.choiceBox("Do you want to save this work?"))
                    eventBus.post(new SaveEvent(true));
                else
                    Platform.exit();
            }
        });

        saveItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                logger.trace("Saving application");
                eventBus.post(new SaveEvent(false));
            }
        });

         aboutItem.setOnAction(new EventHandler<ActionEvent>() {
              @Override
            public void handle(ActionEvent actionEvent) {
                  logger.trace("Loading about popup");
                  FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/HelpPopUps/about.fxml"));
                try {
                    Parent newRoot = (Parent)loader.load();
                    Stage newStage = new Stage();
                    newStage.setTitle("About");
                    newStage.setScene(new Scene(newRoot, 428, 235));
                    newStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        helpItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                logger.trace("Loading help popup");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/HelpPopUps/help.fxml"));
                try {
                    Parent newRoot = (Parent)loader.load();
                    Stage newStage = new Stage();
                    newStage.setTitle("Help");
                    newStage.setScene(new Scene(newRoot,  654, 752));
                    newStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
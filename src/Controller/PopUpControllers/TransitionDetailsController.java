package Controller.PopUpControllers;

import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.AddTransitionStepEvent;
import GraphicsElements.RestrictiveTextField;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 15/08/13
 * Time: 11:57
 * To change this template use File | Settings | File Templates.
 */
public class TransitionDetailsController implements Initializable
{
static Logger logger = Logger.getLogger(TransitionDetailsController.class);

@Getter
    @Setter
    String id;

    @Getter
    @Setter
    private Stage stage;

    @Getter
    @Setter
    private Stage parentStage;

    @FXML
    private Label transCharLabel;

    @FXML
    private Label pushStringLabel;

    @FXML
    private Label popCharLabel;

    @FXML
    private  AnchorPane anchorPane;

    @FXML
    private RestrictiveTextField transCharInput;

    @FXML
    private RestrictiveTextField pushStringInput;

    @FXML
    private RestrictiveTextField popCharInput;

    @FXML
    private Button applyButton;

    private IEventBusServer eventBus;

    @Inject
    public void setEventBus(IEventBusServer eventBus)
    {
        this.eventBus = eventBus;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        logger.trace("Transition details view initialised");
        Injector injector = Guice.createInjector(new EventBusServerModule(this));
        injector.injectMembers(this);

        transCharInput.setMaxLength(1);
        popCharInput.setMaxLength(1);

        // adds entries for the text
        applyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                logger.trace("Closing transition view");
                stage.close();

                transCharInput.setText((transCharInput.getText().length() == 0 ? '\u03B5': transCharInput.getText()).toString());
                pushStringInput.setText((pushStringInput.getText().length() == 0 ? '\u03B5': pushStringInput.getText()).toString());
                popCharInput.setText((popCharInput.getText().length() == 0 ? '\u03B5': popCharInput.getText()).toString());

                eventBus.post(new AddTransitionStepEvent(transCharInput.getText().charAt(0),
                        pushStringInput.getText(),
                        popCharInput.getText().charAt(0), id));
            }
        });

        transCharLabel.setTextFill(Color.WHITE);
        pushStringLabel.setTextFill(Color.WHITE);
        popCharLabel.setTextFill(Color.WHITE);
    }
}

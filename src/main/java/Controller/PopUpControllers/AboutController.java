package Controller.PopUpControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 23/08/13
 * Time: 00:27
 * To change this template use File | Settings | File Templates.
 */
public class AboutController implements Initializable {
    static Logger logger = Logger.getLogger(AboutController.class);

    @FXML
    private Button close;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.trace("About controller loaded");
    }

    public void close(ActionEvent actionEvent) {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }
}

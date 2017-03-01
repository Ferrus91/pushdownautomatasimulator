package Controller.PopUpControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 23/08/13
 * Time: 00:27
 * To change this template use File | Settings | File Templates.
 */
public class HelpController implements Initializable {
    static Logger logger = Logger.getLogger(HelpController.class);

    @FXML
    private Button close;

    @FXML
    private WebView helpWebView;

    private WebEngine webEngine;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.trace("Help view loaded");
        webEngine = helpWebView.getEngine();
        try {
            String projectPath = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource()
                    .getLocation().getPath(), "UTF-8");
            projectPath = projectPath.substring(0, projectPath.lastIndexOf("/"));
            File file = new File(projectPath + "/HTMLHelpFiles/helpOpeningPage.html");
            logger.trace(file.getAbsolutePath() + " is being attempted");
            webEngine.load(file.toURI().toURL().toString());
        }
        catch (MalformedURLException e) {
            logger.error("No file with the name");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void close(ActionEvent actionEvent) {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }
}

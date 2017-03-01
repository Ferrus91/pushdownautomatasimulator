package Controller;

import DependencyInjection.DrawingTemplateModule;
import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.AutomataChangeEvent;
import Utils.ViewUtilities;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 17/07/13
 * Time: 19:30
 * To change this template use File | Settings | File Templates.
 */
public class StringController implements Initializable {
    static Logger logger = Logger.getLogger(StringController.class);

    private static final double INITIALWIDTH = 625.0;
    private static final double INITIALHEIGHT = 38.0;

    private IEventBusServer eventBus;
    private Double percentageProced = 0.0;

    @FXML
    private ScrollPane scrollPane;

    @Inject
    public void setEventBus(IEventBusServer eventBus)
    {
        this.eventBus = eventBus;
    }

    /**
     * Draws string defining current process state of the string
     * @param automataChangeEvent
     */

    @Subscribe
    public void handleAutomataChangeEvent(AutomataChangeEvent automataChangeEvent)
    {

        String proc = automataChangeEvent.getProcessedString();
        String unProc = automataChangeEvent.getUnProcessedString();

        logger.trace("Displaying processed: " + proc + " unprocessed: " + unProc);

        if(proc.length() != 0 && unProc.length() != 0)
            percentageProced = (double)proc.length()/(double)(proc.length() + unProc.length());

        Font font = new Font("tahoma",25.0);

        // inspired by http://stackoverflow.com/questions/12915141/color-single-letters-in-javafx-buttontexts

        HBox coloredTextBox = HBoxBuilder.create()
                .minHeight(scrollPane.getHeight() -10)
                .minWidth(scrollPane.getWidth() -10)
                .alignment(Pos.CENTER)
        .spacing(0)
                .children(
                LabelBuilder.create().text(proc).textFill(Color.RED).font(font)
                       .build(),
                LabelBuilder.create().text(unProc).textFill(Color.BLACK).font(font)
                        .build()
        ).build();

        // inspired by http://stackoverflow.com/questions/16992731/javafx-2-making-a-scrollpane-automatically-scroll-to-the-edge-after-adding-cont

        DoubleProperty wProperty = new SimpleDoubleProperty();
        wProperty.bind(coloredTextBox.widthProperty());

        wProperty.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                scrollPane.setHvalue(percentageProced);
            }
        }) ;

        scrollPane.setContent(coloredTextBox);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ViewUtilities.setToInitialProportions(scrollPane, INITIALWIDTH, INITIALHEIGHT);

        scrollPane.setStyle("-fx-background-color:white;");

        Injector injector = Guice.createInjector(new EventBusServerModule(this), new DrawingTemplateModule());
        injector.injectMembers(this);
    }
}

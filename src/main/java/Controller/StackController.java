package Controller;

import DependencyInjection.DrawingTemplateModule;
import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.AutomataChangeEvent;
import Utils.StackIOString;
import Utils.ViewUtilities;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
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
        public class StackController implements Initializable {
    static Logger logger = Logger.getLogger(StackController.class);
    private static final double INITIALWIDTH = 100.0;
            private static final double INITIALHEIGHT = 472.0;
            private IEventBusServer eventBus;

            @FXML
            public ScrollPane scrollPane;

    /**
     * Draws the green stack containing current state of the automata's stack
     * @param automataChangeEvent - the automata event triggering action
     */
            @Subscribe
            public void handleAutomataChangeEvent(AutomataChangeEvent automataChangeEvent)
            {
                logger.trace("Redrawing stack");
                StackIOString stackString = new StackIOString(automataChangeEvent.getCurrInstanteneousDescription()
                        .getCurrStack().fullContents());

                stackString.reverse();

                Font font = new Font("tahoma",25.0);

                VBox stackBox = VBoxBuilder.create()
                        .minHeight(scrollPane.getHeight() - 10)
                        .minWidth(scrollPane.getWidth() - 10)
                        .alignment(Pos.BOTTOM_CENTER)
                        .spacing(0)
                        .build();

        for(Character charStack : stackString)
        {
             if(charStack != '\u0000')
             {
                Label stackCharLabel = LabelBuilder.create()
                    .prefHeight(90)
                    .prefWidth(90)
                    .style("-fx-border-color: black; -fx-background-color:linear-gradient(from 0% 0% to " +
                            "100% 200%, green  0% , black 100%);")
                    .text(charStack.toString())
                    .font(font)
                    .alignment(Pos.CENTER)
                    .build();

                stackBox.getChildren().add(stackCharLabel);
             }
           }

           scrollPane.setVvalue(scrollPane.getVmax());
           scrollPane.setContent(stackBox);
    }


    @Inject
    public void setEventBus(IEventBusServer eventBus)
    {
        this.eventBus = eventBus;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ViewUtilities.setToInitialProportions(scrollPane, INITIALWIDTH, INITIALHEIGHT);

        scrollPane.setStyle("-fx-background-color:white;");

        Injector injector = Guice.createInjector(new EventBusServerModule(this), new DrawingTemplateModule());
        injector.injectMembers(this);
    }
}

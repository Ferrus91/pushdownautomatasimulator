package View;

import Database.IConnectionContext;
import DependencyInjection.*;
import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.*;
import Model.AnimationModel.IAutomata;
import Model.DrawingModel.IDrawingLists;
import Utils.DialogueUtils;
import XStreamStaging.Serialize;
import XStreamStaging.StagingAutomata;
import XStreamStaging.StagingLoader;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 25/06/13
 * Time: 23:02
 * To change this template use File | Settings | File Templates.
 */
public class PDAGUILoader extends Application {
    static Logger logger = Logger.getLogger(PDAGUILoader.class);

    @Inject
    private IDrawingLists drawingModel;
    @Inject
    private IAutomata animationModel;
    @Inject
    private IConnectionContext connectionContext;
    @Inject
    private IEventBusServer eventBus;
    @Inject
    private Serialize serialize;
    @Inject
    private StagingLoader stagingLoader;

    @Override
    public void start(Stage primaryStage) throws Exception{
        logger.trace("Starting GUI application");
        Injector injector = Guice.createInjector(new ConnectionContextModule(),
                new EventBusServerModule(this),
                new XStreamerModule(),
                new AutomataModule(),
                new DrawingListModule());
        injector.injectMembers(this);
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("pda.fxml") );
        Parent root = (Parent)fxmlloader.load();
        primaryStage.setTitle("PDA application");
        primaryStage.setScene(new Scene(root, 1299, 730));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                connectionContext.close();
            }
        });
        eventBus.post(new DeactivateAnimationButtonsEvent());
    }

    @Subscribe
    public void handleLoadAutomataEvent(LoadAutomataEvent loadAutomataEvent)
    {
        StagingAutomata stagingAutomata = serialize.deserialize(loadAutomataEvent.getXml());
        if(animationModel != null)
        {
            eventBus.post(new ActivateAnimationButtonsEvent());
            animationModel.deregisterEventBus();
        }
        animationModel = stagingLoader.stagingAutomataToAutomata(stagingAutomata);
        if(drawingModel != null)
            drawingModel.deregisterEventBus();
        drawingModel = stagingLoader.stagingAutomataToDrawingLists(stagingAutomata);
        eventBus.post(new RequestPaneRedrawEvent());
        eventBus.post(new DirtyFileEvent(loadAutomataEvent.getId()));
    }

    @Subscribe
    public void handleReloadAutomataEvent(ReloadAutomataEvent reloadAutomataEvent)
    {
        StagingAutomata stagingAutomata = serialize.deserialize(reloadAutomataEvent.getXml());
        if(animationModel != null)
        {
            eventBus.post(new DirtyFileEvent(reloadAutomataEvent.getId()));
            eventBus.post(new ActivateAnimationButtonsEvent());
            animationModel.deregisterEventBus();
        }
        animationModel = stagingLoader.stagingAutomataToAutomata(stagingAutomata);
    }


    public static void main(String[] args) {
        logger.trace("Ending GUI application");
        launch(args);
    }

}

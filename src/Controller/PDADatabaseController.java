package Controller;

import Database.*;
import DependencyInjection.ConnectionContextModule;
import DependencyInjection.ConnectionContextProvider;
import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.*;
import GraphicsElements.DatabaseTextFieldTreeCell;
import Utils.DialogueUtils;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 28/06/13
 * Time: 22:27
 * To change this template use File | Settings | File Templates.
 */
public class PDADatabaseController implements Initializable {
    static Logger logger = Logger.getLogger(PDADatabaseController.class);
    @FXML
    private TreeView<TreeNodeObject> treeList;

    @FXML
    private Button addDirectory;

    @FXML
    private Button deleteDirectory;

    @FXML
    private Button deleteAutomata;

    @FXML
    private Button loadAutomata;

    @Inject
    private IEventBusServer eventBus;

    @Inject
    private IConnectionContext connectionContext;


    @Subscribe
    public void handleRedrawTreeEvent(RedrawTreeViewEvent redrawTreeView)
    {
        logger.trace("Redrawing tree view");
        Connection connection = redrawTreeView.getConnection();
        TreeItem<TreeNodeObject> treeItem = new TreeItem<TreeNodeObject>(new DatabaseRoot("Directories"));

        ArrayList<DatabaseDirectory> databaseDirectories = connectionContext.getDirectories(connection);

        for(TreeNodeObject databaseDirectory : databaseDirectories)
        {
            TreeItem<TreeNodeObject> directoryLevelItem = new TreeItem<TreeNodeObject>(databaseDirectory);
            ArrayList<DatabaseAutomata> automatas = connectionContext.automataList(connection, directoryLevelItem.getValue().getId());

            for(DatabaseAutomata automata : automatas)
            {
                directoryLevelItem.getChildren().add(new TreeItem<TreeNodeObject>(automata));
            }

            treeItem.getChildren().add(directoryLevelItem);
        }
        treeList.setRoot(treeItem);
    }

    /**
     * This method allows the buttons to be disabled/enabled.
     * @param isDeleteAutomata
     * @param isLoadAutomata
     * @param isDeleteDirectory
     */

    private void flipSwitches(boolean isDeleteAutomata,
                              boolean isLoadAutomata,
                              boolean isDeleteDirectory) {
        logger.trace("Delete directory is disabled :" + isDeleteAutomata + "Load Automata is disabled"
        + isLoadAutomata + "Delete directory is disabled" + isDeleteAutomata);
        deleteDirectory.setDisable(isDeleteAutomata);
        loadAutomata.setDisable(isLoadAutomata);
        deleteAutomata.setDisable(isDeleteDirectory);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Injector injector = Guice.createInjector(new EventBusServerModule(this), new ConnectionContextModule());
        injector.injectMembers(this);

        // all start inactive

        flipSwitches(true, true, true);

        treeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //all are swapped according to type of tree view item

        treeList.getFocusModel().focusedItemProperty().addListener(new ChangeListener<TreeItem<TreeNodeObject>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<TreeNodeObject>> observableValue,
                                TreeItem<TreeNodeObject> oldItem, TreeItem<TreeNodeObject> newItem) {
                if(newItem != null
                        && newItem.getValue().getTreeNodeType() != null)
                    switch (newItem.getValue().getTreeNodeType()){
                        case AUTOMATA:
                            logger.trace("Automaton entry found");
                            flipSwitches(true, false, false);
                            break;
                        case DIRECTORY:
                            logger.trace("Directory entry found");
                            flipSwitches(false, true, true);
                            break;
                        default:
                            logger.trace("Any other entry found");
                            flipSwitches(true, true, true);
                            break;
                    }
                else
                    flipSwitches(true, true, true);
            }
        });

        // calls add directory popup

        addDirectory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                logger.trace("Adding directory view loaded");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/DatabaseDialog/adddirectory.fxml"));
                try {
                    Parent newRoot = (Parent)loader.load();
                    Stage newStage = new Stage();
                    newStage.setTitle("Add Directory");
                    newStage.setScene(new Scene(newRoot, 516, 162));
                    newStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // calls delete directory on the item

        deleteDirectory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(treeList.getSelectionModel().getSelectedItem().getValue().getName().equals("Default"))
                {
                    logger.error("Delete default attempted");
                    DialogueUtils.warningMessage("Can't delete the default directory");
                }
                    else
                {
                    if(DialogueUtils.choiceBox("Are you sure you want to delete the directory "
                            + treeList.getSelectionModel().getSelectedItem().getValue().getName()))
                        logger.trace("Deleting " + treeList.getSelectionModel().getSelectedItem().getValue().getName());
                        eventBus.post(new DeleteDirectoryEvent(treeList.getSelectionModel()
                                .getSelectedItem().getValue()));
                }
            }
        });

        // calls delete automaton on the item selected

        deleteAutomata.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(DialogueUtils.choiceBox("Are you sure you want to delete the automata "
                        + treeList.getSelectionModel().getSelectedItem().getValue().getName()))
                {
                    logger.trace("Deleting: " + treeList.getSelectionModel().getSelectedItem().getValue().getName());
                    eventBus.post(new DeleteAutomataEvent(treeList.getSelectionModel()
                            .getSelectedItem().getValue()));
                }
            }
        });

        loadAutomata.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String xml = connectionContext.getAutomataXML(treeList.getSelectionModel()
                        .getSelectedItem().getValue().getId());
                logger.trace("Loading the automaton: " + treeList.getSelectionModel()
                        .getSelectedItem().getValue().getName());
                eventBus.post(new LoadAutomataEvent(xml, treeList.getSelectionModel()
                        .getSelectedItem().getValue().getId()));
            }
        });

        treeList.setEditable(true);
        treeList.setCellFactory(new Callback<TreeView<TreeNodeObject>,TreeCell<TreeNodeObject>>(){
            @Override
            public TreeCell<TreeNodeObject> call(TreeView<TreeNodeObject> treeNodeObjectTreeView) {
                return new DatabaseTextFieldTreeCell(eventBus);
            }
        });

        // updates an edit committed from a text view
        treeList.onEditCommitProperty().setValue(new EventHandler<TreeView.EditEvent<TreeNodeObject>>() {
            @Override
            public void handle(TreeView.EditEvent<TreeNodeObject> treeNodeObjectEditEvent) {
                String name;
                Integer automataId;
                Integer directoryId;

                switch(treeNodeObjectEditEvent.getNewValue().getTreeNodeType())
                {
                    case DIRECTORY:
                        logger.trace("Editing directory name");
                        name = treeNodeObjectEditEvent.getNewValue().getName();
                        directoryId = treeNodeObjectEditEvent.getNewValue().getId();
                        eventBus.post(new UpdateDirectoryEvent(name, directoryId));
                        break;
                    case AUTOMATA:
                        logger.trace("Editing automaton name");
                        name = treeNodeObjectEditEvent.getNewValue().getName();
                        automataId = treeNodeObjectEditEvent.getNewValue().getId();
                        eventBus.post(new UpdateAutomataEvent(name, automataId));
                        break;
                }
            }
            });

        eventBus.post(new RequestConnectionEvent());
    }
}

package Database;

import Controller.PopUpControllers.AddAutomataDialogController;
import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.*;
import Utils.DialogueUtils;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.CharStreams;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/08/13
 * Time: 16:37
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class ConnectionContext implements IConnectionContext {
    static Logger logger = Logger.getLogger(ConnectionContext.class);

    private static String AUTOMATA_MOVE_DIRECTORY_QUERY;
    private static String XML_ROW_NAME;
    private static String LOAD_AUTOMATA_QUERY;
    private static String UPDATE_DIRECTORY_QUERY;
    private static String UPDATE_AUTOMATA_QUERY;
    private static String AUTOMATA_DIRECTORY_ID_ROW;
    private static String DELETE_AUTOMATA_QUERY;
    private static String DELETE_DIRECTORY_QUERY;
    public static String URL;
    public static String USERNAME;
    private static String INSERT_DIRECTORY_QUERY;
    private static String DIRECTORY_NAME_ROW;
    private static String DIRECTORY_ID_ROW;
    private static String DIRECTORY_LEVEL_QUERY;
    private static String AUTOMATA_LEVEL_QUERY;
    private static String AUTOMATA_ID_ROW;
    private static String AUTOMATA_NAME_ROW;
    private static String INSERT_AUTOMATA_QUERY;
    private static String AUTOMATA_XML_UPDATE_QUERY;
    private static String GET_CURRENT_SEQUENCE_VALUE;

    @Setter
    private Connection connection;

    @Setter
    private IEventBusServer eventBus;

    @Inject
    public ConnectionContext(Connection connection, IEventBusServer eventBus)
    {
        this.eventBus = eventBus;
        this.connection = connection;
        try {
            Properties properties = new Properties();
            URL = connection.getMetaData().getURL();
            USERNAME = connection.getMetaData().getUserName();
            properties.load(ConnectionContext.class.getClassLoader()
                    .getResourceAsStream("Properties/database.properties"));
            INSERT_DIRECTORY_QUERY = properties.getProperty("insertdirectoryquery");
            DELETE_DIRECTORY_QUERY = properties.getProperty("deletedirectoryquery");
            DELETE_AUTOMATA_QUERY = properties.getProperty("deleteautomataquery");
            DIRECTORY_LEVEL_QUERY = properties.getProperty("directorylevelquery");
            DIRECTORY_LEVEL_QUERY = properties.getProperty("directorylevelquery");
            DIRECTORY_NAME_ROW = properties.getProperty("directorynamerow");
            DIRECTORY_ID_ROW = properties.getProperty("directoryidrow");
            AUTOMATA_LEVEL_QUERY = properties.getProperty("automatalevelquery");
            AUTOMATA_ID_ROW = properties.getProperty("automataidrow");
            AUTOMATA_NAME_ROW = properties.getProperty("automatanamerow");
            INSERT_AUTOMATA_QUERY = properties.getProperty("insertautomataquery");
            AUTOMATA_DIRECTORY_ID_ROW = properties.getProperty("automatadirectoryidrow");
            UPDATE_AUTOMATA_QUERY = properties.getProperty("updateautomataquery");
            UPDATE_DIRECTORY_QUERY = properties.getProperty("updatedirectoryquery");
            LOAD_AUTOMATA_QUERY = properties.getProperty("loadautomataquery");
            XML_ROW_NAME= properties.getProperty("xmlrowname");
            AUTOMATA_XML_UPDATE_QUERY= properties.getProperty("automataxmlupdatequery");
            GET_CURRENT_SEQUENCE_VALUE= properties.getProperty("getcurrentsequencevalue");
            AUTOMATA_MOVE_DIRECTORY_QUERY= properties.getProperty("automatamovedirectoryquery");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        eventBus.register(this);
    }

    /**
     * post a new event.
     * @param requestConnectionEvent
     */

    @Override
    @Subscribe
    public void handleRequestConnectionEvent(RequestConnectionEvent requestConnectionEvent)
    {
        eventBus.post(new RedrawTreeViewEvent(connection));
    }

    /**
     * close the connecion of the database
     */

    public void close()
    {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *   return an xml of the automaton by id
     * @param id id of automaton to be returned
     * @return xml file of the automaton to be deserialised
     */
    @Override
    public String getAutomataXML(Integer id) {
        try {
            logger.trace("Getting automaton " + id);
            PreparedStatement preparedStatement = connection.prepareStatement(LOAD_AUTOMATA_QUERY);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            Clob clob = rs.getClob(XML_ROW_NAME);
            Reader reader = clob.getCharacterStream();
            try {
                return CharStreams.toString(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            logger.error("Retrieve automaton sql failed");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deletes an automaton by id in the database
     * @param deleteAutomataEvent id of automaton to be deleted
     */

    @Override
    @Subscribe
    public void handleDeleteAutomataEvent(DeleteAutomataEvent deleteAutomataEvent) {
        try {
            logger.trace("Deleting automaton" + deleteAutomataEvent.getDeleteAutomata().getId());
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_AUTOMATA_QUERY);
            preparedStatement.setInt(1, deleteAutomataEvent.getDeleteAutomata().getId());
            preparedStatement.execute();
            eventBus.post(new RedrawTreeViewEvent(connection));
        } catch (SQLException e) {
            logger.error("Delete automaton sql failed");
            e.printStackTrace();
        }
    }

    /**
     * Handles the placement of a new directory into the database
     * @param newDirectoryEvent has the new directory name
     */

    @Override
    @Subscribe
    public void handleNewDirectoryEvent(NewDirectoryEvent newDirectoryEvent) {
        try {
            logger.trace("Creating new directory in database");
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DIRECTORY_QUERY);
            preparedStatement.setString(1, newDirectoryEvent.getDirectoryName());
            preparedStatement.execute();
            eventBus.post(new RedrawTreeViewEvent(connection));
        } catch (SQLException e) {
            logger.error("Add directory sql failed");
            e.printStackTrace();
        }
    }

    /**
     * Deletes a directory from the system
     * @param deleteDirectoryEvent contains directory deleted id
     */

    @Override
    @Subscribe
    public void handleDeleteDirectoryEvent(DeleteDirectoryEvent deleteDirectoryEvent) {
        try {
            logger.trace("Deleting");
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_DIRECTORY_QUERY);
            preparedStatement.setInt(1, deleteDirectoryEvent.getSelectedItem().getId());
            preparedStatement.execute();
            eventBus.post(new RedrawTreeViewEvent(connection));
        } catch (SQLException e) {
            logger.error("Delete automaton sql failed");
            e.printStackTrace();
        }
    }

    /**
     * Updates the xml for an automaton with a certain id
     * @param updateAutomataXMLEvent has xml file for an automaton and its id.
     */

    @Override
    @Subscribe
    public void handleUpdateAutomataXMLEvent(UpdateAutomataXMLEvent updateAutomataXMLEvent) {
        try {
            logger.trace("Updating Automaton's XML " + updateAutomataXMLEvent.getId());
            PreparedStatement preparedStatement = connection.prepareStatement(AUTOMATA_XML_UPDATE_QUERY);
            Clob xmlClob = connection.createClob();
            xmlClob.setString(1, updateAutomataXMLEvent.getXml());
            preparedStatement.setClob(1, xmlClob);
            preparedStatement.setInt(2, updateAutomataXMLEvent.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error("Update automaton xml sql failed");
            e.printStackTrace();
        }
    }

    /**
     * Handles a request to the database to move an automaton from one directory to the other
     * @param moveAutomataEvent contains id of automaton and directory to move to
     */

    @Override
    @Subscribe
    public void handleMoveAutomataEvent(MoveAutomataEvent moveAutomataEvent) {
        try {
            logger.trace("Moving automaton " + moveAutomataEvent.getAutomataId()
                    + " to directory " + moveAutomataEvent.getId());
            PreparedStatement preparedStatement = connection.prepareStatement(AUTOMATA_MOVE_DIRECTORY_QUERY);
            preparedStatement.setInt(1, moveAutomataEvent.getId());
            preparedStatement.setInt(2, moveAutomataEvent.getAutomataId());
            preparedStatement.execute();
            eventBus.post(new RedrawTreeViewEvent(connection));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles an a request to the database to place the xml skeleton of an automaton into the system
     * @param insertAutomataEvent the automaton with its xml content to be inserted
     */

    @Override
    @Subscribe
    public void handleInsertAutomataEvent(InsertAutomataEvent insertAutomataEvent) {
        logger.trace("Loading add automaton fxml");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/DatabaseDialog/addautomata.fxml"));
        try {
            Parent newRoot = (Parent)loader.load();
            AddAutomataDialogController addAutomataDialogController =
                    (AddAutomataDialogController)loader.getController();
            ArrayList<DatabaseDirectory> databaseDirectories = getDirectories(connection);
            addAutomataDialogController.setList(databaseDirectories);
            addAutomataDialogController.setXmlString(insertAutomataEvent.getXmlAutomata());
            addAutomataDialogController.setIsCloseEvent(insertAutomataEvent.isClose());
            Stage newStage = new Stage();
            newStage.setTitle("New Automata");
            newStage.setScene(new Scene(newRoot, 516, 162));
            newStage.show();
        } catch (IOException e) {
            logger.error("Load add automaton view failed");
            e.printStackTrace();
        }
    }

    private ArrayList<String> getDirectoriesString(Connection connection) {
        logger.trace("Retrieving directories for controller");
        ArrayList<DatabaseDirectory> automataList = getDirectories(connection);
        ArrayList<String> automataListString = new ArrayList<String>();
        for(DatabaseDirectory databaseDirectory : automataList)
        {
            automataListString.add(databaseDirectory.getName());
        }
        return automataListString;
    }

    public ArrayList<DatabaseAutomata> automataList(Connection connection, Integer directoryId) {
        logger.trace("Retrieving automata for controller");
        ArrayList<DatabaseAutomata> automataList = new ArrayList<DatabaseAutomata>();
        try {
            PreparedStatement topLevel = connection.prepareStatement(AUTOMATA_LEVEL_QUERY);
            topLevel.setInt(1, directoryId);
            ResultSet resultSet= topLevel.executeQuery();

            while(resultSet.next())
            {
                DatabaseAutomata automataResult =
                        new DatabaseAutomata(resultSet.getInt(AUTOMATA_ID_ROW),
                                resultSet.getString(AUTOMATA_NAME_ROW),
                                resultSet.getInt(AUTOMATA_DIRECTORY_ID_ROW));
                automataList.add(automataResult);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return automataList;
    }

    @Override
    @Subscribe
    public void handleUpdateAutomataEvent(UpdateAutomataEvent updateAutomataEvent)
    {
        try {
            logger.trace("Updating automaton");
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_AUTOMATA_QUERY);
            preparedStatement.setString(1, updateAutomataEvent.getName());
            preparedStatement.setInt(2, updateAutomataEvent.getAutomataId());
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error("Update automaton sql failed");
            e.printStackTrace();
        }
    }

    @Override
    @Subscribe
    public void handleUpdateDirectoryEvent(UpdateDirectoryEvent updateDirectoryEvent)
    {
        try {
            logger.trace("Update directory sql failed");
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DIRECTORY_QUERY);
            preparedStatement.setString(1, updateDirectoryEvent.getName());
            preparedStatement.setInt(2, updateDirectoryEvent.getDirectoryId());
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error("Update directory sql failed");
            e.printStackTrace();
        }
    }

    @Subscribe
    public void handleNewAutomataEvent(NewAutomataEvent newAutomataEvent)
    {
        try {
            logger.trace("New automaton sql failed");
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_AUTOMATA_QUERY);
            preparedStatement.setString(1, newAutomataEvent.getAutomataName());
            Clob xmlClob = connection.createClob();
            xmlClob.setString(1, newAutomataEvent.getXmlString());
            preparedStatement.setClob(2, xmlClob);
            preparedStatement.setInt(3, newAutomataEvent.getSelectedItem().getId());
            preparedStatement.execute();
            Statement statement = connection.createStatement();
            statement.execute(GET_CURRENT_SEQUENCE_VALUE);
            ResultSet rs = statement.getResultSet();
            rs.next();
            Integer newAutomataId = rs.getInt(1);
            if(newAutomataEvent.getIsCloseEvent())
                Platform.exit();
            else{
                if(DialogueUtils.choiceBox("Do you want to load this into the animation screen?"))
                    eventBus.post(new ReloadAutomataEvent(newAutomataEvent.getXmlString(), newAutomataId));
                eventBus.post(new RedrawTreeViewEvent(connection));
                eventBus.post(new SetAutomataDBDetailsEvent(newAutomataId));
            }
        } catch (SQLException e) {
            logger.error("New automaton sql failed");
            e.printStackTrace();
        }
    }

    public ArrayList<DatabaseDirectory> getDirectories(Connection connection) {
        ArrayList<DatabaseDirectory> databaseDirectories = new ArrayList<DatabaseDirectory>();
        try {
            logger.trace("Retrieving directories");
            Statement topLevel = connection.createStatement();
            ResultSet resultSet= topLevel.executeQuery(DIRECTORY_LEVEL_QUERY);

            while(resultSet.next())
            {
                DatabaseDirectory databaseDirectory =
                        new DatabaseDirectory(resultSet.getInt(DIRECTORY_ID_ROW),
                                resultSet.getString(DIRECTORY_NAME_ROW));
                databaseDirectories.add(databaseDirectory);
            }
        } catch (SQLException e) {
            logger.error("Retrieving directories sql failed");
            e.printStackTrace();
        }
        return databaseDirectories;
    }

}

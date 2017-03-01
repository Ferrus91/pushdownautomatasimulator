package Database;

import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.*;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/08/13
 * Time: 16:42
 * To change this template use File | Settings | File Templates.
 */
public interface IConnectionContext {
    public void handleInsertAutomataEvent(InsertAutomataEvent insertAutomataEvent);
    public void handleDeleteAutomataEvent(DeleteAutomataEvent deleteAutomataEvent);
    public void handleNewDirectoryEvent(NewDirectoryEvent newDirectoryEvent);
    public void handleDeleteDirectoryEvent(DeleteDirectoryEvent deleteDirectoryEvent);
    public void handleRequestConnectionEvent(RequestConnectionEvent requestConnectionEvent);
    public void handleUpdateAutomataEvent(UpdateAutomataEvent updateAutomataEvent);
    public void handleUpdateDirectoryEvent(UpdateDirectoryEvent updateDirectoryEvent);
    public void close();
    public String getAutomataXML(Integer id);
    public void handleUpdateAutomataXMLEvent(UpdateAutomataXMLEvent updateAutomataXMLEvent);
    public void handleMoveAutomataEvent(MoveAutomataEvent moveAutomataEvent);
    public ArrayList<DatabaseDirectory> getDirectories(Connection connection);
    public ArrayList<DatabaseAutomata> automataList(Connection connection, Integer id);
}

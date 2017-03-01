package DependencyInjection;

import Database.ConnectionContext;
import Database.IConnectionContext;
import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import DrawingTemplate.TransitionDrawingObject;
import com.google.inject.AbstractModule;
import com.google.inject.Scope;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;
import org.h2.Driver;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/08/13
 * Time: 16:47
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionContextModule extends AbstractModule {
    static Logger logger = Logger.getLogger(ConnectionContextModule.class);

    private static String DATABASE_CLASS;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    private static Connection CONNECTION;
    private static IEventBusServer EVENTBUS;

    static
    {
        Properties properties = new Properties();
        try {
            logger.trace("Setting up connection");
            properties.load(TransitionDrawingObject.class.getClassLoader()
                    .getResourceAsStream("Properties/database.properties"));
            DATABASE_CLASS = properties.getProperty("databaseclass");
            String projectPath = URLDecoder.decode(ConnectionContextModule.class.getProtectionDomain().getCodeSource()
                    .getLocation().getPath(), "UTF-8");
            projectPath = projectPath.substring(0, projectPath.lastIndexOf("/"));
            URL = URLDecoder.decode(properties.getProperty("url") + projectPath + "\\Database\\DefaultDatabase", "UTF-8");
            USERNAME = properties.getProperty("username");
            PASSWORD = properties.getProperty("password");
            Class.forName(DATABASE_CLASS);
            DriverManager.registerDriver(new Driver());
            CONNECTION = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (IOException e) {
            logger.error("Database already in use");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        EVENTBUS = EventBusServerModule.EVENT_BUS_SERVER;
    }

    @Override
    protected void configure() {
        logger.trace("Configuring connection context");
        bind(Connection.class).toInstance(CONNECTION);
        bind(IEventBusServer.class).toInstance(EVENTBUS);
        bind(IConnectionContext.class).toProvider(ConnectionContextProvider.class);
    }
}

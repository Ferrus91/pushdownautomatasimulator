/*
 * Created by IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 13/09/13
 * Time: 18:45
 */
package DependencyInjection;

import Database.ConnectionContext;
import DependencyInjection.EventBusServer.IEventBusServer;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;

import java.sql.Connection;

public class ConnectionContextProvider implements Provider<ConnectionContext> {
    static Logger logger = Logger.getLogger(ConnectionContextProvider.class);
    private static ConnectionContext CONNECTIONCONTEXT;

    @Inject
    ConnectionContextProvider(Connection connection, IEventBusServer eventBusServer)
    {
        if(CONNECTIONCONTEXT == null)
        {
            logger.trace("Setting up static connection context");
            CONNECTIONCONTEXT = new ConnectionContext(connection, eventBusServer);
        }
    }

    @Override
    @Singleton
    public ConnectionContext get() {
        return CONNECTIONCONTEXT;
    }
}

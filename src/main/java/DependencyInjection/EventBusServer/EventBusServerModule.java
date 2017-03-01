package DependencyInjection.EventBusServer;

import com.google.inject.AbstractModule;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/07/13
 * Time: 22:50
 * To change this template use File | Settings | File Templates.
 */
public class EventBusServerModule extends AbstractModule {
    static Logger logger = Logger.getLogger(EventBusServerModule.class);
    public static EventBusServer EVENT_BUS_SERVER = new EventBusServer();

    public EventBusServerModule(Object object) {
        EVENT_BUS_SERVER.register(object);
    }

    @Override
    protected void configure() {
        logger.trace("Configuring event bus");
        bind(IEventBusServer.class).toInstance(EVENT_BUS_SERVER);
    }
}

package DependencyInjection;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 29/06/13
 * Time: 17:46
 * To change this template use File | Settings | File Templates.
 */
public class EventBusModule extends AbstractModule {
    static Logger logger = Logger.getLogger(EventBusModule.class);

    // Inspired by https://gist.github.com/dewind/1606524

    static public final EventBus ANIMATIONEVENTS = new EventBus("Animation EventBus");
    static public final EventBus DRAWINGEVENTS = new EventBus("Drawing EventBus");
    static public final EventBus DATABASEEVENTS = new EventBus("Database EventBus");

    protected void configure()
    {
        logger.trace("Configuring eventbus");
        bind(EventBus.class).annotatedWith(Animation.class).toInstance(ANIMATIONEVENTS);
        bind(EventBus.class).annotatedWith(Drawing.class).toInstance(DRAWINGEVENTS);
        bind(EventBus.class).annotatedWith(Database.class).toInstance(DATABASEEVENTS);
    }
}

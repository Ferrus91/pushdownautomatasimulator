package DependencyInjection.EventBusServer;

import DependencyInjection.EventBusModule;
import EventObjects.EventObject;
import com.google.common.eventbus.EventBus;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/07/13
 * Time: 22:51
 * To change this template use File | Settings | File Templates.
 */
public class EventBusServer implements IEventBusServer{
    static Logger logger = Logger.getLogger(IEventBusServer.class);

    private EventBus animationEventBus;
    private EventBus drawingEventBus;
    private EventBus databaseEventBus;

    /**
     * An proxy pattern, which hides the three different event buses for the different parts of the application
     * behind a single interface resembling the event bus. Switch statement picks the right one based
     * on enum value which is part of the interface for all events in the application
     * @param eventObject
     */

    public void post(EventObject eventObject)
    {
       switch(eventObject.getInstanceToken())
       {
           case ANIMATION:
               logger.trace(eventObject.getClass().getSimpleName() + "being posted to animation");
               animationEventBus.post(eventObject);
               break;
           case DRAWING:
               logger.trace(eventObject.getClass().getSimpleName() + "being posted to drawing");
               drawingEventBus.post(eventObject);
               break;
           case DATABASE:
               logger.trace(eventObject.getClass().getSimpleName() + "being posted to database");
               databaseEventBus.post(eventObject);
               break;
       }
    }

    /**
     * makes sure each component is registered with all event buses
     * @param obj - object being registered
     */

    public void register(Object obj)
    {
        logger.trace("Object " + obj.getClass().getSimpleName()
                + "is being registered with eventbus system");
        animationEventBus.register(obj);
        drawingEventBus.register(obj);
        databaseEventBus.register(obj);
    }

    /**
     * makes sure each component is deregistered with all event buses
     * @param obj - object being registered
     */

    public void unregister(Object obj)
    {
        logger.trace("Object " + obj.getClass().getSimpleName()
                + "is being deregistered with eventbus system");
        register(obj);
        animationEventBus.unregister(obj);
        drawingEventBus.unregister(obj);
        databaseEventBus.unregister(obj);
    }

    /**
     * static event bus instances assigned as variables
     */

    public EventBusServer()
    {
        animationEventBus = EventBusModule.ANIMATIONEVENTS;
        drawingEventBus = EventBusModule.DRAWINGEVENTS;
        databaseEventBus = EventBusModule.DATABASEEVENTS;
    }
}

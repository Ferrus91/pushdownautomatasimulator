package DependencyInjection.EventBusServer;

import EventObjects.EventObject;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/07/13
 * Time: 23:01
 * Basic eventbus interface for proxy
 */
public interface IEventBusServer {
    public void register(Object obj);
    public void unregister(Object obj);
    public void post(EventObject eventObject);
}


package EventObjects;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 23/07/13
 * Time: 00:46
 * To change this template use File | Settings | File Templates.
 */
public enum ZoomedEvent implements EventObject {
    ZOOM_IN, ZOOM_OUT;

    @Override
    public EventBusInstance getInstanceToken() {
        return EventBusInstance.DRAWING;
    }
}

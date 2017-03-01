package DrawingTemplate;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 08/08/13
 * Time: 23:32
 * To change this template use File | Settings | File Templates.
 */
public interface IGUIFactory<T,U> {
    T create(U guiTemplate);
}

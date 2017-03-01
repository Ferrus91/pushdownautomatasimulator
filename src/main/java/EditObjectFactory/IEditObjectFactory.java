package EditObjectFactory;

import PDAGraphicElements.PDANode;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 10/08/13
 * Time: 17:02
 * To change this template use File | Settings | File Templates.
 */
public interface IEditObjectFactory {
    PDANode createPDANode(String uuid, double x, double y, Boolean isDownNode);
}

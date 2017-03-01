package DrawingTemplate;

import Model.ILink;
import PDAGraphicElements.PDANode;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 14/08/13
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public interface IRelaventNodeFactory {
    public ArrayList<PDANode> build(ILink link, Pane pane);
}

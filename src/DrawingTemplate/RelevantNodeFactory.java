package DrawingTemplate;

import Model.ILink;
import PDAGraphicElements.PDANode;
import Utils.ViewUtilities;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 14/08/13
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */
public class RelevantNodeFactory implements IRelaventNodeFactory {

    public static Integer FROMINDEX = 0;
    public static Integer TOINDEX = 1;

    @Override
    public ArrayList<PDANode> build(ILink link, Pane pane) {
        String fromId = link.getFromNode().getId();
        String toId = link.getToNode().get(0).getId();

        ArrayList<PDANode> relevantNodes = new ArrayList<PDANode>();

        PDANode fromPDANode = ViewUtilities.searchForNode(pane.getChildren(), fromId);
        relevantNodes.add(fromPDANode);
        PDANode toPDANode = ViewUtilities.searchForNode(pane.getChildren(), toId);
        relevantNodes.add(toPDANode);

        if(relevantNodes.get(FROMINDEX) != null && relevantNodes.get(TOINDEX) != null)
            return relevantNodes;
        else return null;
   }

}

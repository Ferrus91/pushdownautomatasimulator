package DrawingTemplate;

import Model.INode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 08/08/13
 * Time: 23:27
 * To change this template use File | Settings | File Templates.
 */
public interface IGUITemplate<U,V,W> {
    public void drawNode(INode node, U model);
    public void drawStartNode(INode node, U model);
    public void drawRelations(V nodeConnections, ArrayList<W> helperList);
}

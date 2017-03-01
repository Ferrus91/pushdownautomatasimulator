package Model;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 05/07/13
 * Time: 05:00
 * To change this template use File | Settings | File Templates.
 */
public interface ILink extends PDAItem {
    public INode getFromNode();
    public List<INode> getToNode();
}

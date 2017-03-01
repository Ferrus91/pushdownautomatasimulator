package Model;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 05/07/13
 * Time: 04:59
 * To change this template use File | Settings | File Templates.
 */
public interface INode extends PDAItem {
    public Integer getX();
    public Integer getY();
    public void setX(Integer x);
    public void setY(Integer y);
    public String getId();
    public String getLabel();
    public void setLabel(String label);
    public Boolean getIsStartNode();
    public void setIsStartNode(Boolean setNode);
    public Boolean getIsFinalNode();
    public void setIsFinalNode(Boolean setNode);
    public Boolean getIsNodeDown();
    public void setIsNodeDown(Boolean setNode);
}

package Model.DrawingModel;

import Model.ILink;
import Model.INode;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 04/08/13
 * Time: 21:07
 * To change this template use File | Settings | File Templates.
 */

public class Relation implements ILink, SelectableElement {
    static Logger logger = Logger.getLogger(Relation.class);

    @Getter
    INode fromNode;
    @Getter
    List<INode> toNode;
    @Getter @Setter
    List<EditTransitions> transitionsList;
    @Getter @Setter
    Boolean isSelected;

    public Relation(INode fromNode, List<INode> toNode, List<EditTransitions> transitionsList)
    {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.transitionsList = transitionsList;
        this.isSelected = false;
    }

    @Override
    public String toString() {

        if(transitionsList.size() > 0)
        {
            EditTransitions editTransition = transitionsList.get(0);

            String returnString = editTransition.transitionChar + "/" + editTransition.getPushString()
                    + "/" + editTransition.getPopChar();

            return transitionsList.size() > 1 ? returnString + "..." : returnString;
        }
        return null;
    }

    public String getId()
    {
       return fromNode.getId() + "-" + toNode.get(0).getId();
    }
}

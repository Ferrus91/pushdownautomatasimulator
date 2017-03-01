package Controller.ControllerLogic;

import Model.AnimationModel.ITransition;
import Model.INode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 06/07/13
 * Time: 19:07
 * Simplfied form of the datastructure passed to drawing logic.
 * Holds a set of node connections and builds a full string of them.
 */
@AllArgsConstructor
public class NodeConnections {
    static Logger logger = Logger.getLogger(NodeConnections.class);

    @Getter
    private INode toNode;
    @Getter
    private INode fromNode;
    @Getter
    private ArrayList<NodeConnection> transitionOptions;

    public void add(NodeConnection nodeConnection)
    {
        logger.trace("Adding:" + nodeConnection.toString());
        transitionOptions.add(nodeConnection);
    }

    @Override
    public String toString() {
        String returnString = "";

        logger.trace("Multiple string sources");
        for(NodeConnection nodeConnection : transitionOptions)
            returnString += nodeConnection.toString() + System.getProperty("line.separator");
        return returnString;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        else
            if(getClass() == obj.getClass())
            {
                NodeConnections compNodeConns = (NodeConnections) obj;
                return this.toNode == compNodeConns.getToNode() && this.fromNode == compNodeConns.getFromNode();
            }
            else
                return false;
    }

    @Override
    public int hashCode() {
        return this.fromNode.hashCode() + this.toNode.hashCode();
    }

    public boolean containsLastSelectedTransition(ITransition lastSelectedTransition) {
        if(lastSelectedTransition == null)
            return false;
        else
        {
            boolean ret = lastSelectedTransition.getFromNode() == fromNode
                    && lastSelectedTransition.getToNode().get(0) == toNode;
            return ret;
        }
        }
}

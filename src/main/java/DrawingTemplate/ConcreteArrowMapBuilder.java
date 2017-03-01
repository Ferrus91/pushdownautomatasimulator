package DrawingTemplate;

import Controller.ControllerLogic.NodeConnection;
import Controller.ControllerLogic.NodeConnections;
import Model.AnimationModel.ITransition;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 14/08/13
 * Time: 13:03
 * To change this template use File | Settings | File Templates.
 */
public class ConcreteArrowMapBuilder implements IArrowMapBuilder {
    static Logger logger = Logger.getLogger(ConcreteArrowMapBuilder.class);

    /**
     * Object which determines the arrow relations to be drawn based on the model - it turns the model into a flat object the drawing program can use
     * @param transitions
     */
    @Override
    public HashMap<ITransition, NodeConnections> build(List<ITransition> transitions) {
        logger.trace("Setting up arrow map");

        HashMap<ITransition, NodeConnections> arrowMap = new HashMap<ITransition, NodeConnections>();

        for(ITransition transition : transitions)
        {
            construct(arrowMap, transition);
        }

        return arrowMap;
    }

    private void construct(HashMap<ITransition, NodeConnections> arrowMap, ITransition transition) {
        if(!arrowMap.containsKey(transition))
        {
            NodeConnections nodeConnection = new NodeConnections(transition.getToNode().get(0), transition.getFromNode(), new ArrayList<NodeConnection>());
            nodeConnection.add(new NodeConnection(transition.getSymbolOfTransition(), transition.getOffStack(), transition.getOnStack().toString()));
            arrowMap.put(transition, nodeConnection);
        }
        else
        {
            arrowMap.get(transition).add(new NodeConnection(transition.getSymbolOfTransition(), transition.getOffStack(), transition.getOnStack().toString()));
        }
    }
}

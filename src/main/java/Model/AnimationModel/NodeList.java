package Model.AnimationModel;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 05/07/13
 * Time: 07:00
 * To change this template use File | Settings | File Templates.
 */
public class NodeList extends ArrayList<IPushDownNode> {
    static Logger logger = Logger.getLogger(NodeList.class);

    /**
     *
     * @return list of all transition
     */
    public List<ITransition> getAllTransitions()
    {
        logger.trace("Getting all transitions");

        List<ITransition> fullArray = new ArrayList<ITransition>();

        for(IPushDownNode node : this)
        {
            fullArray.addAll(node.getAllNodeTransitions());
        }

        return fullArray;
    }
}

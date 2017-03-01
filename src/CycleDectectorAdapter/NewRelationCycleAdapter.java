package CycleDectectorAdapter;

import EventObjects.EditConnectionEvent;
import Model.DrawingModel.DrawingLists;
import Model.DrawingModel.EditTransitions;
import Model.DrawingModel.IDrawingLists;
import Model.DrawingModel.Relation;
import Model.INode;
import org.apache.log4j.Logger;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultEdge;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 16/08/13
 * Time: 12:04
 * To change this template use File | Settings | File Templates.
 */
public class NewRelationCycleAdapter implements ICycleAdapter<IDrawingLists> {
    static Logger logger = Logger.getLogger(NewRelationCycleAdapter.class);


    /**
     * gets boolean result for the cycle checker
     * @param cycleChecker
     * @return
     */
    @Override
    public Boolean isCycle(IDrawingLists cycleChecker) {
        logger.trace("Detecting cycle");
        DirectedGraph<INode, DefaultEdge> cycleCheckingGraph = CreateNullGraph.build(cycleChecker);
        CycleDetector<INode, DefaultEdge> cycleDetector = new CycleDetector<INode, DefaultEdge>(cycleCheckingGraph);
        return cycleDetector.detectCycles();
    }


    /**
     * converts to graph and get a cycle
     * @param IDrawingLists
     * @param editConnectionEvent
     * @return
     */
    public static Boolean checkForNullCycle(IDrawingLists IDrawingLists, EditConnectionEvent editConnectionEvent) {
        logger.trace("Checking null cycle on: " + editConnectionEvent.getId());

        Boolean isFullProcessNeeded = false;

        for(EditTransitions editTransition : editConnectionEvent.getConnections())
        {
            if(editTransition.getTransitionChar() == '\u03B5')
            {
                isFullProcessNeeded = true;
                break;
            }

        }

        if(isFullProcessNeeded)
            return fullProcess(IDrawingLists, editConnectionEvent);
        else
            return false;
    }

    /**
     * full process
     * @param IDrawingLists
     * @param editConnectionEvent
     * @return
     */
    private static boolean fullProcess(IDrawingLists IDrawingLists, EditConnectionEvent editConnectionEvent) {
        IDrawingLists IDrawingListsCopy = new DrawingLists();
        IDrawingListsCopy.deregisterEventBus();

        IDrawingListsCopy.setNodes(IDrawingLists.getNodes());
        IDrawingListsCopy.setRelations(new ConcurrentHashMap<String, Relation>());

        logger.trace("Filtering for episilon transitions");

        for(Relation relation : IDrawingLists.getRelations().values())
        {
            Boolean hasEpsilonTransition = false;
            for(EditTransitions editTransitions : relation.getTransitionsList())
            {
                if(editTransitions.getTransitionChar() == '\u03B5')
                {
                    hasEpsilonTransition = true;
                    break;
                }
            }

            if(hasEpsilonTransition)
            {
                IDrawingListsCopy.getRelations().put(relation.getId(), relation);
            }

        }

        IDrawingListsCopy.getRelations().put(editConnectionEvent.getId(),
                IDrawingLists.getRelations().get(editConnectionEvent.getId()));

        NewRelationCycleAdapter newRelationCycleAdapter = new NewRelationCycleAdapter();

        return newRelationCycleAdapter.isCycle(IDrawingListsCopy);
    }
}

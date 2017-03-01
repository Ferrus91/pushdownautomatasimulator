package XStreamStaging;

import Model.AnimationModel.*;
import Model.DrawingModel.*;
import Model.INode;
import Utils.DialogueUtils;
import Utils.StackIOString;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 19/08/13
 * Time: 14:59
 * To change this template use File | Settings | File Templates.
 */
public class StagingLoader implements IStagingLoader {
    static Logger logger = Logger.getLogger(StagingLoader.class);

    /**
     * Drawinglist to staging automaton mapper
     * @param IDrawingLists
     * @return
     */
    @Override
    public StagingAutomata drawingListToStagingAutomata(IDrawingLists IDrawingLists) {
        logger.trace("Converting drawing model to staging automaton");

        StagingAutomata  stagingAutomata= new StagingAutomata(IDrawingLists.getIsLMachine(),
                IDrawingLists.getCanvasSize().getValue());

        for(DrawingNode drawingNode : IDrawingLists.getNodes().values())
        {
            StagingNode stagingNode = new StagingNode(drawingNode.getX(), drawingNode.getY(),
                    drawingNode.getId(), drawingNode.getLabel(),
                    drawingNode.getIsStartNode(), drawingNode.getIsFinalNode(),
                    drawingNode.getIsNodeDown(), drawingNode.getIsSelected(), new HashMap<String, StagingRelation>());
            stagingAutomata.put(stagingNode.id, stagingNode);
        }

        for(Relation relation : IDrawingLists.getRelations().values())
        {
            Map<String,StagingTransition> transitions = new HashMap<String, StagingTransition>();
            for(EditTransitions editTransitions : relation.getTransitionsList())
            {
                StagingTransition stagingTransition =
                        new StagingTransition(editTransitions.getTransitionChar() == '\u03B5' ?
                                '\u0000' : editTransitions.getTransitionChar(),
                                editTransitions.getPushString().equals(Character.toString('\u03B5')) ?
                                        "" : editTransitions.getPushString(),
                                editTransitions.getPopChar() == '\u03B5' ?
                                        '\u0000' : editTransitions.getPopChar());
                transitions.put(editTransitions.getTransitionChar()
                        +editTransitions.getPushString()+editTransitions.getPopChar()
                        , stagingTransition);
            }

            StagingRelation stagingRelation = new StagingRelation(relation.getFromNode().getId(),
                    relation.getToNode().get(0).getId(), transitions);

            stagingAutomata.get(stagingRelation.fromNodeId).put(stagingRelation.toNodeId, stagingRelation);
        }

        return stagingAutomata;
    }

    /**
     * Staging automaton to animation model mapper
     * @param stagingAutomata
     * @return
     */
    @Override
    public Automata stagingAutomataToAutomata(StagingAutomata stagingAutomata) {
        logger.trace("Converting staging automaton to animation model");
        ArrayList<IPushDownNode> nodes = new ArrayList<IPushDownNode>();
        HashMap<String, IPushDownNode> nodeHashMap = new HashMap<String, IPushDownNode>();

        for(StagingNode stagingNode : stagingAutomata.values())
        {
            PushDownNode pushDownNode = new PushDownNode(stagingNode.getId(), stagingNode.getLabel(),
                    stagingNode.getIsStartNode(), stagingNode.getIsFinalNode(), stagingNode.getX(),
                    stagingNode.getY());
            pushDownNode.setIsNodeDown(stagingNode.getIsNodeDown());
            nodeHashMap.put(pushDownNode.getId(), pushDownNode);
        }

        ArrayList<ITransition> transitions = new ArrayList<ITransition>();

        for(StagingNode stagingNode : stagingAutomata.values())
        {
            transitions.clear();
            if(stagingNode.getStagingRelationList() != null)
                for(StagingRelation stagingRelation : stagingNode.values())
                {
                    if(stagingRelation.getStagingTransitionList() != null)
                        for(StagingTransition stagingTransition : stagingRelation.values())
                        {
                            ITransition transition = new Transition(stagingTransition.getTransitionChar(),
                                    new StackIOString(stagingTransition.getPushString()),
                                    stagingTransition.getPopChar(),
                                    nodeHashMap.get(stagingRelation.getToNodeId()),
                                    nodeHashMap.get(stagingRelation.getFromNodeId()));
                            transitions.add(transition);
                        }
                }
            nodeHashMap.get(stagingNode.getId())
                    .AddTransitions(transitions.toArray(new ITransition[transitions.size()]));
        }

        for(IPushDownNode node : nodeHashMap.values())
            nodes.add(node);

        try {
            return new Automata(stagingAutomata.getIsLMachine(), nodes.toArray(new IPushDownNode[nodes.size()]));
        } catch (IOException e) {
            DialogueUtils.warningMessage("This automata has no start node");
        }
        return null;
    }

    /**
     *  Staging Automaton to drawling list mapper
     * @param stagingAutomata
     * @return
     */
    @Override
    public IDrawingLists stagingAutomataToDrawingLists(StagingAutomata stagingAutomata) {
        logger.trace("Converting staging automaton to drawing model");
        IDrawingLists IDrawingLists = new DrawingLists();

        IDrawingLists.setIsLMachine(stagingAutomata.getIsLMachine());

        for(StagingNode stagingNode : stagingAutomata.values())
        {
            DrawingNode drawingNode = new DrawingNode(stagingNode.getX(),stagingNode.getY(), stagingNode.getId(),
                    stagingNode.getLabel(), stagingNode.getIsStartNode(),
                    stagingNode.getIsFinalNode(), stagingNode.getIsNodeDown(), false);

            IDrawingLists.getNodes().put(drawingNode.getId(), drawingNode);
        }

        for(StagingNode stagingNode : stagingAutomata.values())
        {
            if(stagingNode.getStagingRelationList() != null)
            {
                for(StagingRelation stagingRelation : stagingNode.values())
                {
                    ArrayList<EditTransitions> editTransitions = new ArrayList<EditTransitions>();
                    if(stagingRelation.getStagingTransitionList() != null)
                    {
                        for(StagingTransition stagingTransition : stagingRelation.values())
                        {
                            EditTransitions editTransition = new EditTransitions(stagingTransition
                                    .getTransitionChar().equals('\u0000')
                                    ? '\u03B5' : stagingTransition.getTransitionChar(),
                                    stagingTransition.getPushString().equals("") ? "" + '\u03B5'
                                            : stagingTransition.getPushString(),
                                    stagingTransition.getPopChar().equals('\u0000')
                                            ? '\u03B5' : stagingTransition.getPopChar());
                            editTransitions.add(editTransition);
                        }
                    }
                    ArrayList<INode> nodeArrayList = new ArrayList<INode>();
                    nodeArrayList.add((INode) IDrawingLists.getNodes().get(stagingRelation.getToNodeId()));
                    Relation relation = new Relation((INode) IDrawingLists.getNodes()
                            .get(stagingRelation.getFromNodeId()),
                            nodeArrayList,
                            editTransitions);
                    IDrawingLists.getRelations().put(relation.getId(), relation);
                }
            }
        }

        return IDrawingLists;
    }
}

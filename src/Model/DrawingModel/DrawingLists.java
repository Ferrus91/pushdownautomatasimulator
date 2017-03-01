package Model.DrawingModel;

import CycleDectectorAdapter.NewRelationCycleAdapter;
import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import DependencyInjection.XStreamerModule;
import EventObjects.*;
import Model.AutomataType;
import Model.ILink;
import Model.INode;
import ReflexiveStrategy.ReflexiveStrategy;
import Utils.DialogueUtils;
import XStreamStaging.ISerialize;
import XStreamStaging.IStagingLoader;
import XStreamStaging.StagingAutomata;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 04/08/13
 * Time: 21:06
 * To change this template use File | Settings | File Templates.
 */
public class DrawingLists implements IDrawingLists {
    static Logger logger = Logger.getLogger(DrawingLists.class);

    @Inject
    private IEventBusServer eventBus;
    @Inject
    ISerialize serialize;
    @Inject
    IStagingLoader stagingLoader;

    @Getter
    @Setter
    Boolean isLMachine = false;
    @Getter
    @Setter
    SimpleIntegerProperty  canvasSize = new SimpleIntegerProperty();
    @Getter
    @Setter
    ConcurrentHashMap<String, DrawingNode> nodes = new ConcurrentHashMap<String,DrawingNode>();
    @Getter
    @Setter
    ConcurrentHashMap<String, Relation> relations = new ConcurrentHashMap<String, Relation>();
    @Getter
    Boolean isDirtyFile = false;
    @Getter
    Integer automataId = null;


    private static final ReflexiveStrategy REFLEXIVE_STRATEGY = new ReflexiveStrategy();

    public DrawingLists()
    {
        canvasSize.set(1);

        Injector injector = Guice.createInjector(new EventBusServerModule(this), new XStreamerModule());
        injector.injectMembers(this);
    }

    /**
     * controller sets when model is 'dirty' - i.e. it has been changes or edited in some way
     * @param dirtyFileEvent
     */
    @Subscribe
    public void handleDirtyFileEvent(DirtyFileEvent dirtyFileEvent)
    {
        logger.trace("Setting dirty file");
        isDirtyFile = true;
        automataId = dirtyFileEvent.getId();
    }

    /**
     * redraws view based on model state
     * @param requestPaneRedraw
     */
    @Subscribe
    public void handleRequestPaneRedrawEvent(RequestPaneRedrawEvent requestPaneRedraw)
    {
        logger.trace("Redraw says model!");
        eventBus.post(new CurrentDrawEvent(this));
    }

    /**
     * adds a node when requested by the controller
     * @param nodeCreationEvent
     */
    @Subscribe
    public void handleNodeCreationEvent(NodeCreationEvent nodeCreationEvent)
    {
        String newId = UUID.randomUUID().toString();
        addNode(new DrawingNode(nodeCreationEvent.getX(),nodeCreationEvent.getY(), newId));
        logger.trace("Model added with id: " + newId);
        eventBus.post(new RequestNodeDetailsEvent(newId, ""));
    }


    /**
     * adds a relation in the model when requested by the controller
     * @param connectionAddEvent
     */
    @Subscribe
    public void handleConnectionAddEvent(ConnectionAddEvent connectionAddEvent)
    {
        logger.trace("Adding connection");
        for(Relation relation: relations.values())
        {
            if(relation.getTransitionsList().size() == 0)
                relations.remove(relation.getId());
        }
        ArrayList<INode> toNodeList = new ArrayList<INode>();
        toNodeList.add(nodes.get(connectionAddEvent.getToNodeId()));
        addRelation(new Relation(nodes.get(connectionAddEvent.getFromNodeId()),
                toNodeList, new ArrayList<EditTransitions>()), connectionAddEvent);
    }

    /**
     * adds a self connect to a node in the model when requested by the controller
     * @param selfConnectionAddEvent
     */

    @Subscribe
    public void handleSelfConnectionAddEvent(SelfConnectionAddEvent selfConnectionAddEvent)
    {
        logger.trace("Adding self-connection");
        ArrayList<INode> toNodeList = new ArrayList<INode>();
        toNodeList.add(nodes.get(selfConnectionAddEvent.getIdValue()));
        addRelation(new Relation(nodes.get(selfConnectionAddEvent.getIdValue()),
                toNodeList, new ArrayList<EditTransitions>()), selfConnectionAddEvent);
        eventBus.post(new CurrentDrawEvent(this));
    }

    /**
     * handles an edited node, updating its details in the model
     * @param editNodeEvent
     */

    @Subscribe
    public void handleEditNodeEvent(EditNodeEvent editNodeEvent)
    {
        logger.trace("Editing id:" + editNodeEvent.getId());
        INode node = nodes.get(editNodeEvent.getId());
        if(editNodeEvent.getIsStart())
        {
            resetStartNode();
            node.setIsStartNode(editNodeEvent.getIsStart());
        }
        node.setLabel(editNodeEvent.getLabel());
        node.setIsFinalNode(editNodeEvent.getIsFinal());
        node.setIsNodeDown(editNodeEvent.getIsLoopDown());
        eventBus.post(new CurrentDrawEvent(this));
    }

    /**
     * gets rid of all selections
     * @param deselectAllEvent
     */

    @Subscribe
    public void handleDeselectAllEvent(DeselectAllEvent deselectAllEvent)
    {
       selectInit("");
    }

    /**
     * moves a node in the model in response to a drag event
     * @param dragIdEvent
     */

    @Subscribe
    public void handleDragIdEvent(DragIdEvent dragIdEvent)
    {
        if(nodes.containsKey(dragIdEvent.getSelectId()))
        {
            DrawingNode drawingNode = nodes.get(dragIdEvent.getSelectId());
            drawingNode.setX(dragIdEvent.getX());
            drawingNode.setY(dragIdEvent.getY());
        }
        eventBus.post(new CurrentDrawEvent(this));
    }

    @Subscribe
    public void handleEditConnectionEvent(EditConnectionEvent editConnectionEvent)
    {
        logger.trace("Editing " + editConnectionEvent.getId());
        if(relations.containsKey(editConnectionEvent.getId()) && editConnectionEvent.getConnections().size() == 0)
            eventBus.post(new DeleteByIdEvent(editConnectionEvent.getId()));
        else
        {
            if(NewRelationCycleAdapter.checkForNullCycle(this, editConnectionEvent))
            {
                DialogueUtils.warningMessage("Cycle detected, can't add relation");
                eventBus.post(new DeleteByIdEvent(editConnectionEvent.getId()));
            }
            else
            {
                if(checkDuplicatesFromNode(editConnectionEvent))
                {
                    DialogueUtils.warningMessage("Can't have transitions with same character from same node");
                    eventBus.post(new DeleteByIdEvent(editConnectionEvent.getId()));
                }
                else
                {
                    if(relations.containsKey(editConnectionEvent.getId()))
                {
                    Relation relation = relations.get(editConnectionEvent.getId());
                    relation.getTransitionsList().clear();
                    Iterator<EditTransitions> iteratorRelations = editConnectionEvent.getConnections().iterator();
                    while(iteratorRelations.hasNext())
                    {
                        EditTransitions editTransition = iteratorRelations.next();
                        relation.getTransitionsList().add(editTransition);
                    }
                    uniqTransitionslist(relation.getTransitionsList());
                }
                }
            }
        }
        eventBus.post(new CurrentDrawEvent(this));
    }

    @Subscribe
    public void handleSetAutomataDBDetailsEvent(SetAutomataDBDetailsEvent setAutomataDBDetailsEvent)
    {
        isDirtyFile = true;
        automataId = setAutomataDBDetailsEvent.getNewAutomataId();
    }

    private boolean checkDuplicatesFromNode(EditConnectionEvent editConnectionEvent) {
        logger.trace("Checking the duplicates");
        for(Relation relation : relations.values())
        {
            if(relation.getFromNode().getId().equals(editConnectionEvent.getId().substring(0,36))
                    && !relation.getId().equals(editConnectionEvent.getId()))
            {
                for(EditTransitions editTransitions : relation.getTransitionsList())
                {
                    for(EditTransitions editTransitionsEvent : editConnectionEvent.getConnections())
                    {
                        if(!editTransitionsEvent.getTransitionChar().equals('\u03B5') &&
                                (editTransitions.getTransitionChar().equals(editTransitionsEvent.getTransitionChar())
                                && editTransitions.getPopChar().equals(editTransitionsEvent.getPopChar())))
                        {
                            logger.error("Duplicate transition added!");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * method which encapsulates the process of removing duplicate transition definitions
     * before they are saved in the model
     * @param transitionsList
     */
    private void uniqTransitionslist(List<EditTransitions> transitionsList) {
        // inspired by http://stackoverflow.com/questions/203984/how-do-i-remove-repeated-elements-from-arraylist
        // with Linked Hash Set to retain order
        logger.trace("Uniquing transitions");
        HashSet<EditTransitions> temp = new LinkedHashSet<EditTransitions>();
        temp.addAll(transitionsList);
        transitionsList.clear();
        transitionsList.addAll(temp);
    }

    /**
     * Takes a reflexice drawing event, that is one of its subclasses, and processes it through
     * reflection to find the relevant method
     * @param reflexiveIdDrawingEvent
     */
    @Subscribe
    public void handleReflexiveIdDrawingEvent(ReflexiveIdDrawingEvent reflexiveIdDrawingEvent)
    {
        idReflect(reflexiveIdDrawingEvent.getId(), reflexiveIdDrawingEvent.methodName());
        eventBus.post(new CurrentDrawEvent(this));
    }

    /**
     * saves node, deserialises it and passes it to database
     * @param saveEvent
     */
    @Subscribe
    public void handleSaveEvent(SaveEvent saveEvent)
    {
        logger.trace("Saving");
        if(hasOneStartNode())
        {
            StagingAutomata stagingAutomata = stagingLoader.drawingListToStagingAutomata(this);
            String xml = serialize.serialize(stagingAutomata);

            if(isDirtyFile)
            {
                logger.trace("File is dirty!");
                eventBus.post(new UpdateAutomataXMLEvent(automataId, xml));
                if(!saveEvent.isClose()
                    && DialogueUtils.choiceBox("Do you want to reload the Automata?"))
                    eventBus.post(new ReloadAutomataEvent(xml, automataId));
                if(saveEvent.isClose())
                    Platform.exit();
            }
            else
            {
                logger.trace("New file!");
                eventBus.post(new InsertAutomataEvent(xml, saveEvent.isClose()));
            }
        }
        else
            {
                logger.error("No start node!");
                DialogueUtils.warningMessage("Save failed: Automata must have one and exactly one start node");
                if(saveEvent.isClose())
                {
                    Platform.exit();
                }

            }
    }

    /**
     * before save can commence a check that there is only one start node is performed
     * @return
     */
    private boolean hasOneStartNode() {
        Integer counter = 0;
        for(DrawingNode drawingNode : nodes.values())
        {
            if(drawingNode.getIsStartNode())
                counter++;
        }

        logger.trace("Found " + counter + "start node(s)");
        return counter == 1;
    }

    /**
     * clears model
     * @param clearDrawingScreenEvent
     */
    @Subscribe
    public void handleClearDrawingScreenEvent(ClearDrawingScreenEvent clearDrawingScreenEvent)
    {
        logger.trace("Clearing model");
        isDirtyFile = false;
            automataId = null;
            nodes.clear();
            relations.clear();
            eventBus.post(new ClosePanesEvent());
            eventBus.post(new CurrentDrawEvent(this));
    }

    /**
     * changes the automata
     * @param changeAutomataType
     */
    @Subscribe
    public void handleChangeAutomataType(ChangeAutomataType changeAutomataType)
    {
        logger.trace("Automaton type changed in model");
        if(changeAutomataType.getMachineType() == AutomataType.LMACHINE)
            isLMachine = true;
        else
            isLMachine = false;
    }

    /**
     * handles deletion by ID
     * @param deleteIfSelectedEvent
     */
    @Subscribe
    public void handleDeleteIfSelectedEvent(DeleteIfSelectedEvent deleteIfSelectedEvent)
    {
        logger.trace("Deleting by id on selection");
        String id = selectedId();
        idReflect(id, "delete");
        eventBus.post(new CurrentDrawEvent(this));
    }

    /**
     * provides record of
     * resize event by storing the value in the model so it can be reloaded when persisted
     * @param canvasResizeEvent
     */
    @Subscribe
    public void handleCanvasResizeEvent(CanvasResizeEvent canvasResizeEvent)
    {
        logger.trace("Resize the canvas");
        canvasSize.set(canvasResizeEvent.getCanvasSize());
    }

    /**
     * restes the start node
     */
    private void resetStartNode() {
        logger.trace("Resetting start node");
        for(INode node : nodes.values())
        {
            node.setIsStartNode(false);
        }
    }


    /**
     * resets any selection
     * @param id
     */
    private void selectInit(String id) {
        logger.trace("Resetting start node inside model");
        for(DrawingNode node : nodes.values())
        {
            node.setIsSelected(false);
        }
        for(Relation relation : relations.values())
        {
            relation.setIsSelected(false);
        }
    }

    /**
     * finds node to be selected by id
     * @param id
     * @param hashMap
     */
    private void selectNode(String id, ConcurrentHashMap hashMap)
    {
        logger.trace("Select a node id: " + id);
        DrawingNode node = nodes.get(id);
        node.setIsSelected(true);
        ArrayList<EditTransitions> editTransitions = loopTransitions(id, true);
        eventBus.post(new OpenNodeAccordionEvent(node, editTransitions));
    }

    /**
     * find relation to be selected by id
     * @param id
     * @param hashMap
     */
    private void selectRelation(String id, ConcurrentHashMap hashMap)
    {
        logger.trace("Select a relation id: " + id);
        Relation relation = relations.get(id);
        relation.setIsSelected(true);
        ArrayList<EditTransitions> editTransitions = loopTransitions(id, false);
        eventBus.post(new OpenRelationAccordionEvent(relation, editTransitions));
    }

    /**
     * finds node to be deleted by id
     * @param id
     * @param hashMap
     */
    private void deleteNode(String id, ConcurrentHashMap hashMap)
    {
        logger.trace("Delete node by id: " + id);
        boolean deleteNode = true;
        if(isNodeRelated(id))
           deleteNode = DialogueUtils.choiceBox("This node has connections, are you sure you want to delete?");
        if(deleteNode)
        {
            nodes.remove(id);
            deleteRelatedNodesById(id);
            eventBus.post(new ClosePanesEvent());
        }
    }

    /**
     * finds relation to be deleted by id
     * @param id
     * @param hashMap
     */
    private void deleteRelation(String id, ConcurrentHashMap hashMap)
    {
        logger.trace("Delete relation by id: " + id);
        relations.remove(id);
        eventBus.post(new ClosePanesEvent());
    }

    /**
     * gets model item by id before passing it on to the method selected by refelction
     * @param id
     * @param methodName
     */
    private void idReflect(String id, String methodName)
    {
        if(REFLEXIVE_STRATEGY.hasMethod(methodName+"Init"))
            REFLEXIVE_STRATEGY.callBack(id, methodName+"Init", this);

        for(INode node : nodes.values())
        {
            if(node.getId().equals(id))
            {
                logger.trace("Node found for reflection");
                REFLEXIVE_STRATEGY.callBack(id, methodName + "Node", nodes, this);
                return;
            }
        }

        for(ILink link : relations.values())
        {
            String compositeId = link.getFromNode().getId() + "-" +
                    link.getToNode().get(0).getId();
            if(compositeId.equals(id))
            {
                logger.trace("Relation found for reflection");
                REFLEXIVE_STRATEGY.callBack(id, methodName + "Relation", relations, this);
                return;
            }
        }
    }

    /**
     * adds a relation between two events when requested by the controller
     * @param relation
     * @param connectionAddEvent
     */
    private void addRelation(Relation relation, ConnectionAddEvent connectionAddEvent) {
        logger.trace("Relation between two nodes added: " + "relation.fromNode.getId() + \"-\" + relation.getToNode().get(0).getId()" +
                "");
        if(!relations.containsKey(relation.fromNode.getId() + "-" + relation.getToNode().get(0).getId()))
        {
            relations.put(relation.fromNode.getId() + "-" + relation.getToNode().get(0).getId(), relation);
            eventBus.post(new RequestConnectionDetailsEvent(connectionAddEvent.getFromNodeId() + "-" +
                    connectionAddEvent.getToNodeId(),
                    relation.fromNode.getLabel(), relation.toNode.get(0).getLabel()));
            eventBus.post(new CurrentDrawEvent(this));
        }
        else
        {
            logger.error("Duplicate relation");
            DialogueUtils.warningMessage("Connection between these nodes already added");
        }
     }

    /**
     * adds a self-relation when requested by the controller
     * @param relation
     * @param selfConnectionAddEvent
     */
    private void addRelation(Relation relation, SelfConnectionAddEvent selfConnectionAddEvent) {
        logger.trace("Adding self relation id: " + relation.fromNode.getId() + "-"
                + relation.getToNode().get(0).getId());
        if(!relations.containsKey(relation.fromNode.getId() + "-" + relation.getToNode().get(0).getId()))
        {
            relations.put(relation.fromNode.getId() + "-" + relation.getToNode().get(0).getId(), relation);
        }
        else
        {
            relations.remove(relation.fromNode.getId() + "-" + relation.getToNode().get(0).getId());
            relations.put(relation.fromNode.getId() + "-" + relation.getToNode().get(0).getId(), relation);
        }
    }

    /**
     * adds a node when requested to by the controller
     * @param drawingNode
     */
    @Override
    public void addNode(DrawingNode drawingNode)
    {
        logger.trace("Add node");
        nodes.put(drawingNode.getId(), drawingNode);
        eventBus.post(new CurrentDrawEvent(this));
    }

    /**
     * filters out transitive loops with no id
     * @param id
     * @param isSelfLoop
     * @return
     */
    private ArrayList<EditTransitions> loopTransitions(String id, Boolean isSelfLoop) {
        logger.trace("Getting relations with id");
        ArrayList<EditTransitions> retval = new ArrayList<EditTransitions>();
        for(Relation relation : relations.values())
        {
           if (isSelfLoop ? isLoopWithId(relation, id) : isTransWithId(relation, id))
               retval.addAll(relation.getTransitionsList());
        }
        return retval;
    }

    /**
     * checks if loop is a self-loop based on its id concatenation
     * @param relation
     * @param id
     * @return
     */
    private Boolean isLoopWithId(Relation relation, String id)
    {
        return relation.getFromNode().getId().equals(relation.getToNode().get(0).getId())
                && relation.getFromNode().getId().equals(id);
    }

    /**
     * checks equality on relations between nodes
     * @param relation
     * @param id
     * @return
     */
    private Boolean isTransWithId(Relation relation, String id)
    {
        return (relation.getFromNode().getId() + "-" + relation.getToNode().get(0).getId()).equals(id);
    }

    public void deregisterEventBus()
    {
        eventBus.unregister(this);
    }

    /**
     * checks to see if node is part of this relation
     * @param id
     * @return
     */
    private boolean isNodeRelated(String id) {
        logger.trace("Getting linked nodes for id: " + id);
        for(Relation relation : relations.values())
        {
            if(relation.getId().contains(id))
                return true;
        }
        return false;
    }

    /**
     * deletes those relations which are related the deleted node id
     * @param id
     */
    private void deleteRelatedNodesById(String id) {
        logger.trace("Deleting node: " + id);
        for(Relation relation : relations.values())
        {
            if(relation.getFromNode().getId().equals(id)
                    || relation.getToNode().get(0).getId().equals(id))
                relations.remove(relation.getId());
        }
    }

    /**
     * gets the id of the selected node
     * @return
     */
    private String selectedId() {
        logger.trace("Get selected object id");
        for(DrawingNode node : nodes.values())
        {
            if(node.getIsSelected())
                return node.getId();
        }
        for(Relation relation : relations.values())
        {
            if(relation.getIsSelected())
                return relation.getId();
        }
        return null;
    }
}
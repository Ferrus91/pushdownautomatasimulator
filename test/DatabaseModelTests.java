import EventObjects.ConnectionAddEvent;
import EventObjects.EditConnectionEvent;
import Model.AnimationModel.*;
import Model.DrawingModel.*;
import Utils.StackIOString;
import XStreamStaging.*;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 11/09/13
 * Time: 03:51
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseModelTests {
    private static String xml;
    private static String xmlFail;
    private static StagingAutomata stagingAutomata;
    private static Serialize serialize;
    private static StagingLoader stagingLoader;

    @BeforeClass
    public static void oneTimeSetUp() {
        try {
            xml =
                    CharStreams.toString(new InputStreamReader(DatabaseModelTests
                            .class.getResourceAsStream("pdatest.xml"),
                             Charsets.UTF_8));
            xmlFail =
                    CharStreams.toString(new InputStreamReader(DatabaseModelTests
                            .class.getResourceAsStream("pdafailref.xml"),
                            Charsets.UTF_8));

             serialize = new Serialize();
            stagingAutomata = serialize.deserialize(xml);
            stagingLoader = new StagingLoader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        @Test
        public void testStagingLoad()
        {
            assertTrue(!stagingAutomata.getIsLMachine());
            StagingNode stagingNode = stagingAutomata.getStagingNodeList().get("9bea7ca6-e1e1-4e30-9b78-f2f62f337c39");
            assertTrue(stagingNode.getIsNodeDown());
            StagingRelation stagingRelation = stagingNode.getStagingRelationList().get("4ecf664f-d696-4345-9c70-19cc7db5ce26-9bea7ca6-e1e1-4e30-9b78-f2f62f337c39");
            assertEquals(stagingRelation.getToNodeId(), "4ecf664f-d696-4345-9c70-19cc7db5ce26");
            StagingTransition stagingTransition = stagingRelation.getStagingTransitionList().get("");
            assertEquals(stagingTransition.getKey(),"");
        }

    @Test(expected=IllegalStateException.class)
    public void testValidationFail()
    {
        serialize.deserialize(xmlFail);
    }

    @Test
    public void testAutomatonLoad() throws TransitionException {
        Automata automaton = stagingLoader.stagingAutomataToAutomata(stagingAutomata);
        assertTrue(!automaton.getIsLMachine());
        automaton.performTransition('a');
        IPushDownNode node = automaton.get(2);
        assertEquals(node.getId(), "9bea7ca6-e1e1-4e30-9b78-f2f62f337c39");
        assertEquals(node.getLabel(), "a");
        ITransition transition = node.getEpsilonTransitions('\u0000').get(0);
        assertEquals(transition.getTransitionRelation().toString(),"\u0000,\u0000");
        automaton = stagingLoader.stagingAutomataToAutomata(stagingAutomata);
        IPushDownStack stack = automaton.getCurrInstanteneousDescription().getCurrStack();
        stack.push(new StackIOString("b"));
        assertEquals(node.getTransition('b', stack).getTransitionRelation().toString(), "b,b");
        automaton.performTransition('b');
        assertEquals(stack.fullContents(), "\u0000bcd");
    }

    @Test
    public void testDrawingLoad()
    {
        IDrawingLists drawingLists = stagingLoader.stagingAutomataToDrawingLists(stagingAutomata);
        assertTrue(drawingLists.getNodes().contains("9bea7ca6-e1e1-4e30-9b78-f2f62f337c39"));
        assertTrue(!drawingLists.getIsLMachine());
        DrawingNode drawingNode  = drawingLists.getNodes().get("9bea7ca6-e1e1-4e30-9b78-f2f62f337c39");
        assertEquals(drawingNode.getLabel(),"a");
        assertTrue(    drawingLists.getNodes()
                .contains("9bea7ca6-e1e1-4e30-9b78-f2f62f337c39-9bea7ca6-e1e1-4e30-9b78-f2f62f337c39"));
        Relation relation = drawingLists.getRelations()
                .get("9bea7ca6-e1e1-4e30-9b78-f2f62f337c39-9bea7ca6-e1e1-4e30-9b78-f2f62f337c39");
        EditTransitions transitionList = relation.getTransitionsList().get(0);
        assertEquals(transitionList.toString(), "a\\ε\\abc");
    }

    @Test
    public void testDrawingSave()
    {
        DrawingLists drawingList = new DrawingLists();
        drawingList.addNode(new DrawingNode(0.0,0.0,"6c407e4a-4d65-40f5-837b-ef9e41b67bca"));
        drawingList.addNode(new DrawingNode(0.0, 0.0, "8b18f262-d2b9-40ac-8052-8d882f8b5362"));
        ObservableList<EditTransitions> editList1 = FXCollections.observableArrayList();
        editList1.add(new EditTransitions('a', "f1", '3'));
        editList1.add(new EditTransitions('b', "f1", '3'));
        ObservableList<EditTransitions> editList2 = FXCollections.observableArrayList();
        editList2.add(new EditTransitions('b',"f1",'3'));
        drawingList.handleConnectionAddEvent(new ConnectionAddEvent("6c407e4a-4d65-40f5-837b-ef9e41b67bca",
                "8b18f262-d2b9-40ac-8052-8d882f8b5362"));
        drawingList.handleEditConnectionEvent(new
                EditConnectionEvent("6c407e4a-4d65-40f5-837b-ef9e41b67bca-8b18f262-d2b9-40ac-8052-8d882f8b5362"
                , editList1));
        drawingList.handleConnectionAddEvent(new ConnectionAddEvent("8b18f262-d2b9-40ac-8052-8d882f8b5362",
                "6c407e4a-4d65-40f5-837b-ef9e41b67bca"));
        drawingList.handleEditConnectionEvent(new
                EditConnectionEvent("8b18f262-d2b9-40ac-8052-8d882f8b5362-6c407e4a-4d65-40f5-837b-ef9e41b67bca"
                , editList2));
        StagingAutomata newStagingAutomata = stagingLoader.drawingListToStagingAutomata(drawingList);

        StagingNode node = newStagingAutomata.get("6c407e4a-4d65-40f5-837b-ef9e41b67bca");
        StagingRelation relation = node.getStagingRelationList()
                .get("8b18f262-d2b9-40ac-8052-8d882f8b5362");
        StagingTransition transition = relation.get("bf13");
        assertTrue(!node.getIsNodeDown());
        assertEquals(relation.size(),2);
        assertTrue(transition.getTransitionChar().equals('b'));
    }
}

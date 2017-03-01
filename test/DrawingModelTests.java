import EventObjects.*;
import Model.DrawingModel.DrawingLists;
import Model.DrawingModel.DrawingNode;
import Model.DrawingModel.EditTransitions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 11/09/13
 * Time: 03:51
 * To change this template use File | Settings | File Templates.
 */
public class DrawingModelTests {
    @Test
    public void testAddNode(){
        DrawingLists drawingList = new DrawingLists();
        drawingList.addNode(new DrawingNode(0.0,0.0,"id1"));
        assertEquals(drawingList.getNodes().get("id1").getId(), "id1");
    }

    @Test
    public void testAddSelfRelation(){
        DrawingLists drawingList = new DrawingLists();
        drawingList.addNode(new DrawingNode(0.0,0.0,"id1"));
        ObservableList<EditTransitions> editList = FXCollections.observableArrayList();
        editList.add(new EditTransitions('c',"d",'d'));
        editList.add(new EditTransitions('e',"f",'t'));
                drawingList.handleSelfConnectionAddEvent(new SelfConnectionAddEvent("id1", editList));
        assertEquals(drawingList.getRelations().get("id1-id1").getId(),"id1-id1");
    }

    @Test
    public void testAddTransRelation(){
        DrawingLists drawingList = new DrawingLists();
        drawingList.addNode(new DrawingNode(0.0,0.0,"id1"));
        drawingList.addNode(new DrawingNode(0.0,0.0,"id2"));
        ObservableList<EditTransitions> editList = FXCollections.observableArrayList();
        editList.add(new EditTransitions('c',"d",'d'));
        editList.add(new EditTransitions('e',"f",'t'));
        drawingList.handleConnectionAddEvent(new ConnectionAddEvent("id1", "id2"));
        assertEquals(drawingList.getRelations().get("id1-id2").getId(),"id1-id2");
    }

    @Test
    public void testDeleteNode(){
        DrawingLists drawingList = new DrawingLists();
        drawingList.addNode(new DrawingNode(0.0,0.0,"id1"));
        ObservableList<EditTransitions> editList = FXCollections.observableArrayList();
        editList.add(new EditTransitions('c', "d", 'd'));
        drawingList.handleReflexiveIdDrawingEvent(new DeleteByIdEvent("id1"));
        assertTrue(!drawingList.getNodes().contains("id1"));
    }

    @Test
    public void testDeleteRelation(){
        DrawingLists drawingList = new DrawingLists();
        drawingList.addNode(new DrawingNode(0.0,0.0,"id1"));
        drawingList.addNode(new DrawingNode(0.0,0.0,"id2"));
        ObservableList<EditTransitions> editList = FXCollections.observableArrayList();
        editList.add(new EditTransitions('c',"d",'d'));
        editList.add(new EditTransitions('e',"f",'t'));
        drawingList.handleConnectionAddEvent(new ConnectionAddEvent("id1","id2"));
        drawingList.handleReflexiveIdDrawingEvent(new DeleteByIdEvent("id1-id2"));
        assertTrue(!drawingList.getRelations().contains("id1-id2"));
    }

    @Test
    public void testSelectNode(){
        DrawingLists drawingList = new DrawingLists();
        drawingList.addNode(new DrawingNode(0.0,0.0,"id1"));
        drawingList.addNode(new DrawingNode(0.0,0.0,"id2"));
        ObservableList<EditTransitions> editList = FXCollections.observableArrayList();
        editList.add(new EditTransitions('c',"d",'d'));
        editList.add(new EditTransitions('e',"f",'t'));
        drawingList.handleReflexiveIdDrawingEvent(new SelectIdEvent("id1"));
        assertTrue(drawingList.getNodes().get("id1").getIsSelected());
    }

    @Test
    public void testSelectRelation(){
        DrawingLists drawingList = new DrawingLists();
        drawingList.addNode(new DrawingNode(0.0,0.0,"id1"));
        drawingList.addNode(new DrawingNode(0.0,0.0,"id2"));
        ObservableList<EditTransitions> editList = FXCollections.observableArrayList();
        editList.add(new EditTransitions('c',"d",'d'));
        editList.add(new EditTransitions('e', "f", 't'));
        drawingList.handleConnectionAddEvent(new ConnectionAddEvent("id1","id2"));
        drawingList.handleReflexiveIdDrawingEvent(new SelectIdEvent("id1-id2"));
        assertTrue(drawingList.getRelations().get("id1-id2").getIsSelected());
    }

    @Test
    public void testDuplicateRelation(){
        DrawingLists drawingList = new DrawingLists();
        drawingList.addNode(new DrawingNode(0.0,0.0,"id1"));
        drawingList.addNode(new DrawingNode(0.0,0.0,"id2"));
        ObservableList<EditTransitions> editList = FXCollections.observableArrayList();
        editList.add(new EditTransitions('c',"d",'d'));
        editList.add(new EditTransitions('e',"f",'t'));
        drawingList.handleConnectionAddEvent(new ConnectionAddEvent("id1", "id2"));
        drawingList.handleConnectionAddEvent(new ConnectionAddEvent("id1", "id2"));
        assertEquals(drawingList.getRelations().size(), 1);
    }

    @Test
    public void testEditRelation(){
        DrawingLists drawingList = new DrawingLists();
        drawingList.addNode(new DrawingNode(0.0,0.0,"6c407e4a-4d65-40f5-837b-ef9e41b67bca"));
        drawingList.addNode(new DrawingNode(0.0,0.0,"8b18f262-d2b9-40ac-8052-8d882f8b5362"));
        ObservableList<EditTransitions> editList = FXCollections.observableArrayList();
        editList.add(new EditTransitions('c',"d",'d'));
        editList.add(new EditTransitions('e', "f", 't'));
        drawingList.handleConnectionAddEvent(new ConnectionAddEvent("6c407e4a-4d65-40f5-837b-ef9e41b67bca",
                "8b18f262-d2b9-40ac-8052-8d882f8b5362"));
        editList.clear();
        editList.add(new EditTransitions('3', "f1", '3'));
        drawingList.handleEditConnectionEvent(new
                EditConnectionEvent("6c407e4a-4d65-40f5-837b-ef9e41b67bca-8b18f262-d2b9-40ac-8052-8d882f8b5362", editList));
        assertEquals(drawingList.getRelations().size(), 1);
        assertTrue(drawingList.getRelations()
                .get("6c407e4a-4d65-40f5-837b-ef9e41b67bca-8b18f262-d2b9-40ac-8052-8d882f8b5362")
                .getTransitionsList().get(0).getPopChar().equals('3'));
    }

    @Test(expected=IllegalStateException.class)
    public void testEditCycle(){
        DrawingLists drawingList = new DrawingLists();
        drawingList.addNode(new DrawingNode(0.0,0.0,"6c407e4a-4d65-40f5-837b-ef9e41b67bca"));
        drawingList.addNode(new DrawingNode(0.0,0.0,"8b18f262-d2b9-40ac-8052-8d882f8b5362"));
        ObservableList<EditTransitions> editList1 = FXCollections.observableArrayList();
        editList1.add(new EditTransitions('ε', "f1", '3'));
        ObservableList<EditTransitions> editList2 = FXCollections.observableArrayList();
        editList2.add(new EditTransitions('ε',"f1",'3'));
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
    }

    @Test
    public void testUniqTransitions(){
        DrawingLists drawingList = new DrawingLists();
        drawingList.addNode(new DrawingNode(0.0,0.0,"6c407e4a-4d65-40f5-837b-ef9e41b67bca"));
        drawingList.addNode(new DrawingNode(0.0,0.0,"8b18f262-d2b9-40ac-8052-8d882f8b5362"));
        ObservableList<EditTransitions> editList1 = FXCollections.observableArrayList();
        editList1.add(new EditTransitions('ε', "ε", 'ε'));
        editList1.add(new EditTransitions('ε', "ε", 'ε'));
        drawingList.handleConnectionAddEvent(new ConnectionAddEvent("6c407e4a-4d65-40f5-837b-ef9e41b67bca",
                "8b18f262-d2b9-40ac-8052-8d882f8b5362"));
        drawingList.handleEditConnectionEvent(new
                EditConnectionEvent("6c407e4a-4d65-40f5-837b-ef9e41b67bca-8b18f262-d2b9-40ac-8052-8d882f8b5362"
                , editList1));
        assertEquals(drawingList.getRelations()
                .get("6c407e4a-4d65-40f5-837b-ef9e41b67bca-8b18f262-d2b9-40ac-8052-8d882f8b5362")
                .getTransitionsList().size(), 1);
    }

    @Test(expected=IllegalStateException.class)
    public void testTooFewStartNodes()
    {
        DrawingLists drawingList = new DrawingLists();
        drawingList.addNode(new DrawingNode(0.0,0.0,"6c407e4a-4d65-40f5-837b-ef9e41b67bca"));
        drawingList.addNode(new DrawingNode(0.0, 0.0, "8b18f262-d2b9-40ac-8052-8d882f8b5362"));
        drawingList.handleSaveEvent(new SaveEvent(false));
    }

    @Test(expected=IllegalStateException.class)
    public void testManyStartNodes()
    {
        DrawingLists drawingList = new DrawingLists();
        DrawingNode node1 = new DrawingNode(0.0,0.0,"6c407e4a-4d65-40f5-837b-ef9e41b67bca");
        DrawingNode node2 = new DrawingNode(0.0, 0.0, "8b18f262-d2b9-40ac-8052-8d882f8b5362");
        node1.setIsStartNode(true);
        node2.setIsStartNode(true);
        drawingList.addNode(node1);
        drawingList.addNode(node2);
        drawingList.handleSaveEvent(new SaveEvent(false));
    }

    @Test
    public void testSameCharError()
    {
        DrawingLists drawingList = new DrawingLists();
        DrawingNode node1 = new DrawingNode(0.0,0.0,"6c407e4a-4d65-40f5-837b-ef9e41b67bca");
        DrawingNode node2 = new DrawingNode(0.0, 0.0, "8b18f262-d2b9-40ac-8052-8d882f8b5362");
        node1.setIsStartNode(true);
        drawingList.addNode(node1);
        drawingList.addNode(node2);
        drawingList.handleSaveEvent(new SaveEvent(false));
    }
}


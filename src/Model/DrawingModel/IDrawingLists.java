package Model.DrawingModel;

import Model.IAutomataModel;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 06/09/13
 * Time: 21:41
 * To change this template use File | Settings | File Templates.
 */
public interface IDrawingLists extends IAutomataModel {
    void addNode(DrawingNode drawingNode);
    void setIsLMachine(Boolean isLMachine);
    SimpleIntegerProperty getCanvasSize();
    void setCanvasSize(javafx.beans.property.SimpleIntegerProperty canvasSize);
    ConcurrentHashMap<String, DrawingNode> getNodes();
    void setNodes(java.util.concurrent.ConcurrentHashMap<String, DrawingNode> nodes);
    ConcurrentHashMap<String, Relation> getRelations();
    void setRelations(java.util.concurrent.ConcurrentHashMap<String, Relation> relations);
    Boolean getIsDirtyFile();
    Integer getAutomataId();
}

package Model.DrawingModel;

import Model.INode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 04/08/13
 * Time: 21:07
 * To change this template use File | Settings | File Templates.
 */
@AllArgsConstructor
public class DrawingNode implements INode, SelectableElement {
    static Logger logger = Logger.getLogger(DrawingNode.class);

    @Getter
    @Setter
    Integer x;
    @Getter
    @Setter
    Integer y;
    @Getter
    String id;
    @Getter
    @Setter
    String label;
    @Getter
    @Setter
    Boolean isStartNode;
    @Getter
    @Setter
    Boolean isFinalNode;
    @Getter
    @Setter
    Boolean isNodeDown;
    @Getter
    @Setter
    Boolean isSelected;

        public DrawingNode(Double x, Double y, String id)
        {
        this.x = x.intValue();
        this.y = y.intValue();
        this.id = id;
        this.label = "";
        this.isFinalNode = false;
        this.isStartNode = false;
        this.isNodeDown = false;
        this.isSelected = false;
    }
}

package XStreamStaging;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.Delegate;
import lombok.Getter;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 19/08/13
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
@XStreamAlias("Node")
public class StagingNode {
    static Logger logger = Logger.getLogger(StagingNode.class);
    @Getter
    @XStreamAsAttribute
    Integer x;
    @Getter
    @XStreamAsAttribute
    Integer y;
    @Getter
    @XStreamAsAttribute
    String id;
    @Getter
    @XStreamAsAttribute
    String label;
    @Getter
    @XStreamAsAttribute
    Boolean isStartNode;
    @Getter
    @XStreamAsAttribute
    Boolean isFinalNode;
    @Getter
    @XStreamAsAttribute
    Boolean isNodeDown;
    @Getter
    @XStreamOmitField
    Boolean isSelected;
    @Delegate
    @Getter
    @XStreamImplicit(itemFieldName ="Relation", keyFieldName = "key")
    Map<String, StagingRelation> stagingRelationList = new HashMap<String, StagingRelation>();

    public StagingNode(Integer x, Integer y, String id, String label, Boolean startNode, Boolean finalNode,
                       Boolean nodeDown, Boolean selected, Map<String, StagingRelation> stagingRelationList) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.label = label;
        isStartNode = startNode;
        isFinalNode = finalNode;
        isNodeDown = nodeDown;
        isSelected = selected;
        this.stagingRelationList = stagingRelationList;
        logger.trace("Node constructed for id: " + id);
    }
}

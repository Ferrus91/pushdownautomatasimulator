package XStreamStaging;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Delegate;
import lombok.Getter;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 19/08/13
 * Time: 14:55
 * To change this template use File | Settings | File Templates.
 */
@XStreamAlias("Relation")
public class StagingRelation {
    static Logger logger = Logger.getLogger(StagingRelation.class);

    @Getter
    @XStreamAsAttribute
    String key;
    @Getter
    @XStreamAsAttribute
    String fromNodeId;
    @Getter
    @XStreamAsAttribute
    String toNodeId;
    @Getter
    @Delegate
    @XStreamImplicit(itemFieldName ="Transition", keyFieldName = "key")
    Map<String, StagingTransition> stagingTransitionList;

    public StagingRelation(String fromNodeId, String toNodeId, Map<String, StagingTransition> transitions)
    {
        logger.trace("Setting up staging relation: " + fromNodeId + " to " + toNodeId);
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
        this.stagingTransitionList = transitions;
        key = toNodeId + "-" + fromNodeId;
    }
}
package XStreamStaging;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.AllArgsConstructor;
import lombok.Delegate;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor @NoArgsConstructor
@XStreamAlias("Automata")
public class StagingAutomata {
    static Logger logger = Logger.getLogger(StagingAutomata.class);

    @Getter
    @Delegate
    @XStreamImplicit(itemFieldName ="Node", keyFieldName = "id")
    Map<String, StagingNode> stagingNodeList = new HashMap<String, StagingNode>();
    @Getter
    @XStreamAsAttribute
    Integer canvasMultiply;
    @Getter
    @XStreamAsAttribute
    Boolean isLMachine;

    public StagingAutomata(Boolean isLMachine, Integer canvasMultiply)
    {
        logger.trace("Building staging automaton");
        this.isLMachine = isLMachine;
        this.canvasMultiply = canvasMultiply;
    }
}

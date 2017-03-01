package EventObjects;

import Model.AnimationModel.IInstantaneousDescription;
import Model.AnimationModel.NodeList;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 29/06/13
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class AutomataChangeEvent implements EventObject {
    static Logger logger = Logger.getLogger(AutomataChangeEvent.class);
    @Override
    public EventBusInstance getInstanceToken() {
        return EventBusInstance.DRAWING;
    }

    @Getter
    NodeList nodeList;
    @Getter
    IInstantaneousDescription currInstanteneousDescription;
    @Getter @Setter
    Boolean isNewAutomata;
    @Getter
    public String processedString;
    @Getter
    public String unProcessedString;

    public AutomataChangeEvent(NodeList nodeList, IInstantaneousDescription currInstanteneousDescription,
                               Boolean isNewAutomata, String processedString, String unProcessedString)
    {
        logger.trace(this.getClass().getSimpleName() + " fired! With new automaton? " + isNewAutomata
                + " processed " + processedString + " processing " + unProcessedString + " with stack top " +
        currInstanteneousDescription.getCurrStack().fullContents() + " and current node "
                + currInstanteneousDescription.getCurrNode().getId());
        this.nodeList = nodeList;
        this.currInstanteneousDescription = currInstanteneousDescription;
        this.isNewAutomata = isNewAutomata;
        this.processedString = processedString;
        this.unProcessedString = unProcessedString;
    }
}

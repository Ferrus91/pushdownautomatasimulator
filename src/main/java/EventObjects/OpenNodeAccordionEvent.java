package EventObjects;

import Model.DrawingModel.DrawingNode;
import Model.DrawingModel.EditTransitions;
import lombok.Getter;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 19/08/13
 * Time: 22:57
 * To change this template use File | Settings | File Templates.
 */
public class OpenNodeAccordionEvent implements EventObject {
    static Logger logger = Logger.getLogger(OpenNodeAccordionEvent.class);

    @Getter
    private final DrawingNode node;
    @Getter
    private final ArrayList<EditTransitions> editTransitions;

    public OpenNodeAccordionEvent(DrawingNode node,ArrayList<EditTransitions> editTransitions) {
        this.node = node;
        this.editTransitions = editTransitions;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! On node: " + node.getId());
        return EventBusInstance.DRAWING;
    }
}

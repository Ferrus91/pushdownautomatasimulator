package EventObjects;

import Model.DrawingModel.EditTransitions;
import Model.DrawingModel.Relation;
import lombok.Getter;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 19/08/13
 * Time: 22:58
 * To change this template use File | Settings | File Templates.
 */
public class OpenRelationAccordionEvent implements EventObject {
    static Logger logger = Logger.getLogger(OpenRelationAccordionEvent.class);

    @Getter
    private final Relation relation;
    @Getter
    private final ArrayList<EditTransitions> editTransitions;

    public OpenRelationAccordionEvent(Relation relation, ArrayList<EditTransitions> editTransitions) {
        this.relation = relation;
        this.editTransitions = editTransitions;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! On relation: " + relation.getId());
        return EventBusInstance.DRAWING;
    }
}

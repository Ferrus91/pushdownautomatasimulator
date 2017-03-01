package EventObjects;

import Database.TreeNodeObject;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 25/08/13
 * Time: 20:37
 * To change this template use File | Settings | File Templates.
 */
public class DeleteAutomataEvent implements EventObject {
    static Logger logger = Logger.getLogger(DeleteAutomataEvent.class);

    @Getter
    private TreeNodeObject deleteAutomata;

    public DeleteAutomataEvent(TreeNodeObject deleteAutomata) {
        this.deleteAutomata = deleteAutomata;
    }

    @Override
    public EventBusInstance getInstanceToken() {
        logger.trace(this.getClass().getSimpleName() + " fired! With deletion automaton: "
                + deleteAutomata.getName());
        return EventBusInstance.DATABASE;
    }
}

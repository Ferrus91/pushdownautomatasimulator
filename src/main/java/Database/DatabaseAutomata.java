package Database;

import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/08/13
 * Time: 22:57
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseAutomata extends TreeNodeObject {
    static Logger logger = Logger.getLogger(DatabaseAutomata.class);

    @Getter
    private Integer directoryId;

    public DatabaseAutomata(Integer id, String name, Integer directoryId) {
        super(id, name);
        this.directoryId = directoryId;
    }

    @Override
    public TreeNodeType getTreeNodeType() {
        return TreeNodeType.AUTOMATA;
    }
}

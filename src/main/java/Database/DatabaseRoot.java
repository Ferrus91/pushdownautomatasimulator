package Database;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/08/13
 * Time: 22:59
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseRoot extends TreeNodeObject {
    static Logger logger = Logger.getLogger(DatabaseRoot.class);

    public DatabaseRoot(String name) {
        super(0, name);
    }

    @Override
    public TreeNodeType getTreeNodeType() {
        return TreeNodeType.ROOT;
    }
}

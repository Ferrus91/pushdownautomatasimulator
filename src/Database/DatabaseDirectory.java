package Database;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/08/13
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseDirectory extends TreeNodeObject {
    static Logger logger = Logger.getLogger(TreeNodeObject.class);

    public DatabaseDirectory(Integer id, String name) {
        super(id, name);
    }

    @Override
    public TreeNodeType getTreeNodeType() {
        return TreeNodeType.DIRECTORY;
    }
}

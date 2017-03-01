package Database;

import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/08/13
 * Time: 22:54
 * To change this template use File | Settings | File Templates.
 */
public abstract class TreeNodeObject {
    static Logger logger = Logger.getLogger(TreeNodeObject.class);
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private String name;

    public TreeNodeObject(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public abstract TreeNodeType getTreeNodeType();
}

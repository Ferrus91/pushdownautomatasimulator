package DrawingTemplate;

import Controller.ControllerLogic.NodeConnections;
import Model.AnimationModel.ITransition;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 14/08/13
 * Time: 13:02
 * To change this template use File | Settings | File Templates.
 */
public interface IArrowMapBuilder {
    public HashMap<ITransition, NodeConnections> build(List<ITransition> transition);
}

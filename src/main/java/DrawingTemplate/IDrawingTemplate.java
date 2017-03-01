package DrawingTemplate;

import Controller.ControllerLogic.FullDisplayBox;
import Controller.ControllerLogic.NodeConnections;
import Model.AnimationModel.IInstantaneousDescription;
import Model.AnimationModel.ITransition;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 05/07/13
 * Time: 04:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class IDrawingTemplate extends GUITemplateAbstractClass<IInstantaneousDescription,
        NodeConnections, FullDisplayBox> {
        public abstract void setLastSelectedTransition(ITransition lastSelectedTransition);
}

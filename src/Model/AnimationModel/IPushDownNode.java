package Model.AnimationModel;

import Model.INode;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 25/06/13
 * Time: 21:07
 * To change this template use File | Settings | File Templates.
 */
public interface IPushDownNode extends INode
{
        public void AddTransitions(ITransition... transitions);
        public ITransition getTransition(Character symbol, IPushDownStack stack) throws TransitionException;
        public List<ITransition> getAllNodeTransitions();
        public List<ITransition> getEpsilonTransitions(Character popSymbol);
}

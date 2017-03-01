package Model.AnimationModel;

import Model.INode;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 25/06/13
 * Time: 21:06
 * To change this template use File | Settings | File Templates.
 */
public interface IInstantaneousDescription {
    public IPushDownNode getCurrNode();
    public IPushDownStack getCurrStack();
    public ITransition getLastSelectedTransition();
    public void resetLastSelectedTransition();
    public void shiftInstantaneousDescription(Character symbol) throws TransitionException;
    public Boolean successState();
    public Boolean isFinalNode();
    public List<ITransition> getEpisilonTransitions(Character popSymbol);
    public void setUpEpsilonJump(IPushDownStack currInstanteneousDescription, INode jump);
    void performEpsilonJump(ITransition epsilonTransition);
}

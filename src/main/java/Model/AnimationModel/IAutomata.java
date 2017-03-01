package Model.AnimationModel;

import Model.IAutomataModel;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 25/06/13
 * Time: 21:03
 * To change this template use File | Settings | File Templates.
 */
public interface IAutomata extends IAutomataModel {
    public void performTransition(Character transitionSymbol) throws TransitionException;
    public Boolean hasSucceeded();
    public IAutomata deepCopy(IAutomata iAutomata);
    public void setUpEpisilonJump(ITransition epsilonTransition, String processedInput,
                                  String unprocessedInput, IPushDownStack currInstanteneousDescription);
    public IPushDownNode getNodeById(String id);
    public void performEpsilonJump(ITransition epsilonTransition);
    public void setAutomata(IAutomata pop);
    public IInstantaneousDescription getCurrInstanteneousDescription();
    public String getUnprocessedInput();
    public String getProcessedInput();
    public boolean hasNonDeterministicJumps();
    public void  popAutomata();
}

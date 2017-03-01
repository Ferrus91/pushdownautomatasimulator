package Model.AnimationModel;

import Model.ILink;
import Utils.StackIOString;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 25/06/13
 * Time: 21:11
 * To change this template use File | Settings | File Templates.
 */
public interface ITransition extends ILink {
    public Character getSymbolOfTransition();
    public Character getOffStack();
    public StackIOString getOnStack();
    public TransitionRelation getTransitionRelation();
    boolean epsilonPop();
    boolean epsilonPush();

    boolean epsilonRelation();
}

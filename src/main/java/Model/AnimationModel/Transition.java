package Model.AnimationModel;

import Model.INode;
import Utils.StackIOString;
import lombok.Delegate;
import lombok.Getter;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Transition implements ITransition {
    static Logger logger = Logger.getLogger(Transition.class);

    @Getter @Delegate
    private IPushDownNode fromNode;
    @Getter
    private TransitionRelation transitionRelation;
	@Getter
    private StackIOString onStack;
	private IPushDownNode toNode;

	public Character getSymbolOfTransition() {
		return transitionRelation.getTransitionChar();
	}

	public Character getOffStack() {
		return transitionRelation.getTransitionStackTop();
	}

    @Override
    public boolean epsilonRelation() {
        return transitionRelation.getTransitionChar().equals('\0');
    }

    @Override
    public boolean epsilonPop() {
        return transitionRelation.getTransitionStackTop().equals('\0');
    }

    @Override
    public boolean epsilonPush() {
        return onStack.equals(new StackIOString(""));
    }

    public Transition(Character symbolOfTransition, StackIOString onStack, Character offStack, IPushDownNode toNode, IPushDownNode fromNode)
	{
		transitionRelation = new TransitionRelation(symbolOfTransition, offStack);
		this.toNode = toNode;
		this.onStack = onStack;
        this.fromNode = fromNode;
	}

	public List<INode> getToNode() {
        List<INode> returnList = new ArrayList<INode>();
        returnList.add(toNode);
        return returnList;
	}

    @Override
    public boolean equals(Object obj) {
        return !(this == null || obj == null) && obj.toString().equals(this.toString());
    }

    @Override
    public int hashCode()
    {
        return (fromNode.toString() + toNode.toString() ).hashCode();
    }

    @Override
    public String toString() {
        return fromNode.toString() + toNode.toString();
    }
}

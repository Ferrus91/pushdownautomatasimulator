package Model.AnimationModel;

import Model.INode;
import lombok.Getter;
import org.apache.log4j.Logger;

import java.util.List;

public class InstantaneousDescription implements IInstantaneousDescription{
    static Logger logger = Logger.getLogger(InstantaneousDescription.class);

    @Getter
    IPushDownNode currNode;
    @Getter
    IPushDownStack currStack;
	@Getter
    ITransition lastSelectedTransition;

	public InstantaneousDescription(IPushDownNode currNode)
	{
		this.currNode = currNode;
		currStack = new PushDownStack();
	}

    /**
     * Processes string
     * @param symbol - current symbol in string to process
     * @throws TransitionException
     */

	public void shiftInstantaneousDescription(Character symbol) throws TransitionException
	{
		logger.trace("Shift in ID");

        ITransition selectedTransition = currNode.getTransition(symbol, currStack);

        lastSelectedTransition = selectedTransition;

        if(!selectedTransition.epsilonPop())
		    currStack.pop();
		if(!selectedTransition.epsilonPush())
            currStack.push(selectedTransition.getOnStack());
		currNode = (IPushDownNode) selectedTransition.getToNode().get(0);
	}

    /**
     *
     * @return whether the current string is empty
     */
    @Override
    public Boolean successState() {
        return currStack.empty();
    }

    /**
     *
     * @return is current node final?
     */

    @Override
    public Boolean isFinalNode() {
        logger.trace("Finding out if current node is final node");
        return currNode.getIsFinalNode();
    }

    /**
     *
     * @return the list of transitions specified by epsilon entries
     * @param popSymbol
     */

    @Override
    public List<ITransition> getEpisilonTransitions(Character popSymbol) {
        logger.trace("Getting epsilons");
        return currNode.getEpsilonTransitions(popSymbol);
    }

    /**
     * Sets up the current instantaneous description with the jumped values
     * @param pushDownStack
     * @param jump
     */

    @Override
    public void setUpEpsilonJump(IPushDownStack pushDownStack, INode jump) {
         this.currNode = (IPushDownNode)jump;
         this.currStack = pushDownStack;
    }

    @Override
    public void performEpsilonJump(ITransition epsilonTransition) {
        logger.trace("Epsilon jumping");
        this.currNode = (IPushDownNode)epsilonTransition.getToNode().get(0);
        if(!epsilonTransition.getOffStack().equals('\u0000'))
            this.currStack.pop();
        this.currStack.push(epsilonTransition.getOnStack());
    }

    /**
     * resets the last transition
     */

    @Override
    public void resetLastSelectedTransition() {
        logger.trace("Setting up blink in model");
        lastSelectedTransition = null;
    }
}

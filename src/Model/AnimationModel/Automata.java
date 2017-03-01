package Model.AnimationModel;

import DependencyInjection.EventBusServer.EventBusServerModule;
import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.*;
import Model.INode;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

public class Automata implements IAutomata {
    static Logger logger = Logger.getLogger(Automata.class);

    private IEventBusServer eventBus;

    private Stack<IAutomata> automataStack = new Stack<IAutomata>();
    @Getter
    @Setter
    private Boolean isLMachine;
    private IPushDownNode startNode;
    @Getter
    private IInstantaneousDescription currInstanteneousDescription;
    @Getter
    @Delegate
    private NodeList nodeList;
    @Getter @Setter
    private String unprocessedInput = "";
    @Getter @Setter
    private String processedInput = "";
    @Getter
    private String preProcessedInput;
    @Getter
    private String preUnprocessedInput;

    public Automata() {
        logger.trace("Initialising animation model");
        this.isLMachine = false;

        Injector injector = Guice.createInjector(new EventBusServerModule(this));
        injector.injectMembers(this);

        nodeList = new NodeList();
    }

    @Inject
    public void setEventBus(IEventBusServer eventBus)
    {
        this.eventBus = eventBus;
    }

    public Automata(Boolean isLMachine, IPushDownNode... pushDownNodes) throws IOException
    {

        logger.trace("Initialising animation model with nodes");
        this.isLMachine = isLMachine;

        Injector injector = Guice.createInjector(new EventBusServerModule(this));
        injector.injectMembers(this);

        nodeList = new NodeList();

        for(IPushDownNode iPushDownNode : pushDownNodes)
        {
            nodeList.add(iPushDownNode);
        }

        currInstanteneousDescription = new InstantaneousDescription(getStartNode());

        newAutomataChangeEvent();
    }

    /**
     *
     * @param automata
     * @return the same automata in a different object with all nodes and transitions set as before.
     */

    @Override
    public IAutomata deepCopy(IAutomata automata) {
        try {
            logger.trace("Deep copying automata model");
            IAutomata newCopy = new Automata(isLMachine,
                    nodeList.toArray(new PushDownNode[nodeList.size()]));
            newCopy.deregisterEventBus();
            return newCopy;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * sets processed string back to start
     */

    public void resetAutomata()
    {
        logger.trace("Resetting automaton");
        unprocessedInput = "";
        processedInput = "";
        try {
            automataStack.clear();
            currInstanteneousDescription = new InstantaneousDescription(getStartNode());
            newAutomataChangeEvent();
        } catch (IOException e) {
            logger.error("No start node");
            e.printStackTrace();
        }
    }

    /**
     * resets string
     * @param event - notification that a new string is to be processed
     */

    @Subscribe
    public void handleNewStringEvent(NewStringEvent event) {
        logger.trace("Automaton has new string");
        resetAutomata();
        this.setUnprocessedInput(event.getNewString());
        newAutomataChangeEvent();
    }

    /**
     * resets string
     * @param event - notification that a new string is to be processed
     */

    @Subscribe
    public void handleResetAnimationAutomataEvent(ResetAnimationAutomataEvent event) {
        resetAutomata();
        newAutomataChangeEvent();
    }

    /**
     * notification that the automata should transition
     * @param event
     */

    @Subscribe
    public void handleNextTransitionEvent(NextTransitionEvent event){
        if(unprocessedInput.length() > 0)
        {
            Character charToTransition = unprocessedInput.charAt(0);
            processStringChar(charToTransition);
            try {
                this.performTransition(charToTransition);
                if(unprocessedInput.length() == 0)
                {
                    if(!hasNonDeterministicJumps() || hasSucceeded())
                    {
                        logger.trace("Automaton detects completion");
                        transitionAutomataChangeEvent();
                        if(!hasSucceeded() && hasNonDeterministicJumps())
                            for(IAutomata automata : automataStack)
                                if(automata.hasSucceeded())
                                {
                                    eventBus.post(new PDACompleteEvent(automata.hasSucceeded(), processedInput));
                                    return;
                                }
                        eventBus.post(new PDACompleteEvent(hasSucceeded(), processedInput));
                    }
                    else
                    {
                        logger.trace("Automaton processes next item");
                        popAutomata();
                        if(!hasSucceeded())
                            eventBus.post(new NonDeterministicJumpEvent(currInstanteneousDescription.getCurrNode().getLabel(),
                                    currInstanteneousDescription.getCurrStack().fullContents(), processedInput,
                                    " because the automata is in a fail state."));
                        eventBus.post(new AutomataChangeEvent(this.getNodeList(),
                                this.getCurrInstanteneousDescription(), false,
                                this.getProcessedInput(), this.getUnprocessedInput()));
                    }
                }
            } catch (TransitionException e) {
                logger.error("Wrong char and stack combination!");
                eventBus.post(new TransitionErrorEvent(e.getMessage()));
                this.resetAutomata();
            }
        }
    }

    private void processStringChar(Character charToTransition) {
        preUnprocessedInput = unprocessedInput;
        preProcessedInput = processedInput;
        unprocessedInput = unprocessedInput.substring(1);
        processedInput += charToTransition;
    }

    /**
     * copy old fields into new
     * @param pop popped off automaton
     */

    public void setAutomata(IAutomata pop) {
        logger.trace("Nondeterminsitic load");
        currInstanteneousDescription = pop.getCurrInstanteneousDescription();
        processedInput = pop.getProcessedInput();
        unprocessedInput = pop.getUnprocessedInput();
    }

    /**
     * posts a new automata to be drawn
     */

    public void newAutomataChangeEvent() {
        logger.trace("Change event on automaton model");
        eventBus.post(new AutomataChangeEvent(getNodeList(), getCurrInstanteneousDescription(), true,
                getProcessedInput(), getUnprocessedInput()));
    }

    /**
     * posts an existing automata to be drawn
     */

    public void transitionAutomataChangeEvent() {
        logger.trace("Automaton changes on transition");
        eventBus.post(new AutomataChangeEvent(getNodeList(), getCurrInstanteneousDescription(), false,
                getProcessedInput(), getUnprocessedInput()));
    }

    /**
     *
     * @return returns automata's start node
     * @throws IOException
     */

    private IPushDownNode getStartNode() throws IOException {
        logger.trace("Finding start node");
        for(IPushDownNode pushDown : nodeList)
        {
            if(pushDown.getIsStartNode())
                return pushDown;
        }
        throw new IOException("No start node");
    }

    /**
     * checks pop char to see if there is a valid epsilon jump
     * @param epsilonTransition
     * @return
     */

    public Boolean isValidEpsilon(ITransition epsilonTransition)
    {
        return epsilonTransition.getOffStack().equals(currInstanteneousDescription.getCurrStack().peek())
                || epsilonTransition.getOffStack().equals('\u0000');
    }

    /**
     * updates the epsilon list with new automata that can be jumped to
     * @param processedInput
     * @param unprocessedInput
     */

    public void updateEpsilons(String processedInput, String unprocessedInput)
    {
        List<ITransition> nonDeterministicJumps = currInstanteneousDescription.getEpisilonTransitions(
                currInstanteneousDescription.getCurrStack().peek());

        for(ITransition epsilonTransition : nonDeterministicJumps)
        {
            if(isValidEpsilon(epsilonTransition))
            {
                jumpStack(epsilonTransition, processedInput, unprocessedInput);
            }
        }
    }

    /**
     *
     * @param transitionSymbol - current symbol in the string to process
     * @throws TransitionException
     */

    public void performTransition(Character transitionSymbol) throws TransitionException {
        try {
            logger.trace("Shift instantaneous description");
            updateEpsilons(preProcessedInput, preUnprocessedInput);
            currInstanteneousDescription.shiftInstantaneousDescription(transitionSymbol);
            if(unprocessedInput.length() == 0)
                updateEpsilons(processedInput, unprocessedInput);
        } catch (TransitionException e) {
            logger.trace("Transition error on char/stack combination");
            if(automataStack.size() > 0)
            {
                logger.trace("Found alternative nondeterministic paths");
                popAutomata();
                eventBus.post(new NonDeterministicJumpEvent(currInstanteneousDescription.getCurrNode().getLabel(),
                        currInstanteneousDescription.getCurrStack().fullContents(), processedInput, " because "
                        + e.getMessage().toLowerCase()));
                eventBus.post(new AutomataChangeEvent(this.getNodeList(),
                        this.getCurrInstanteneousDescription(), false,
                        this.getProcessedInput(), this.getUnprocessedInput()));
            }
            else
                throw new TransitionException(e.getFailedChar(),e.getFailedStack(),e.getLabel());
        }
        transitionAutomataChangeEvent();
    }

    /**
     * create the copies to push onto the stack
     * @param epsilonTransition
     * @param processedInput
     * @param unprocessedInput
     */

    private void jumpStack(ITransition epsilonTransition, String processedInput, String unprocessedInput) {
        logger.trace("Finding epsilon transitions");
        IAutomata jumpAutomata = this.deepCopy(this);
        jumpAutomata.setUpEpisilonJump(epsilonTransition, processedInput,
                unprocessedInput, this.currInstanteneousDescription.getCurrStack());
        jumpAutomata.performEpsilonJump(epsilonTransition);
        this.addStack(jumpAutomata);
    }

    /**
     * process a newly copied automaton so it matches post-epsilon conditions
     * @param epsilonTransition
     * @param processedInput
     * @param unprocessedInput
     * @param pushDownStack
     */

    public void setUpEpisilonJump(ITransition epsilonTransition, String processedInput, String unprocessedInput,
                                  IPushDownStack pushDownStack)
    {
        logger.trace("Setting up epsilon jump");
        this.currInstanteneousDescription =
                new InstantaneousDescription(this.getCurrInstanteneousDescription().getCurrNode());
        INode jump = epsilonTransition.getToNode().get(0);
        this.currInstanteneousDescription.setUpEpsilonJump(new PushDownStack(pushDownStack), jump);
        this.processedInput = processedInput;
        this.unprocessedInput = unprocessedInput;
    }

    /**
     * get a node by its id
     * @param id
     * @return
     */

    @Override
    public IPushDownNode getNodeById(String id) {
        logger.trace("Finding a node with id: " + id);
        for(IPushDownNode node : nodeList)
        {
            if(node.getId().equals(id))
                return node;
        }
        return null;
    }

    /**
     * trigger epsilon tradition in Instantaneous description
     * @param epsilonTransition
     */

    @Override
    public void performEpsilonJump(ITransition epsilonTransition) {
        logger.trace("Performing epsilon jump");
        currInstanteneousDescription.performEpsilonJump(epsilonTransition);
    }

    /**
     * returns success value
     * @return
     */

    @Override
    public Boolean hasSucceeded() {
        logger.trace("Checking success");
        return isLMachine ? currInstanteneousDescription.isFinalNode() : currInstanteneousDescription.successState();
    }

    /**
     * checks to see if the non-determinism is getting too large for the memory
     * @param automata
     */

    private void addStack(IAutomata automata)
    {
        logger.trace("Adding to stack");
        if(automataStack.size() % 20 == 0
                && automataStack.size() != 0)
            eventBus.post(new StackSizeWarningEvent(automataStack.size()));
        automataStack.add(automata);
    }

    @Override
    public void deregisterEventBus() {
        logger.trace("Deregister from event bus");
        eventBus.unregister(this);
    }

    /**
     * determine Nondeterministic jumps
     * @return
     */

    @Override
    public boolean hasNonDeterministicJumps() {
        logger.trace("Getting ND jumps");
        return automataStack.size() > 0;
    }

    /**
     * retrieve an old epsilon jump and test to see if it is in a success state
     */

    @Override
    public void popAutomata() {
        logger.trace("Popping automaton");
        setAutomata(automataStack.pop());
        if(hasSucceeded() && unprocessedInput.length() == 0)
            eventBus.post(new PDACompleteEvent(hasSucceeded(), processedInput));
    }
}

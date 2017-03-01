package Model.AnimationModel;

import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PushDownNode implements IPushDownNode {
    static Logger logger = Logger.getLogger(PushDownNode.class);

    @Getter
	private String id;
    @Getter
    @Setter
    String label;
    @Getter
    @Setter
    private Boolean isFinalNode;
    @Getter
    @Setter
    private Boolean isStartNode;
    @Getter
    @Setter
    private Integer x;
    @Getter
    @Setter
    private Integer y;
    @Getter
    @Setter
    Boolean isNodeDown;
    private HashMap<TransitionRelation, ITransition> transitionMap;
    private ArrayList<ITransition> epsilonTransitions = new ArrayList<ITransition>();

    public PushDownNode(String id, String label, Boolean isStartNode, Boolean isFinalNode, Integer x, Integer y)
    {
        transitionMap = new HashMap<TransitionRelation, ITransition>();
        this.id = id;
        this.label = label;
        this.isStartNode = isStartNode;
        this.isFinalNode = isFinalNode;
        this.x = x;
        this.y = y;
        this.isNodeDown = false;
    }

    /**
     *
     * @return list of all transitions
     */
    public List<ITransition> getAllNodeTransitions()
    {
       logger.trace("Getting all transitions");
        List<ITransition> returnArray = new ArrayList<ITransition>();
        for(ITransition transition : transitionMap.values()) {
            returnArray.add(transition);
        }
        for(ITransition transition : epsilonTransitions)
        {
            returnArray.add(transition);
        }
        return returnArray;
    }

    /**
     *
     * @return  returns epsilon transitions
     * @param popSymbol
     */
    @Override
    public List<ITransition> getEpsilonTransitions(Character popSymbol) {
        logger.trace("Getting epsilon transitions");
        List<ITransition> matchingEpsilons = new ArrayList<ITransition>();

        for(ITransition transition : epsilonTransitions)
        {
            if(transition.getOffStack().equals(popSymbol)
                    || transition.getOffStack().equals('\u0000'))
                matchingEpsilons.add(transition);
        }

        return matchingEpsilons;
    }

    /**
     * add a transition to the node
        * @param transitions
            */

        public void AddTransitions(ITransition... transitions)
        {
           logger.trace("Adding transitions");
            for(ITransition addTransition : transitions)
            {
                if(addTransition.getFromNode() == this)
                    if(addTransition.getSymbolOfTransition().equals('\u0000'))
                        epsilonTransitions.add(addTransition);
                    else
                        transitionMap.put(addTransition.getTransitionRelation(), addTransition);
            }
        }

        /**
         *
         * @param symbol
         * @param stack
         * @return get nodes transitions
         * @throws TransitionException
         */

    public ITransition getTransition(Character symbol, IPushDownStack stack) throws TransitionException
    {
        logger.trace("Getting transitions");
        Character stackSymbol = stack.peek();
        TransitionRelation trSearch = new TransitionRelation();
        trSearch.setTransitionChar(symbol);
        trSearch.setTransitionStackTop(stackSymbol);

		if(searchKey(trSearch))
			return fetchKey(trSearch);
        else
        {
            trSearch.setTransitionStackTop('\0');
            if(searchKey(trSearch))
                return fetchKey(trSearch);
        }
		throw new TransitionException(symbol, stackSymbol, this.getLabel());
	}

	private Boolean searchKey(TransitionRelation trSearch){
        logger.trace("Searching for " + trSearch);
        return transitionMap.containsKey(trSearch);
	}

	private ITransition fetchKey(TransitionRelation key)
	{
		logger.trace("Fetching " + key);
        return transitionMap.get(key);
	}

}
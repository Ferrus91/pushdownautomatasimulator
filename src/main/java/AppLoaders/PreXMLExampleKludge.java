package AppLoaders;

import Model.AnimationModel.Automata;
import Model.AnimationModel.IPushDownNode;
import Model.AnimationModel.PushDownNode;
import Model.AnimationModel.Transition;
import Utils.StackIOString;
import org.apache.log4j.Logger;

import java.io.IOException;

public class PreXMLExampleKludge {
    static Logger logger = Logger.getLogger(PreXMLExampleKludge.class);

    /**
     *
     * @return Builder pattern class for testing purporses. Constructs a test automata
     * @throws IOException
     */
	public Automata buildAutomata() throws IOException {
		logger.trace("Creating automaton");
        IPushDownNode startNode = new PushDownNode("1","q", true, false, 200, 300);
        IPushDownNode zeroNode = new PushDownNode("2", "s", false, false, 50, 150);
        IPushDownNode oneNode = new PushDownNode("3", "t", false, true, 300, 200);
		
		Transition start1 = new Transition('0',new StackIOString("$"),'\0',zeroNode, startNode);
		Transition start2 = new Transition('1',new StackIOString("$"),'\0',oneNode, startNode);
        Transition start3 = new Transition('2',new StackIOString("dd"),'\0',zeroNode, startNode);

        startNode.AddTransitions(start1, start2, start3);
		
		Transition zeroNode1 = new Transition('0',new StackIOString("0"),'\0',zeroNode, zeroNode);
		Transition zeroNode2 = new Transition('1',new StackIOString(""),'0',zeroNode, zeroNode);
		Transition zeroNode3 = new Transition('1',new StackIOString(""),'$',startNode, zeroNode);
		
		zeroNode.AddTransitions(zeroNode1, zeroNode2, zeroNode3);

        zeroNode.setIsNodeDown(true);

		Transition oneNode1 = new Transition('1',new StackIOString("1"),'\0',oneNode, oneNode);
		Transition oneNode2 = new Transition('0',new StackIOString(""),'1',oneNode, oneNode);
		Transition oneNode3 = new Transition('0',new StackIOString(""),'$',startNode, oneNode);
		
		oneNode.AddTransitions(oneNode1, oneNode2, oneNode3);

		return new Automata(false, startNode, zeroNode, oneNode);
	}
}

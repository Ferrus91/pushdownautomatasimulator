package AppLoaders;

import Model.AnimationModel.Automata;
import Model.AnimationModel.IPushDownNode;
import Model.AnimationModel.PushDownNode;
import Model.AnimationModel.Transition;
import Utils.StackIOString;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 26/08/13
 * Time: 23:25
 * To change this template use File | Settings | File Templates.
 */
public class PreXMLExampleKludgeNonDeterminism {
    static Logger logger = Logger.getLogger(PreXMLExampleKludge.class);

    /**
     *
     * @return Builder pattern class for testing purporses. Constructs a test automata
     * @throws java.io.IOException
     */
    public Automata buildAutomata() throws IOException {
        logger.trace("Building automaton");
        IPushDownNode startNode = new PushDownNode("1","q", true, false, 200, 300);
        IPushDownNode zeroNode = new PushDownNode("2", "s", false, false, 300, 300);
        IPushDownNode oneNode = new PushDownNode("3", "t", false, true, 400, 300);
        IPushDownNode twoNode = new PushDownNode("4", "v", false, true, 300, 400);
        IPushDownNode threeNode = new PushDownNode("5", "w", false, true, 500, 300);
        IPushDownNode fourNode = new PushDownNode("6", "y", false, true, 600, 300);
        IPushDownNode fiveNode = new PushDownNode("7", "z", false, true, 600, 300);

        Transition start1 = new Transition('\u0000', new StackIOString("$"),'\0', zeroNode, startNode);

        startNode.AddTransitions(start1);

        Transition zeroNode1 = new Transition('\u0000', new StackIOString(""),'\0', oneNode, zeroNode);
        Transition zeroNode2 = new Transition('\u0000', new StackIOString(""),'\0', twoNode, zeroNode);
        Transition zeroNode3 = new Transition('a', new StackIOString("a"),'\0', zeroNode, zeroNode);

        zeroNode.AddTransitions(zeroNode3, zeroNode1, zeroNode2, zeroNode3);

        zeroNode.setIsNodeDown(true);

        Transition one1 = new Transition('\u0000', new StackIOString(""),'$', twoNode, oneNode);
        Transition one2 = new Transition('b', new StackIOString(""),'a', oneNode, oneNode);

        oneNode.AddTransitions(one1, one2);

        Transition two1 = new Transition('c', new StackIOString(""),'\0', twoNode, twoNode);

        twoNode.AddTransitions(two1);

        Transition three1 = new Transition('\u0000', new StackIOString(""),'\0', fourNode, threeNode);
        Transition three2 = new Transition('b', new StackIOString(""),'\0', threeNode, threeNode);

        threeNode.AddTransitions(three1, three2);

        Transition four1 = new Transition('c', new StackIOString(""),'a', fourNode, fourNode);
        Transition four2 = new Transition('\u0000', new StackIOString(""),'$', fiveNode, threeNode);

        fourNode.AddTransitions(four1, four2);

        Automata automata = new Automata(false, startNode, zeroNode, oneNode, twoNode, threeNode, fourNode, fiveNode);

        return automata;
    }
}

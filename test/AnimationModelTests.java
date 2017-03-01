import AppLoaders.PreXMLExampleKludge;
import AppLoaders.PreXMLExampleKludgeNonDeterminism;
import Model.AnimationModel.Automata;
import Model.AnimationModel.TransitionException;
import Utils.StackIOString;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 11/09/13
 * Time: 03:50
 * To change this template use File | Settings | File Templates.
 */
public class AnimationModelTests {
    @Test
    public void successfulAutomataRunTest()
    {
        StackIOString testString = new StackIOString("01010101");
        PreXMLExampleKludge preXMLExampleKludge = new PreXMLExampleKludge();
        try {
        Automata automata = preXMLExampleKludge.buildAutomata();
        for(Character c : testString)
        {
            automata.performTransition(c);
        }
        assertTrue(automata.hasSucceeded());
    } catch (IOException e) {
        assertTrue(false);
    } catch (TransitionException e) {
        assertTrue(false);
    }
}

    @Test
    public void successfulAutomataRunLMachineTest()
    {
        StackIOString testString = new StackIOString("1");
        PreXMLExampleKludge preXMLExampleKludge = new PreXMLExampleKludge();
        try {
            Automata automata = preXMLExampleKludge.buildAutomata();
            automata.setIsLMachine(true);
            for(Character c : testString)
            {
                automata.performTransition(c);
            }
            assertTrue(automata.hasSucceeded());
        } catch (IOException e) {
            assertTrue(false);
        } catch (TransitionException e) {
            assertTrue(false);
        }
    }

        @Test(expected = TransitionException.class)
    public void failedAutomataRunTestBadTransition() throws TransitionException {
        StackIOString testString = new StackIOString("01a010101");
        PreXMLExampleKludge preXMLExampleKludge = new PreXMLExampleKludge();
        try {
            Automata automata = preXMLExampleKludge.buildAutomata();
            for(Character c : testString)
            {
                automata.performTransition(c);
            }
        } catch (IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void failedAutomataRunTest()
    {
        StackIOString testString = new StackIOString("010101010");
        PreXMLExampleKludge preXMLExampleKludge = new PreXMLExampleKludge();
        try {
            Automata automata = preXMLExampleKludge.buildAutomata();
            for(Character c : testString)
            {
                automata.performTransition(c);
            }
            assertTrue(!automata.hasSucceeded());
        } catch (IOException e) {
            assertTrue(false);
        } catch (TransitionException e) {
            assertTrue(false);
        }
    }

    @Test
    public void successfulNonDeterministicAutomataRunTest()
    {
        PreXMLExampleKludgeNonDeterminism preXMLExampleKludgeNonDeterminism = new PreXMLExampleKludgeNonDeterminism();
        try {
            Automata automata = preXMLExampleKludgeNonDeterminism.buildAutomata();
            automata.performTransition('\u0000');
            assertTrue(automata.getCurrInstanteneousDescription().getCurrNode().getId().equals("2"));
        } catch (IOException e) {
            assertTrue(false);
        } catch (TransitionException e) {
            assertTrue(false);
        }
    }

    @Test
    public void successfulNonDeterministicAutomataSuccessStateRunTest()
    {
        PreXMLExampleKludgeNonDeterminism preXMLExampleKludgeNonDeterminism = new PreXMLExampleKludgeNonDeterminism();
        try {
            Automata automata = preXMLExampleKludgeNonDeterminism.buildAutomata();
            for(int i = 1; i < 5; i++)
                automata.performTransition('\u0000');
            assertTrue(automata.hasSucceeded());
        } catch (IOException e) {
            assertTrue(false);
        } catch (TransitionException e) {
            assertTrue(false);
        }
    }
}

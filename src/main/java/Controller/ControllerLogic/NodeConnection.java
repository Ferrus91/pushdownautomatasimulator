package Controller.ControllerLogic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 06/07/13
 * Time: 19:11
 * Class holds a simplfied model of the data for the purposes of the vector graphics, and includes a to-do string which
 * handles the unicode character episolon used to represent null values in the literature.
 */
@AllArgsConstructor
public class NodeConnection {
    static Logger logger = Logger.getLogger(NodeConnection.class);

    @Getter
    private Character transitionChar;
    @Getter
    private Character stackOn;
    @Getter
    private String stackOff;

    @Override
    public String toString() {
        Character denormalisedTransitionChar = transitionChar == '\0' ? '\u03B5' : transitionChar;
        Character denormalisedStackOn = stackOn == '\0' ? '\u03B5' : stackOn;
        String denormalisedStackOff = String.valueOf(stackOff.equals("") || stackOff == null ? '\u03B5' : stackOff);

        logger.trace(denormalisedTransitionChar + "/" + denormalisedStackOn + "/" + denormalisedStackOff + "returned");
        return denormalisedTransitionChar + "/" + denormalisedStackOn + "/" + denormalisedStackOff;
    }
}

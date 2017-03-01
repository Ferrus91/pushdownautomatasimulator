package XStreamStaging;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 19/08/13
 * Time: 14:55
 * To change this template use File | Settings | File Templates.
 */
@XStreamAlias("Transition")
public class StagingTransition {
    static Logger logger = Logger.getLogger(StagingTransition.class);
    @Getter
    @XStreamAsAttribute
    String key;
    @Getter
    @XStreamAsAttribute
    Character transitionChar;
    @Getter
    @XStreamAsAttribute
    String pushString;
    @Getter
    @XStreamAsAttribute
    Character popChar;

    public StagingTransition(Character transitionChar, String pushString, Character popChar)
    {
        logger.trace("Setting up transition");
        this.transitionChar = transitionChar;
        this.pushString = pushString;
        this.popChar = popChar;
        key = "";

        if(!transitionChar.equals('\u0000'))
            key += transitionChar.charValue();
        if(!pushString.equals(""))
            key += pushString;
        if(!popChar.equals('\u0000'))
            key += popChar;
    }
}

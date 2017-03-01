package Model.DrawingModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 15/08/13
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */
@AllArgsConstructor
public class EditTransitions {
    static Logger logger = Logger.getLogger(EditTransitions.class);

    @Getter @Setter
    Character transitionChar;
    @Getter @Setter
    String pushString;
    @Getter @Setter
    Character popChar;

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null)
            return this.toString().equals(obj.toString());
        else
            return false;
    }

    @Override
    public String toString() {
        return transitionChar + "/" + popChar + "/" + pushString;
    }
}

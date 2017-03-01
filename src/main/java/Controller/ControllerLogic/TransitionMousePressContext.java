package Controller.ControllerLogic;

import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 11/08/13
 * Time: 21:43
 * When the use is creating connections this form holds the data needed to draw helper lines to aid the user
 */
public class TransitionMousePressContext {
    static Logger logger = Logger.getLogger(TransitionMousePressContext.class);

    @Getter @Setter
    Double startX;
    @Getter @Setter
    Double startY;
    @Getter @Setter
    Double endX;
    @Getter @Setter
    Double endY;
    @Getter @Setter
    String startUUID;
    @Getter @Setter
    String endUUID;
    @Getter @Setter
    Boolean isActive;


    public TransitionMousePressContext()
    {
        initialise();
    }

    public void reset()
    {
        initialise();
    }

    /**
     * This just sets it back to a basic state for the user
     */

    private void initialise()
    {
        logger.trace("Mouse context initalised");
        isActive = false;
        startX = 0.0;
        startY = 0.0;
        endX = 0.0;
        endY = 0.0;
        startUUID = "";
        endUUID = "";
    }
}

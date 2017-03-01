package Controller.ControllerLogic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 11/07/13
 * Time: 21:36
 * Holds the text and bounding box used to display multiple transitions so the screen is not cluttered
 */

@AllArgsConstructor
public class FullDisplayBox {
    static Logger logger = Logger.getLogger(FullDisplayBox.class);

    @Getter
    private Double topX;
    @Getter
    private Double topY;
    @Getter
    private Double bottomX;
    @Getter
    private Double bottomY;
    @Getter String fullText;
}

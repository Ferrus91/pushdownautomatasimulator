package DrawingTemplate;

import javafx.scene.text.Text;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 16/08/13
 * Time: 11:05
 * To change this template use File | Settings | File Templates.
 */
public class TextBoxBoundsGenerator {
    static Logger logger = Logger.getLogger(TextBoxBoundsGenerator.class);

    @Getter
    Double labelX1;
    @Getter
    Double labelY1;
    @Getter
    Double labelX2;
    @Getter
    Double labelY2;

    TextBoxBoundsGenerator(Text drawText, Double firstX, Double secondX, Double firstY, Double secondY)
    {
        Double textHeight = drawText.getLayoutBounds().getHeight();
        Double textWidth = drawText.getLayoutBounds().getWidth();
        double gradientX = firstX - secondX;
        double gradientY = firstY - secondY;


        if(gradientX > 0)
        {
            if(gradientY > 0)
            {
                labelX1 = secondX - textWidth;
                labelY1 = secondY - textHeight;
            }
            else
            {
                labelX1 = secondX;
                labelY1 = secondY;
            }
        }
        else
        {
            if(gradientY > 0)
            {
                labelX1 = secondX - textWidth/2;
                labelY1 = secondY;
            }
            else
            {
                labelX1 = secondX;
                labelY1 = secondY;
            }
        }
        labelX2 = labelX1 + textWidth;
        labelY2 = labelY1 + textHeight;
        logger.trace("Textbox created between x: " + labelX1 + " y: " + labelY1 + " and x: "
            + labelX2 + " and " + labelY2);
    }
}

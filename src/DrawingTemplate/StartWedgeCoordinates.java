package DrawingTemplate;

import Utils.ViewUtilities;
import lombok.Getter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 15/08/13
 * Time: 21:53
 * To change this template use File | Settings | File Templates.
 */
public class StartWedgeCoordinates {
    static Logger logger = Logger.getLogger(StartWedgeCoordinates.class);

    private static final Integer NODEDIAMETER;
    private static final Double STARTLINELENGTH;
    static
    {
        Properties properties = new Properties();
        try {
            properties.load(ConcreteDrawingTemplate.class.getClassLoader()
                    .getResourceAsStream("Properties/pdaapp.properties"));
        } catch (IOException e) {e.printStackTrace();}
        NODEDIAMETER = Integer.parseInt(properties.getProperty("nodediameter"));
        STARTLINELENGTH = Double.parseDouble(properties.getProperty("startlinelength"));
    }


    @Getter
    double tipX;
    @Getter
    double tipY;
    @Getter
    double endX;
    @Getter
    double tipX2;
    @Getter
    double tipY2;
    @Getter
    double endX2;
    @Getter
    double[] firstDashX = {0};
    @Getter
    double[] firstDashY = {0};
    @Getter
    double[] secondDashX = {0};
    @Getter
    double[] secondDashY = {0};

    public StartWedgeCoordinates(Integer x, Integer y) {
        tipX = x;
        tipY = y + NODEDIAMETER/2;
        tipX2 = x;
        tipY2 = y + NODEDIAMETER/2;
        endX = x - STARTLINELENGTH;
        double x2 = y;
        double y2 = y + NODEDIAMETER/2;
        double endX2 = x - STARTLINELENGTH;

        firstDashX[0] = endX;
        firstDashY[0] = tipY;
        secondDashX[0] = endX2;
        secondDashY[0] = tipY2;

        ViewUtilities.rotateVector(firstDashX, firstDashY, tipX, tipY, -Math.PI / 4);
        ViewUtilities.rotateVector(secondDashX, secondDashY, tipX2,tipY2,Math.PI/4);
        logger.trace("Start x 1: " + tipX + "Start y 1: "
                + tipY + "End x 1: " + endX + "End y 1: " + tipY);
        logger.trace("Start x 2: " + tipX2 + "Start y 1: "
                + tipY2 + "End x 1: " + endX2 + "End y 1: " + tipY2);
    }
}

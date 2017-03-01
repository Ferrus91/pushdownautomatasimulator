package PDAGraphicElements;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.PolygonBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 11/08/13
 * Time: 22:48
 * To change this template use File | Settings | File Templates.
 */
public class  ArrowHeadBuilder {
    static Logger logger = Logger.getLogger(ArrowHeadBuilder.class);

    private static final Integer ARROWHEADSIZE;
    private static final Double SQRT3 = Math.sqrt(3);
    private static final Double HEADALIGHWITHXAXISANGLE = 270.0;
    static
    {
        Properties properties = new Properties();
        try {
            properties.load(ArrowHeadBuilder.class.getClassLoader()
                    .getResourceAsStream("Properties/pdaapp.properties"));
        } catch (IOException e) {e.printStackTrace();}
        ARROWHEADSIZE = Integer.parseInt(properties.getProperty("arrowheadsize"));
    }

    /**
     *
     * @return Returns an unrotated black arrowhead.
     */
    public static Polygon create(Double baseX, Double baseY, Double xDiff, Double yDiff) {

        Double angleOfRotation = Math.atan2(yDiff, xDiff);
        //Sine function needs radians. Used to shift side-lying arrow head up to match tip of the line
        Double proportionShiftY = Math.sin(angleOfRotation);
        //JavaFX needs degrees...
        angleOfRotation = Math.toDegrees(angleOfRotation);
        Double yShift = proportionShiftY*ARROWHEADSIZE/2;

        Double[] coordinates = new Double[]
                {
                  baseX, baseY + SQRT3/2*ARROWHEADSIZE + yShift,
                  baseX + ARROWHEADSIZE/2, baseY + yShift,
                  baseX - ARROWHEADSIZE/2, baseY + yShift
                };

        logger.trace("Built arrowhead with co-ords: " + coordinates);

        return PolygonBuilder
                .create()
                .points(coordinates)
                .rotate(HEADALIGHWITHXAXISANGLE + angleOfRotation)
                .build();
    }
}

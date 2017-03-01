package PDAGraphicElements;

import DrawingTemplate.TransitionEditObject;
import Model.INode;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.PolygonBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 18/08/13
 * Time: 01:07
 * To change this template use File | Settings | File Templates.
 */
public class HandleGenerator {
    static Logger logger = Logger.getLogger(HandleGenerator.class);

    private static final Integer NODEDIAMETER;
    private static final Integer HANDLELENGTH;
    private static final Double HANDLEDIAGONAL;

    static
    {
        Properties properties = new Properties();
        try {
            properties.load(HandleGenerator.class.getClassLoader()
                    .getResourceAsStream("Properties/pdaapp.properties"));
        } catch (IOException e) {e.printStackTrace();}
        NODEDIAMETER = Integer.parseInt(properties.getProperty("nodediameter"));
        HANDLELENGTH = Integer.parseInt(properties.getProperty("handlelength"));
        HANDLEDIAGONAL = Math.sqrt(HANDLELENGTH * HANDLELENGTH + HANDLELENGTH * HANDLELENGTH);
    }

    /**
     * builds handle for a node
     * @param node
     * @return
     */
    public static Node[] build(INode node)
    {
       Integer nodeX = node.getX();
       Integer nodeY = node.getY();
       Double[] corners = handleCornerArray((double)nodeX,(double)nodeY);

      Polygon handle1 = PolygonBuilder.create()
               .fill(Color.BLACK)
               .points(corners)
               .translateX(NODEDIAMETER / 2 * Math.cos(Math.PI / 4))
               .translateY(NODEDIAMETER / 2 * Math.sin(Math.PI / 4))
               .build();
        Polygon handle2 = PolygonBuilder.create()
                .fill(Color.BLACK)
                .points(corners)
                .translateX(NODEDIAMETER/2*Math.cos(3*Math.PI/4))
                .translateY(NODEDIAMETER/2*Math.sin(3*Math.PI/4))
                .build();
        Polygon handle3 = PolygonBuilder.create()
                .fill(Color.BLACK)
                .points(corners)
                .translateX(NODEDIAMETER/2*Math.cos(5*Math.PI/4))
                .translateY(NODEDIAMETER/2*Math.sin(5*Math.PI/4))
                .build();
        Polygon handle4 = PolygonBuilder.create()
                .fill(Color.BLACK)
                .points(corners)
                .translateX(NODEDIAMETER/2*Math.cos(7*Math.PI/4))
                .translateY(NODEDIAMETER/2*Math.sin(7*Math.PI/4))
                .build();

        logger.trace("Built handles with co-ords: "
                + handle1.getPoints().toString()
                + handle2.getPoints().toString()
                + handle3.getPoints().toString()
                + handle4.getPoints().toString());
        return new Node[]{handle1,handle2,handle3,handle4};
    }

    /**
     * builds handle for a relation
     * @param transitionEditObject
     * @return
     */
    public static Node[] build(TransitionEditObject transitionEditObject)
    {
        Double[] corners = handleCornerArray(0.0,0.0);

        Polygon handle1 = PolygonBuilder.create()
                .fill(Color.BLACK)
                .points(corners)
                .translateX(transitionEditObject.getXAngleOnFirstCircle())
                .translateY(transitionEditObject.getYAngleOnFirstCircle())
                .build();
        Polygon handle2 = PolygonBuilder.create()
                .fill(Color.BLACK)
                .points(corners)
                .translateX((transitionEditObject.getXAngleOnFirstCircle()
                        + transitionEditObject.getXAngleOnSecondCircle())/2)
                .translateY((transitionEditObject.getYAngleOnFirstCircle()
                        + transitionEditObject.getYAngleOnSecondCircle())/2)
                .build();
        Polygon handle3 = PolygonBuilder.create()
                .fill(Color.BLACK)
                .points(corners)
                .translateX(transitionEditObject.getXAngleOnSecondCircle())
                .translateY(transitionEditObject.getYAngleOnSecondCircle())
                .build();

        logger.trace("Built handles with co-ords: "
                + handle1.getPoints().toString()
                + handle2.getPoints().toString()
                + handle3.getPoints().toString());
        return new Node[]{handle1, handle2, handle3};
    }

    private static Double[] handleCornerArray(Double nodeX, Double nodeY) {
        return new Double[]{nodeX + HANDLEDIAGONAL,  nodeY + HANDLEDIAGONAL,
                nodeX - HANDLEDIAGONAL, nodeY + HANDLEDIAGONAL,
                nodeX - HANDLEDIAGONAL, nodeY - HANDLEDIAGONAL,
                nodeX + HANDLEDIAGONAL, nodeY - HANDLEDIAGONAL
        };
    }
}


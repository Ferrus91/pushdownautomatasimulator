package DrawingTemplate;

import EditObjectFactory.ConcreteEditObjectFactory;
import EditObjectFactory.IEditObjectFactory;
import Model.DrawingModel.DrawingNode;
import Model.DrawingModel.IDrawingLists;
import Model.DrawingModel.Relation;
import Model.INode;
import PDAGraphicElements.*;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

//import PDAGraphicElements.ArrowHeadBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 08/08/13
 * Time: 23:24
 * The template for the editor part of the application
 */
public class ConcreteEditTemplate extends  IEditTemplate {
    static Logger logger = Logger.getLogger(ConcreteEditTemplate.class);

    private static final IEditObjectFactory EDIT_OBJECT_FACTORY = new ConcreteEditObjectFactory();
    private static final Integer NODEDIAMETER;
    private static final Integer ARROWHEADSIZE;
    private static final Double FINALNODEINPER;
    static
    {
        Properties properties = new Properties();
        try {
            properties.load(ConcreteDrawingTemplate.class.getClassLoader()
                    .getResourceAsStream("Properties/pdaapp.properties"));
        } catch (IOException e) {e.printStackTrace();}
        NODEDIAMETER = Integer.parseInt(properties.getProperty("nodediameter"));
        FINALNODEINPER = Double.parseDouble(properties.getProperty("finalnodeinper"));
        ARROWHEADSIZE = Integer.parseInt(properties.getProperty("arrowheadsize"));
    }

    private Pane pane;

    @Inject
    ConcreteEditTemplate(@Assisted Pane pane)
    {
        this.pane = pane;
    }

    @Override
    public void drawNode(INode node, IDrawingLists IDrawingLists) {
        logger.trace("Drawing node on pane: " + node.getId());

        final PDANode circleNode = EDIT_OBJECT_FACTORY.createPDANode(node.getId(),node.getX(),
                node.getY(), node.getIsNodeDown());
        circleNode.addEventHandler(MouseEvent.MOUSE_DRAGGED, new CircleEventHandler(pane));

        circleNode.addEventHandler(MouseEvent.MOUSE_RELEASED, new CircleEventHandler(pane));

        Text drawText = new Text(node.getLabel());
        Double textWidth =  drawText.getLayoutBounds().getWidth();
        Double textHeight = drawText.getLayoutBounds().getHeight();

        Label label = LabelBuilder.create()
                .text(labelCutter(node.getLabel()))
                .layoutX(circleNode.getCenterX() - drawText.getLayoutBounds().getWidth()/2)
                .layoutY(circleNode.getCenterY() - drawText.getLayoutBounds().getWidth()/2)
                .font(Font.font("Tahoma",15))
                .build();

        label.addEventHandler(MouseEvent.MOUSE_DRAGGED, new CircleEventHandler(pane));

        label.addEventHandler(MouseEvent.MOUSE_RELEASED, new CircleEventHandler(pane));

        if(node.getIsFinalNode())
        {
            logger.trace("Drawing final node on pane: " + node.getId());
            Circle finalCircle = CircleBuilder.create()
                    .centerX(node.getX())
                    .centerY(node.getY())
                    .radius(NODEDIAMETER / 2 * FINALNODEINPER)
                    .build();
            finalCircle.setFill(Color.WHITE);
            finalCircle.setStroke(Color.BLACK);

            finalCircle.addEventHandler(MouseEvent.MOUSE_DRAGGED, new CircleEventHandler(pane));

            finalCircle.addEventHandler(MouseEvent.MOUSE_RELEASED, new CircleEventHandler(pane));

            pane.getChildren().addAll(circleNode, finalCircle, label);
        }
        else
            pane.getChildren().addAll(circleNode, label);
        if(((DrawingNode)node).getIsSelected())
        {
            Node[] handles = HandleGenerator.build(node);
            pane.getChildren().addAll(handles);
        }
    }

    @Override
    public void drawStartNode(INode node, IDrawingLists IDrawingLists) {
        logger.trace("Drawing start node on pane: " + node.getId());
     StartWedgeCoordinates startWedgeCoordinates = new StartWedgeCoordinates(node.getX()- NODEDIAMETER/2,
             node.getY() - NODEDIAMETER/2);
     Line lineBit = LineBuilder.create()
             .startX(startWedgeCoordinates.tipX)
             .endX(startWedgeCoordinates.firstDashX[0])
             .startY(startWedgeCoordinates.tipY)
             .endY(startWedgeCoordinates.firstDashY[0])
             .build();
     Line lineBit2 = LineBuilder.create()
             .startX(startWedgeCoordinates.tipX2)
             .endX(startWedgeCoordinates.secondDashX[0])
             .startY(startWedgeCoordinates.tipY2)
             .endY(startWedgeCoordinates.secondDashY[0])
             .build();

        pane.getChildren().addAll(lineBit, lineBit2);
        drawNode(node, IDrawingLists);
    }

    @Override
    public void drawRelations(Relation link, ArrayList<PDANode> pdaNodes) {
        if(link.getToNode().get(0).equals(link.getFromNode()))
        {
            logger.trace("Drawing relaiton on pane from " + link.getToNode().get(0).getId());

            Boolean isNodeDown = pdaNodes.get(0).getIsDownNode();

            TransitionEditObject transitionEditObject = new TransitionEditObject(pdaNodes,isNodeDown);

            Integer loopUpCorrection = !isNodeDown ? ARROWHEADSIZE/2 : 0;

            PDALoopRelation pdaLoopRelation = new PDALoopRelation(link.getId(),
                    transitionEditObject.getStartX(), transitionEditObject.getStartY(),
                    transitionEditObject.getFirstX(), transitionEditObject.getFirstY(),
                    transitionEditObject.getSecondX(), transitionEditObject.getSecondY()
                    );
            Polygon arrowHead = ArrowHeadBuilder.create(transitionEditObject.getStartX() + loopUpCorrection,
                    transitionEditObject.getStartY() - loopUpCorrection,
                    transitionEditObject.getSecondX() -transitionEditObject.getStartX(),
                    transitionEditObject.getSecondY() -transitionEditObject.getStartY()
            );

            Text drawText = new Text(link.toString());
            Double textWidth =  drawText.getLayoutBounds().getWidth();
            Double textHeight = drawText.getLayoutBounds().getHeight();

            Label label = LabelBuilder.create()
                    .text(drawText.getText())
                    .layoutX((transitionEditObject.getFirstX() + transitionEditObject.getSecondX()) / 2 - textWidth / 2)
                    .layoutY(transitionEditObject.getSecondY() + textHeight / 2
                            - (!isNodeDown ? textHeight : 0))
                    .build();

            pane.getChildren().addAll(pdaLoopRelation,arrowHead, label);
        }
        else
        {
            logger.trace("Drawing relaiton on pane from " + link.getFromNode().getId()
                    + " to " + link.getToNode().get(0).getId());
            TransitionEditObject transitionEditObject = new TransitionEditObject(pdaNodes);

            PDATransRelation pdaTransRelation = new PDATransRelation(link.getId(),
                transitionEditObject.getXAngleOnFirstCircle(), transitionEditObject.getYAngleOnFirstCircle(),
                transitionEditObject.getXBulge(), transitionEditObject.getYBulge(),
                transitionEditObject.getXAngleOnSecondCircle(), transitionEditObject.getYAngleOnSecondCircle());

            Polygon arrowHead = ArrowHeadBuilder.create(transitionEditObject.getXAngleOnFirstCircle(),
                transitionEditObject.getYAngleOnFirstCircle(),
                transitionEditObject.getXAngleOnFirstCircle()- transitionEditObject.getXBulge(),
                transitionEditObject.getYAngleOnFirstCircle()- transitionEditObject.getYBulge()
                );

            Text drawText = new Text(link.toString());

            TextBoxBoundsGenerator textBoxBoundsGenerator = new TextBoxBoundsGenerator(drawText,
                    transitionEditObject.getXAngleOnFirstCircle(),
                    transitionEditObject.getXBulge(),
                    transitionEditObject.getYAngleOnFirstCircle(),
                    transitionEditObject.getYBulge());

            Label label = LabelBuilder.create()
                    .text(drawText.getText())
                    .layoutX(textBoxBoundsGenerator.labelX1)
                    .layoutY(textBoxBoundsGenerator.labelY1)
                    .build();

            pane.getChildren().addAll(pdaTransRelation,arrowHead, label);

            if(link.getIsSelected())
            {
                Node[] handles = HandleGenerator.build(transitionEditObject);
                pane.getChildren().addAll(handles);
            }

        }
    }
}

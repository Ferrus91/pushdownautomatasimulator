package SugiyamaAdapter;

import DrawingTemplate.TransitionDrawingObject;
import Model.DrawingModel.IDrawingLists;
import javafx.geometry.Point2D;
import org.apache.log4j.Logger;
import pedviz.algorithms.HierarchieUpDown;
import pedviz.algorithms.sugiyama.SugiyamaLayout;
import pedviz.graph.Graph;
import pedviz.graph.Node;
import pedviz.view.DefaultEdgeView;
import pedviz.view.DefaultNodeView;
import pedviz.view.NodeView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 15/08/13
 * Time: 17:19
 * To change this template use File | Settings | File Templates.
 */
public class ConcereteSugiyamaAdapter implements ISugiyamaAdapter {
    static Logger logger = Logger.getLogger(ConcereteSugiyamaAdapter.class);
    static final Float NODEDIAMETER;
    static final Integer DEFAULTLINELENGTH;

    static
    {
        Properties properties = new Properties();
        try {
            properties.load(TransitionDrawingObject.class.getClassLoader()
                    .getResourceAsStream("Properties/pdaapp.properties"));
        } catch (IOException e) {e.printStackTrace();}

        NODEDIAMETER = Float.parseFloat(properties.getProperty("nodediameter"));
        DEFAULTLINELENGTH = Integer.parseInt(properties.getProperty("defaultlinelength"));
    }

    /**
     * incomplete adapter designed as a means of reordering the nodes by the Sugiyama algorithm
     * doesn't produce co-ordinates so abandoned, but in place to be refactored if it can be impleneted
     * possibly by hand
     * @param IDrawingLists
     * @return
     */
    @Override
    public IDrawingLists runSugiyama(IDrawingLists IDrawingLists) {
        logger.trace("Running sugiyama");
        if(IDrawingLists.getRelations().size() > 0)
        {
            Graph drawingGraph = PedVizBuilder.buildPezVizGraph(IDrawingLists);
            DefaultNodeView defaultNodeView = new DefaultNodeView();
            defaultNodeView.setSize(NODEDIAMETER);
            defaultNodeView.setGap(DEFAULTLINELENGTH);
            DefaultEdgeView defaultEdgeView = new DefaultEdgeView();

            defaultNodeView.getSize();

            drawingGraph.buildHierarchie(new HierarchieUpDown());

            SugiyamaLayout sugiyamaLayout = new SugiyamaLayout(drawingGraph, defaultNodeView,defaultEdgeView);
            sugiyamaLayout.run();


            HashMap<String, Point2D> points = new HashMap<String, Point2D>();

            for(Node reDrawnNode : sugiyamaLayout.getLayoutGraph().getGraph().getAllNodes())
            {
                NodeView nodeView = sugiyamaLayout.createNodeView(reDrawnNode, defaultNodeView);
                points.put(reDrawnNode.getId().toString(), new Point2D(nodeView.getPosX(), nodeView.getPosY()));
            }

            PointCollator.pointsCollator(IDrawingLists, points);
        }

        return IDrawingLists;
    }
}

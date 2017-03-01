package SugiyamaAdapter;

import Model.DrawingModel.IDrawingLists;
import Model.ILink;
import Model.INode;
import org.apache.log4j.Logger;
import pedviz.graph.Edge;
import pedviz.graph.Graph;
import pedviz.graph.Node;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 15/08/13
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */
public class PedVizBuilder {
    static Logger logger = Logger.getLogger(PedVizBuilder.class);

    public static Graph buildPezVizGraph(IDrawingLists IDrawingLists)
    {
        logger.trace("Adapting for pedviz");
        Graph returnGraph = new Graph();
        for(INode modelNode : IDrawingLists.getNodes().values())
        {
            returnGraph.addNode(new Node(modelNode.getId()));
        }

        for(ILink modelLink : IDrawingLists.getRelations().values())
        {
             returnGraph.addEdge(new Edge(returnGraph.getNode(modelLink.getFromNode().getId()),
                     returnGraph.getNode(modelLink.getToNode().get(0).getId())));
        }
        return returnGraph;
    }
}

package CycleDectectorAdapter;

import Model.DrawingModel.IDrawingLists;
import Model.DrawingModel.Relation;
import Model.INode;
import org.apache.log4j.Logger;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 16/08/13
 * Time: 12:07
 * To change this template use File | Settings | File Templates.
 */
public class CreateNullGraph {
    static Logger logger = Logger.getLogger(CreateNullGraph.class);

    /**
     * aids the adapter by converting from drawing model to abstract graph
     * @param IDrawingLists
     * @return abstract graph
     */
    public static DirectedGraph<INode, DefaultEdge> build(IDrawingLists IDrawingLists)
    {
        logger.trace("Creating null graph");
        DirectedGraph<INode, DefaultEdge> directedGraph =
                new DefaultDirectedGraph<INode, DefaultEdge>(DefaultEdge.class);

        for(INode node : IDrawingLists.getNodes().values())
        {
            directedGraph.addVertex(node);
        }

        for(Relation relation : IDrawingLists.getRelations().values())
        {
            directedGraph.addEdge(IDrawingLists.getNodes().get(relation.getFromNode().getId()),
                    IDrawingLists.getNodes().get(relation.getToNode().get(0).getId()));
        }

        return directedGraph;
    }
}

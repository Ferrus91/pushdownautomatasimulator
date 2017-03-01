package XStreamStaging;

import Model.AnimationModel.Automata;
import Model.DrawingModel.IDrawingLists;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 19/08/13
 * Time: 14:59
 * To change this template use File | Settings | File Templates.
 */
public interface IStagingLoader {
    StagingAutomata drawingListToStagingAutomata(IDrawingLists IDrawingLists);
    Automata stagingAutomataToAutomata(StagingAutomata stagingAutomata) throws IOException;
    IDrawingLists stagingAutomataToDrawingLists(StagingAutomata stagingAutomata);
}

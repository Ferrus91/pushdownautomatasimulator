package DependencyInjection;

import Model.DrawingModel.DrawingLists;
import Model.DrawingModel.IDrawingLists;
import com.google.inject.AbstractModule;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 06/09/13
 * Time: 21:39
 * To change this template use File | Settings | File Templates.
 */
public class DrawingListModule extends AbstractModule {
    static Logger logger = Logger.getLogger(DrawingListModule.class);

    @Override
    protected void configure() {
        logger.trace("Configuring drawing list");
        bind(IDrawingLists.class).to(DrawingLists.class);
    }
}

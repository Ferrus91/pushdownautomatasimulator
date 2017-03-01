package DependencyInjection;

import Model.AnimationModel.Automata;
import Model.AnimationModel.IAutomata;
import com.google.inject.AbstractModule;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 06/09/13
 * Time: 21:38
 * To change this template use File | Settings | File Templates.
 */
public class AutomataModule extends AbstractModule {
    static Logger logger = Logger.getLogger(AutomataModule.class);

    @Override
    protected void configure() {
        logger.trace("Configuring automata");
        bind(IAutomata.class).to(Automata.class);
    }
}

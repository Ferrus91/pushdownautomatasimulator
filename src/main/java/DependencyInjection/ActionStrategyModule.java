package DependencyInjection;

import Controller.RunnerActionStrategies.ActionStrategy;
import Controller.RunnerActionStrategies.IActionStrategy;
import com.google.inject.AbstractModule;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/07/13
 * Time: 21:40
 * To change this template use File | Settings | File Templates.
 */
public class ActionStrategyModule extends AbstractModule {
    static Logger logger = Logger.getLogger(ActionStrategyModule.class);

    @Override
    protected void configure() {
        logger.trace("Configuring abstract module");
        bind(IActionStrategy.class).to(ActionStrategy.class);
    }
}

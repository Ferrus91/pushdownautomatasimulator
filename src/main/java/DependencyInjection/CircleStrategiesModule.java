package DependencyInjection;

import Controller.RunnerActionStrategies.CircleStrategies;
import Controller.RunnerActionStrategies.ICircleStrategies;
import com.google.inject.AbstractModule;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 11/09/13
 * Time: 00:50
 * To change this template use File | Settings | File Templates.
 */
public class CircleStrategiesModule extends AbstractModule {
    @Override
    protected void configure() {
         bind(ICircleStrategies.class).to(CircleStrategies.class);
    }
}

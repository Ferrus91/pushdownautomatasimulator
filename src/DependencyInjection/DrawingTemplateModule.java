package DependencyInjection;

import DrawingTemplate.ConcreteDrawingTemplate;
import DrawingTemplate.IDrawingFactory;
import DrawingTemplate.IDrawingTemplate;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 17/07/13
 * Time: 20:04
 * To change this template use File | Settings | File Templates.
 */
public class DrawingTemplateModule extends AbstractModule {
    static Logger logger = Logger.getLogger(DrawingTemplateModule.class);

    @Override
    protected void configure() {
        logger.trace("Configuring drawing template");
        install(new FactoryModuleBuilder()
                .implement(IDrawingTemplate.class, ConcreteDrawingTemplate.class)
                .build(IDrawingFactory.class));
    }
}

package DependencyInjection;

import DrawingTemplate.ConcreteEditTemplate;
import DrawingTemplate.IEditFactory;
import DrawingTemplate.IEditTemplate;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 08/08/13
 * Time: 23:37
 * To change this template use File | Settings | File Templates.
 */
public class EditTemplateModule extends AbstractModule {
    static Logger logger = Logger.getLogger(EditTemplateModule.class);

    @Override
    protected void configure() {
        logger.trace("Configuring edit template");

        install(new FactoryModuleBuilder()
                .implement(IEditTemplate.class, ConcreteEditTemplate.class)
                .build(IEditFactory.class));
    }


}

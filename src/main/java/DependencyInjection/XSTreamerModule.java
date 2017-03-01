package DependencyInjection;

import XStreamStaging.ISerialize;
import XStreamStaging.IStagingLoader;
import XStreamStaging.Serialize;
import XStreamStaging.StagingLoader;
import com.google.inject.AbstractModule;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 22/08/13
 * Time: 23:52
 * To change this template use File | Settings | File Templates.
 */
public class XStreamerModule extends AbstractModule {
    static Logger logger = Logger.getLogger(XStreamerModule.class);

    @Override
    protected void configure() {
        logger.trace("XStreamer configured");
        bind(ISerialize.class).to(Serialize.class);
        bind(IStagingLoader.class).to(StagingLoader.class);
    }
}

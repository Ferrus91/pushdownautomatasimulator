package AppLoaders;

import com.beust.jcommander.Parameter;
import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 29/08/13
 * Time: 11:39
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleCommanderParams {
        @Getter
        @Parameter(names = "-input", description = "Input string of data", required = true)
        private String inputString;

        @Getter
        @Parameter(names = "-directory", description = "Name of the directory automata is in", required = true)
        private String directoryName;

        @Getter
        @Parameter(names = "-automata", description = "Name of automata (if more than one with same name, top item is selected",
                required = true)
        private String automataName;
}

package Model;

import DependencyInjection.EventBusServer.DeregisterEventServer;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 17/08/13
 * Time: 22:18
 * To change this template use File | Settings | File Templates.
 */
public interface IAutomataModel extends DeregisterEventServer {
    Boolean getIsLMachine();
}

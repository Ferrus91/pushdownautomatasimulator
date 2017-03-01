package CycleDectectorAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 16/08/13
 * Time: 12:03
 * To change this template use File | Settings | File Templates.
 */
public interface ICycleAdapter<T> {
    Boolean isCycle(T cycleChecker);
}

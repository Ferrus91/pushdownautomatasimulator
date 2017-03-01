package ReflexiveStrategy;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 17/08/13
 * Time: 22:49
 * To change this template use File | Settings | File Templates.
 */
public class ReflexiveStrategy {
    static Logger logger = Logger.getLogger(ReflexiveStrategy.class);

    /**
     *  fetches node and finds the method to be called upon it by reflection
     * @param id  id of node to be called
     * @param methodName name of the method
     * @param hashMap  hashMap of types
     * @param instance
     */
    public void callBack(String id, String methodName, ConcurrentHashMap<String, ?> hashMap, Object instance)
    {
        try {
            logger.trace("Finding method for reflection in " + instance.getClass().getSimpleName());
            Class drawingLists = Class.forName("Model.DrawingModel.DrawingLists");
            for(Method method : drawingLists.getDeclaredMethods())
            {
                if(method.getName().equals(methodName))
                {
                    try {
                        method.setAccessible(true);
                        Object[] args = {id, hashMap};
                        method.invoke(instance, args);
                        method.setAccessible(false);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void callBack(String id, String methodName, Object instance)
    {
        try {
            Class drawingLists = Class.forName("Model.DrawingModel.DrawingLists");
            for(Method method : drawingLists.getDeclaredMethods())
            {
                if(method.getName().equals(methodName))
                {
                    try {
                        method.setAccessible(true);
                        Object[] args = {id};
                        method.invoke(instance, args);
                        method.setAccessible(false);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean hasMethod(String s) {
        Class drawingLists = null;
        try {
            drawingLists = Class.forName("Model.DrawingModel.DrawingLists");
        for(Method method : drawingLists.getDeclaredMethods())
        {
            if(method.getName().equals(s))
                return true;
        }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return false;
    }
}

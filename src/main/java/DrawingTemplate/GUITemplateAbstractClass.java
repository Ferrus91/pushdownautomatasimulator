package DrawingTemplate;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 19/08/13
 * Time: 18:16
 * To change this template use File | Settings | File Templates.
 */
public abstract class GUITemplateAbstractClass<U,V,W> implements IGUITemplate<U,V,W>   {
    public String labelCutter(String label)
    {
        return label.length() > 3 ? label.substring(0,2) + ".." : label;
    }
}

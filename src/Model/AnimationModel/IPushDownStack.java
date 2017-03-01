package Model.AnimationModel;

import Utils.StackIOString;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 25/06/13
 * Time: 21:07
 * To change this template use File | Settings | File Templates.
 */
public interface IPushDownStack {
    public Character pop();
    public Character peek();
    public void push(StackIOString symbols);
    public String fullContents();
    public boolean empty();
}

package Controller.RunnerActionStrategies;

import DependencyInjection.EventBusServer.IEventBusServer;
import javafx.animation.Timeline;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/07/13
 * Time: 21:38
 * To change this template use File | Settings | File Templates.
 */
public interface IActionStrategy {
    void addToQueue(TextField userInputBox, ObservableList<String> inputItems);

    void extractFile(ObservableList<String> inputItems);

    void fastForward(Property<Boolean> pdaFinished, ObservableList<String> inputItems, TextArea textBox,
                     Timeline timelineFastForward, IEventBusServer eventBus);

    void forward(Property<Boolean> pdaFinished, ObservableList<String> inputItems, TextArea textBox,
                 Timeline timelineForward, IEventBusServer eventBus);
}

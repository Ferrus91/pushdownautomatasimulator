package DependencyInjection;

import EventObjects.Blink;
import EventObjects.NextTransitionEvent;
import EventObjects.ResetAutomataEvent;
import com.google.inject.AbstractModule;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/07/13
 * Time: 20:55
 * To change this template use File | Settings | File Templates.
 */
public class AnimationModule extends AbstractModule {
    static Logger logger = Logger.getLogger(AnimationModule.class);

    static private final Timeline PLAYTIMELINE;
    static private final Timeline FASTFORWARDTIMELINE;
    static private KeyFrame KEYFRAME1;
    static private KeyFrame KEYFRAME2;
    static private Integer DURATION1 = 400;
    static private Integer DURATION2 = 800;

    static
    {
        EventHandler onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                EventBusModule.ANIMATIONEVENTS.post(new ResetAutomataEvent(Blink.BLINK));
            }
        };

        EventHandler onFinished2 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                EventBusModule.ANIMATIONEVENTS.post(new NextTransitionEvent());
            }
        };

        KEYFRAME1 = new KeyFrame(Duration.millis(DURATION1), onFinished);
        KEYFRAME2 = new KeyFrame(Duration.millis(DURATION2), onFinished2);
        FASTFORWARDTIMELINE = new Timeline();
        PLAYTIMELINE = new Timeline();

        FASTFORWARDTIMELINE.setCycleCount(javafx.animation.Animation.INDEFINITE);
        FASTFORWARDTIMELINE.getKeyFrames().add(KEYFRAME1);
        FASTFORWARDTIMELINE.getKeyFrames().add(KEYFRAME2);

        PLAYTIMELINE.setCycleCount(1);
        PLAYTIMELINE.getKeyFrames().add(KEYFRAME1);
        PLAYTIMELINE.getKeyFrames().add(KEYFRAME2);
    }

    @Override
    protected void configure() {
        logger.trace("Configuring animations");
        bind(Timeline.class).annotatedWith(Fastforward.class).toInstance(FASTFORWARDTIMELINE);
        bind(Timeline.class).annotatedWith(Forward.class).toInstance(PLAYTIMELINE);
    }
}

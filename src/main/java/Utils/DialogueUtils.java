package Utils;

import org.apache.log4j.Logger;
import org.thehecklers.monologfx.MonologFX;
import org.thehecklers.monologfx.MonologFXBuilder;
import org.thehecklers.monologfx.MonologFXButton;
import org.thehecklers.monologfx.MonologFXButtonBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 14/08/13
 * Time: 00:26
 * To change this template use File | Settings | File Templates.
 */
public class DialogueUtils {
    static Logger logger = Logger.getLogger(DialogueUtils.class);

    /**
     * static method to to produce standard warning message with a string
     * @param warningMessage
     */
    static public void warningMessage(String warningMessage)
    {
        //inspired by https://blogs.oracle.com/javajungle/entry/monologfx_floss_javafx_dialogs_for
        logger.trace("Fire warning message: " + warningMessage);
        MonologFXButton ok = MonologFXButtonBuilder.create()
                .defaultButton(true)
                .icon("/testmonologfx/dialog_apply.png")
                .type(MonologFXButton.Type.YES)
                .build();

        MonologFX warning = MonologFXBuilder.create()
                .modal(true)
                .message(warningMessage)
                .titleText("Warning")
                .button(ok)
                .buttonAlignment(MonologFX.ButtonAlignment.CENTER)
                .build();

        warning.showDialog();

    }

    /**
     * typical static message to produce a modal dialog box and a boolean with the result
     * @param message
     * @return
     */
    public static boolean choiceBox(String message) {
        logger.trace("Fire choice box" + message);
        //inspired by https://blogs.oracle.com/javajungle/entry/monologfx_floss_javafx_dialogs_for
        MonologFXButton mlb = MonologFXButtonBuilder.create()
                .defaultButton(true)
                .icon("/testmonologfx/dialog_apply.png")
                .type(MonologFXButton.Type.YES)
                .build();

        MonologFXButton mlb2 = MonologFXButtonBuilder.create()
                .cancelButton(true)
                .icon("/testmonologfx/dialog_cancel.png")
                .type(MonologFXButton.Type.NO)
                .build();

        MonologFX mono = MonologFXBuilder.create()
                .modal(true)
                .type(MonologFX.Type.QUESTION)
                .message(message)
                .titleText("Warning")
                .button(mlb)
                .button(mlb2)
                .buttonAlignment(MonologFX.ButtonAlignment.RIGHT)
                .build();

        MonologFXButton.Type retval = mono.showDialog();

        return retval == MonologFXButton.Type.YES;
    }
}

package GraphicsElements;

import Model.DrawingModel.EditTransitions;
import Utils.DialogueUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;

// Class adapted from Example 12-11 Alternative Solution Of Cell Editing in ths tutorial:
// http://docs.oracle.com/javafx/2/ui_controls/table-view.htm
// @Author: Alla Redko

public class EditingCellTransitionChar extends EditingCellChar {
    static Logger logger = Logger.getLogger(EditingCellTransitionChar.class);

    private final ObservableList<EditTransitions> data;

    public EditingCellTransitionChar(ObservableList<EditTransitions> data) {
        super();
        this.data = data;
    }

    @Override
    void createTextField() {
        logger.trace("Generate text field");
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
        textField.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0,
                                Boolean arg1, Boolean arg2) {
                if (!arg2) {
                    if(textField.getText().length() > 0)
                    {
                        for(EditTransitions editTransition : data)
                        {
                            if(editTransition.getTransitionChar() == textField.getText().charAt(0))
                            {
                                DialogueUtils.warningMessage("Can't have two transitions with the same name");
                                return;
                            }
                        }
                        commitEdit(textField.getText().charAt(0));
                    }
                }
            }
        });
    }
}

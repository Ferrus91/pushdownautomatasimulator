package GraphicsElements;

import Model.DrawingModel.EditTransitions;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import org.apache.log4j.Logger;

// Class adapted from Example 12-11 Alternative Solution Of Cell Editing in ths tutorial:
// http://docs.oracle.com/javafx/2/ui_controls/table-view.htm
// @Author: Alla Redko

public class EditingCellChar extends TableCell<EditTransitions, Character> {
    static Logger logger = Logger.getLogger(EditingCellChar.class);

    RestrictiveTextField textField = new RestrictiveTextField();

    @Override
    public void startEdit() {
        logger.trace("Editing started");
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setText(null);
            textField.setMaxLength(1);
            setGraphic(textField);
            textField.selectAll();
        }
    }

    @Override
    public void cancelEdit() {
        logger.trace("Editing canceled");
        super.cancelEdit();

        setText(getItem().toString());
        setGraphic(null);
    }

    @Override
    public void updateItem(Character item, boolean empty) {
        logger.trace("Updating item");
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(null);
            }
        }
    }

    void createTextField() {
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
        textField.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0,
                                Boolean arg1, Boolean arg2) {
                if (!arg2) {
                    if(textField.getText().length() > 0)
                        commitEdit(textField.getText().charAt(0));
                }
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}
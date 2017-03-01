package GraphicsElements;

import Model.DrawingModel.EditTransitions;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 25/08/13
 * Time: 00:30
 * To change this template use File | Settings | File Templates.
 */
public class RightClickableRow extends TableRow {
    static Logger logger = Logger.getLogger(RightClickableRow.class);

    final private ContextMenu deleteMenu = new ContextMenu();
    private final TableView transitionTable;

    public RightClickableRow(TableView transitionTable, ObservableList<EditTransitions> inputItems)
    {
        this.transitionTable = transitionTable;
        MenuItem deleteString = new MenuItem("Delete Transition");
        deleteString.setOnAction(new MenuRemovalHandler(getIndex(), inputItems));

        deleteMenu.getItems().add(deleteString);

        /**
         * Right click displays the menu with delete option
         */

        this.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        if (e.getButton() == MouseButton.SECONDARY)
                            deleteMenu.show(RightClickableRow.this, e.getScreenX(), e.getScreenY());  }
                }
        );
    }

    /**
     * creates the necessary context menu with delete option
     */

    public class MenuRemovalHandler implements EventHandler<ActionEvent> {


        private ObservableList<EditTransitions> inputItems;

        public MenuRemovalHandler(Integer index, ObservableList<EditTransitions> inputItems) {
            this.inputItems = inputItems;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            Integer index = RightClickableRow.this.getIndex();
            logger.trace("Deleting row: " + index);
            inputItems.remove(index);
            transitionTable.setItems(inputItems);
        }

    }
}
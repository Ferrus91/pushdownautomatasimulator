package GraphicsElements;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 20/07/13
 * Time: 13:28
 * To change this template use File | Settings | File Templates.
 */
public class RightClickableCell extends ListCell<String> {
    static Logger logger = Logger.getLogger(RightClickableCell.class);

    final private ContextMenu deleteMenu = new ContextMenu();

    @Override protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(item);
}

    public RightClickableCell(ObservableList<String> inputItems)
    {
        MenuItem deleteString = new MenuItem("Delete String");
        deleteString.setOnAction(new MenuRemovalHandler(getIndex(), inputItems));

        deleteMenu.getItems().add(deleteString);

        /**
         * Right click displays the menu with delete option
         */

        this.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        if (e.getButton() == MouseButton.SECONDARY)
                            deleteMenu.show(RightClickableCell.this, e.getScreenX(), e.getScreenY());  }
                }
                    );
    }

    /**
     * creates the necessary context menu with delete option
     */

    public class MenuRemovalHandler implements EventHandler<ActionEvent> {


        private ObservableList<String> inputItems;

        public MenuRemovalHandler(Integer index, ObservableList<String> inputItems) {
            this.inputItems = inputItems;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            Integer index = RightClickableCell.this.getIndex();
            logger.trace("Deleting row: " + index);
            if(inputItems.size() >= index)
                inputItems.remove(RightClickableCell.this.getListView().getItems().get(index));
        }

    }
}

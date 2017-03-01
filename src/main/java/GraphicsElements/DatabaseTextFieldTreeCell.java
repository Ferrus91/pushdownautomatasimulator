package GraphicsElements;

import Database.TreeNodeObject;
import Database.TreeNodeType;
import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.MoveAutomataEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;

// Class adapted from Example 13-3 Alternative Solution Of Cell Editing in ths tutorial:
// http://docs.oracle.com/javafx/2/ui_controls/tree-view.htm
// @Author: Alla Redko


public class DatabaseTextFieldTreeCell extends TreeCell<TreeNodeObject> {
    static Logger logger = Logger.getLogger(DatabaseTextFieldTreeCell.class);

    private TextField textField;
        private IEventBusServer eventBusServer;
        private Label ghostLabel;

        public DatabaseTextFieldTreeCell(final IEventBusServer eventBusServer) {
            ghostLabel = new Label(getText());
            ghostLabel.setTextFill(Color.LIGHTGREY);
            ghostLabel.setVisible(false);

            this.getChildren().addAll(ghostLabel);

            this.eventBusServer = eventBusServer;

            // these events save the id of the automaton and allow it to be dragged onto a directory which
            // triggers an update to the database to add these nodes there

            this.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(getItem().getTreeNodeType() == TreeNodeType.AUTOMATA)
                    {
                        logger.trace("Drag start");
                        Dragboard db = startDragAndDrop(TransferMode.MOVE);
                        ClipboardContent content = new ClipboardContent();
                        content.putString(getItem().getId().toString());
                        db.setContent(content);
                        mouseEvent.consume();
                    }
                }
            });

            this.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent dragEvent) {
                    Dragboard db = dragEvent.getDragboard();
                    ghostLabel.setLayoutX(dragEvent.getX());
                    ghostLabel.setLayoutY(dragEvent.getY());
                    ghostLabel.setVisible(true);
                    if(getItem().getTreeNodeType() == TreeNodeType.DIRECTORY)
                        dragEvent.acceptTransferModes(TransferMode.MOVE);
                }
            });

            this.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent dragEvent) {
                    dragEvent.acceptTransferModes(TransferMode.MOVE);
                    if (getItem().getTreeNodeType() == TreeNodeType.DIRECTORY) {
                        logger.trace("Drag dropped");
                        ghostLabel.setVisible(false);
                        Dragboard db = dragEvent.getDragboard();
                        boolean success = false;
                        if (db.hasString()) {
                            Integer automataId = Integer.parseInt(db.getString());
                            eventBusServer.post(new MoveAutomataEvent(getItem().getId(), automataId));
                            success = true;
                        }
                        dragEvent.setDropCompleted(success);
                        dragEvent.consume();
                    }
                }
            });
        }



        @Override
        public void startEdit() {
            super.startEdit();
            logger.trace("Editing node");
            if(getItem().getTreeNodeType() != TreeNodeType.ROOT
                    && !getItem().getName().equals("Default"))
            {

                if (textField == null) {
                createTextField();
            }
            setText(null);
            setGraphic(textField);
            textField.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            logger.trace("Editing canceled");
            setText(getItem().getName());
            setGraphic(getTreeItem().getGraphic());
        }

        @Override
        public void updateItem(TreeNodeObject treeNodeObject, boolean empty) {
            logger.trace("Updating node");
            super.updateItem(treeNodeObject, empty);
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
                    setGraphic(getTreeItem().getGraphic());
                }
            }
        }

        private void createTextField() {
            logger.trace("Creating text field");
            textField = new TextField(getString());
            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        getItem().setName(textField.getText());
                        commitEdit(getItem());
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }

}

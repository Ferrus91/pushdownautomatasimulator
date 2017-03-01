package Controller.ControllerLogic;

import GraphicsElements.EditingCellChar;
import GraphicsElements.EditingCellString;
import GraphicsElements.EditingCellTransitionChar;
import GraphicsElements.RightClickableRow;
import Model.DrawingModel.EditTransitions;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.log4j.Logger;


/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 25/08/13
 * Time: 02:20
 * To change this template use File | Settings | File Templates.
 */
public class TransitionTableBuilder {
    static Logger logger = Logger.getLogger(TransitionTableBuilder.class);
    public static ObservableList<EditTransitions> INPUTITEMS;

    /**
     * creates a transition table for the popup view with the correct columns
     * @param transitionTable
     * @param transCharCol
     * @param pushStringCol
     * @param popCharCol
     * @param data
     * @return a table view with the correct columns
     */

    public static TableView build(final TableView transitionTable, TableColumn transCharCol, TableColumn pushStringCol,
    TableColumn popCharCol, ObservableList<EditTransitions> data)
    {
        logger.trace("Transition table built");
        INPUTITEMS = data;

        transitionTable.setEditable(true);

        transCharCol.setCellFactory(new Callback<TableColumn, TableCell>() {
            public TableCell call(TableColumn p) {
                return new EditingCellTransitionChar(INPUTITEMS);
            }
        });

        transCharCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<EditTransitions, Character>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<EditTransitions, Character> editTransitionsCharacterCellEditEvent) {
                        (editTransitionsCharacterCellEditEvent.getTableView().getItems().get(
                                editTransitionsCharacterCellEditEvent.getTablePosition().getRow())
                        ).setTransitionChar(editTransitionsCharacterCellEditEvent.getNewValue());
                    }
                }
        );

        transCharCol.setCellValueFactory(
                new PropertyValueFactory<EditTransitions, Character>("transitionChar"));

        pushStringCol.setCellFactory(new Callback<TableColumn, TableCell>() {
            public TableCell call(TableColumn p) {
                return new EditingCellString();
            }
        });

        pushStringCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<EditTransitions, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<EditTransitions, String> editTransitionsCharacterCellEditEvent) {
                        (editTransitionsCharacterCellEditEvent.getTableView().getItems().get(
                                editTransitionsCharacterCellEditEvent.getTablePosition().getRow())
                        ).setPushString(editTransitionsCharacterCellEditEvent.getNewValue());
                    }
                }
        );

        pushStringCol.setCellValueFactory(
                new PropertyValueFactory<EditTransitions, String>("pushString"));

        popCharCol.setCellFactory(new Callback<TableColumn, TableCell>() {
            public TableCell call(TableColumn p) {
                return new EditingCellChar();
            }
        });

        popCharCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<EditTransitions, Character>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<EditTransitions, Character> editTransitionsCharacterCellEditEvent) {
                        (editTransitionsCharacterCellEditEvent.getTableView().getItems().get(
                                editTransitionsCharacterCellEditEvent.getTablePosition().getRow())
                        ).setPopChar(editTransitionsCharacterCellEditEvent.getNewValue());
                    }
                }
        );

        popCharCol.setCellValueFactory(
                new PropertyValueFactory<EditTransitions, String>("popChar"));

        transitionTable.setRowFactory(new Callback<TableView, TableRow>() {
            @Override
            public TableRow call(TableView tableView) {
                return new RightClickableRow(transitionTable, INPUTITEMS);
            }
        }
        );

        return transitionTable;
    }

}

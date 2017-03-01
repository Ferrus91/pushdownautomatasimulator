package Controller.RunnerActionStrategies;

import DependencyInjection.EventBusServer.IEventBusServer;
import EventObjects.NewStringEvent;
import javafx.animation.Timeline;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 24/07/13
 * Time: 21:27
 * To change this template use File | Settings | File Templates.
 */
public class ActionStrategy implements IActionStrategy {
    static Logger logger = Logger.getLogger(ActionStrategy.class);

    private static final String CSVSPLITTER = ",";
    private static final String LINEBREAK = System.lineSeparator();

    /**
     * adds textfields to the input box which get displayed in the list view
     * @param userInputBox
     * @param inputItems
     */
    @Override
    public void addToQueue(TextField userInputBox, ObservableList<String> inputItems) {
        if(userInputBox.getCharacters().length() > 0)
        {
            logger.trace("Adding text: " + userInputBox.getCharacters().toString());
            String userInput = userInputBox.getCharacters().toString();
            inputItems.add(userInput);
            userInputBox.clear();
        }
    }

    /**
     * Takes a csv files and adds them to input item list to be shown on the main page
     * @param inputItems
     */
    @Override
    public void extractFile(ObservableList<String> inputItems) {
        // adapted from http://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/

        FileChooser batchSelector = new FileChooser();
        batchSelector.setTitle("Pick a batch csv file");
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        batchSelector.getExtensionFilters().add(csvFilter);
        File selectedFile = batchSelector.showOpenDialog(null);

        if(selectedFile != null)
        {
            try {
                logger.trace("Reading in values from the CSV file: " + selectedFile.getAbsoluteFile());
                String readIn;
                String[] inputs;
                BufferedReader selectedReader = new BufferedReader(new FileReader(selectedFile));

                while ((readIn = selectedReader.readLine()) != null) {
                    inputs = readIn.split(CSVSPLITTER);
                    for(String input : inputs)
                    {
                        inputItems.add(input);
                    }
                }

            } catch (FileNotFoundException e) {
                logger.error("No file for csv");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    @Override
    public void fastForward(Property<Boolean> pdaFinished, ObservableList<String> inputItems, TextArea textBox,
                            Timeline timelineFastForward, IEventBusServer eventBus) {
        logger.trace("Start the fastfoward timeline");
        if(pdaFinished.getValue())
        {
            if(inputItems.size() > 0)
            {
                pdaFinished.setValue(false);
                eventBus.post(new NewStringEvent(PopListView(inputItems)));
                timelineFastForward.playFromStart();
            }
            else
            {
                textBox.appendText("No strings to process" + LINEBREAK);
            }
        }
        else
        {
            timelineFastForward.play();
        }
    }

    private String PopListView(ObservableList<String> inputItems)
    {
        String returnString;

        if(inputItems.size() > 0)
            returnString = inputItems.get(0);
        else
            return null;

        inputItems.remove(0);

        return returnString;
    }

    @Override
    public void forward(Property<Boolean> pdaFinished, ObservableList<String> inputItems, TextArea textBox,
                        Timeline timelineForward, IEventBusServer eventBus) {

        logger.trace("Start the forward timeline");
        if(pdaFinished.getValue())
        {
            if(inputItems.size() > 0)
            {
                pdaFinished.setValue(false);
                eventBus.post(new NewStringEvent(PopListView(inputItems)));
            }
            else
            {
                textBox.appendText("No strings to process" + LINEBREAK);
            }
        }
        else
        {
            timelineForward.playFromStart();
        }
    }
}

package XStreamStaging;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 19/08/13
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
public interface ISerialize {
        public String serialize(StagingAutomata stagingAutomata);
        public StagingAutomata deserialize(String xml);

    boolean validateXML(String xml);
}

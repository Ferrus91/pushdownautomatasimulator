package XStreamStaging;


import Utils.DialogueUtils;
import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Samuel Keays
 * Date: 19/08/13
 * Time: 15:01
 * To change this template use File | Settings | File Templates.
 */
public class Serialize implements  ISerialize {
    static Logger logger = Logger.getLogger(Serialize.class);

    XStream xs = new XStream();

    public Serialize()
    {
        xs.autodetectAnnotations(true);
        xs.processAnnotations(StagingAutomata.class);
    }

    /**
     * turn a staging automaton into an xml string
     * @param stagingAutomata
     * @return
     */
    @Override
    public String serialize(StagingAutomata stagingAutomata) {
        logger.trace("Serialising automaton");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            xs.toXML(stagingAutomata, writer);
            return outputStream.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * deserialise an xml to an automaton
     * @param xml
     * @return
     */
    @Override
    public StagingAutomata deserialize(String xml) {
        logger.trace("Deserialising automaton");
        if(validateXML(xml))
            return (StagingAutomata)xs.fromXML(xml);
        else
        {
            logger.error("Validation failed");
            DialogueUtils.warningMessage("XML doesn't validate");
        }
        return null;
    }

    /**
     * validate the xml with the string
     * @param xml
     * @return
     */
    @Override
    public boolean validateXML(String xml)
    {
        //Built using adapted code from http://www.journaldev.com/895/how-to-validate-xml-against-xsd-in-java

        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            InputStream xsdIs = Serialize.class.getClass().getResourceAsStream("/XStreamStaging/pda.xsd");
            StreamSource xsd = new StreamSource(xsdIs);
            Schema schema = factory.newSchema(xsd);
            Validator validator = schema.newValidator();
            StringReader reader = new StringReader(xml);
            validator.validate(new StreamSource(reader));
        } catch (SAXException e) {
            e.getStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}

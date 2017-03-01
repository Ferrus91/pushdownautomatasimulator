package AppLoaders;

import DrawingTemplate.TransitionDrawingObject;
import Model.AnimationModel.IAutomata;
import Model.AnimationModel.TransitionException;
import XStreamStaging.Serialize;
import XStreamStaging.StagingAutomata;
import XStreamStaging.StagingLoader;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.google.common.io.CharStreams;
import org.apache.log4j.Logger;
import org.h2.Driver;

import java.io.IOException;
import java.io.Reader;
import java.net.URLDecoder;
import java.sql.*;
import java.util.Properties;

public class ConsoleApp {
    static Logger logger = Logger.getLogger(ConsoleApp.class);

    private static String DATABASE_CLASS;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static String CONSOLE_QUERY;
    private static String XML_ROW_NAME;


    public static void main(String args[]) {
        logger.trace("Starting console application");
        ConsoleCommanderParams ccp = new ConsoleCommanderParams();
        JCommander cmd = new JCommander(ccp);
        Serialize serialize = new Serialize();

        try {
            cmd.parse(args);

            Properties properties = new Properties();
            properties.load(TransitionDrawingObject.class.getClassLoader()
                    .getResourceAsStream("Properties/database.properties"));
            DATABASE_CLASS = properties.getProperty("databaseclass");
            URL = URLDecoder.decode(properties.getProperty("url"), "UTF-8");
            USERNAME = properties.getProperty("username");
            PASSWORD = properties.getProperty("password");
            CONSOLE_QUERY = properties.getProperty("consolequery");
            XML_ROW_NAME = properties.getProperty("xmlrowname");
            Class.forName(DATABASE_CLASS);
            DriverManager.registerDriver(new Driver());
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(CONSOLE_QUERY);
            preparedStatement.setString(1,ccp.getAutomataName());
            preparedStatement.setString(1,ccp.getDirectoryName());
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            Clob clob = rs.getClob(XML_ROW_NAME);
            Reader reader = clob.getCharacterStream();
            String xml = CharStreams.toString(reader);
            StagingAutomata stagingAutomata = serialize.deserialize(xml);
            StagingLoader stagingLoader = new StagingLoader();
            IAutomata testAutomata = stagingLoader.stagingAutomataToAutomata(stagingAutomata);

            for (char symbol : args[0].toCharArray()) {
                testAutomata.performTransition(symbol);
            }
            if(testAutomata.hasNonDeterministicJumps())
                testAutomata.popAutomata();
            else
            {
                if(testAutomata.hasSucceeded())
                    System.out.println("Valid input");
                else
                    System.out.println("Invalid input");
            }
            logger.trace("Closing console application");
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (TransitionException e) {
            System.out.println(e.getMessage());
        }
        catch (ParameterException ex) {
            System.out.println(ex.getMessage());
            cmd.usage();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
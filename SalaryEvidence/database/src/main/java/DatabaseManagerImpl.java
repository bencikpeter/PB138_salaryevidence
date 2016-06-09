package main.java;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmldb.api.base.Database;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by oldrichkonecny on 13.05.16.
 */
public class DatabaseManagerImpl implements DatabaseManager{

    private static final Logger logger = Logger.getLogger(
            DatabaseManagerImpl.class.getName());

    @Override
    public void createRecord(Day day) throws DatabaseFailureException {
        validate(day);

        final String driver = "org.exist.xmldb.DatabaseImpl";

        try {


            // initialize database driver
            Class cl = Class.forName(driver);
            Database database = (Database) cl.newInstance();
            database.setProperty("create-database", "true");
            //DatabaseManager.registerDatabase(database);
        }catch (Exception e) {

        }

    }

    @Override
    public void updateRecord(Day day) throws DatabaseFailureException {

    }

    @Override
    public void deleteRecord(Day day) throws DatabaseFailureException {

    }

    @Override
    public void deleteRecord(long unixDate) throws DatabaseFailureException {

    }

    @Override
    public List<Day> findRecord(long date) throws DatabaseFailureException {
        return null;
    }

    @Override
    public List<Day> findRecord(long from, long to) throws DatabaseFailureException {
        return null;
    }

    @Override
    public Day getOldest() throws DatabaseFailureException {
        return null;
    }

    @Override
    public Day getNewest() throws DatabaseFailureException {
        return null;
    }

    private void validate(Day day) {
        if (day == null) {
            throw new IllegalArgumentException("day is null");
        }
        if (day.getDate() < 0 ) {
            throw new ValidateException("date in day is negative");
        }
        if (day.getHours() < 0  && day.getHours() > 24 ) {
            throw new ValidateException("hours  in day are wrong");
        }
        if (day.getJob() == null ) {
            throw new ValidateException("enum job is null");
        }
    }

    private File createFile(Day day) {
        try {
            File tmp = File.createTempFile(day.getDate().toString(), ".xml");
            tmp.deleteOnExit();

            BufferedWriter bw = new BufferedWriter(new FileWriter(tmp));
            bw.write(makeXMLString(day));
            bw.close();
            return tmp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String makeXMLString(Day day) {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            Element root = doc.createElement("day");
            root.setAttribute("date", day.getDate().toString() );
            doc.appendChild(root);

            Element hoursElem = doc.createElement("hours");
            hoursElem.appendChild(doc.createTextNode(String.valueOf(day.getHours())));
            root.appendChild(hoursElem);

            Element jobElem = doc.createElement("job");
            jobElem.appendChild(doc.createTextNode(day.getJob().toString()));
            root.appendChild(jobElem);

            Element perhourElem = doc.createElement("perhour");
            perhourElem.appendChild(doc.createTextNode(String.valueOf(day.getPerHours())));
            root.appendChild(perhourElem);
            return doc.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}

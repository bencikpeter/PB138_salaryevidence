package main.java;

import org.exist.xmldb.EXistResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by oldrichkonecny on 13.05.16.
 */
public class DatabaseManagerImpl implements DatabaseManager{

    private static final Logger logger = Logger.getLogger(
            DatabaseManagerImpl.class.getName());


    private static final long DAY_LENGTH = 86400L;
    private static final String FILE_EXTENSION = ".xml";
    private static final String CONFIG_PATH = "SalaryEvidence/database/src/main.java/config.properties";
    private static String URI;
    private static String recordsColectionPath;




    public DatabaseManagerImpl() {
        Properties config = new Properties();
        InputStream input;

        try {
            input = new FileInputStream(CONFIG_PATH);
            config.load(input);
            this.setupDatabase(config.getProperty("xmldb.driver"));
            this.URI = config.getProperty("xmldb.uri");
            this.recordsColectionPath = config.getProperty("xmldb.recordCollection");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setupDatabase(String driver) {
        try {

            Class cl = Class.forName(driver);
            Database database = (Database) cl.newInstance();
            database.setProperty("create-database", "true");
            org.xmldb.api.DatabaseManager.registerDatabase(database);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (XMLDBException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void createRecord(Day day) throws DatabaseFailureException {
        validate(day);
        Collection col = null;
        XMLResource res = null;
        File f;
        try {
            col = org.xmldb.api.DatabaseManager.getCollection(URI + "/db/sample");
            res = (XMLResource)col.getResource(String.valueOf(day.getDate())+FILE_EXTENSION);
            if (res == null) {
                res = (XMLResource) col.createResource(day.getDate().toString() + FILE_EXTENSION, "XMLResource");
            }

            f = createFile(day);
            res.setContent(f);
            col.storeResource(res);
        } catch (XMLDBException e) {
            e.printStackTrace();
        } finally {
            if(res != null) {
                try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
            }
            if(col != null) {
                try { col.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
            }
        }
    }

    @Override
    public void deleteRecord(Day day) throws DatabaseFailureException {
        if (day.getDate() == null) {
            throw  new IllegalArgumentException("date in day is null");
        }
        this.deleteRecord(day.getDate());
    }

    @Override
    public void deleteRecord(long unixDate) throws DatabaseFailureException {
        Collection col = null;
        XMLResource res;
        try {
            col = org.xmldb.api.DatabaseManager.getCollection(URI + "/db/sample");
            col.setProperty(OutputKeys.INDENT, "no");
            res = (XMLResource)col.getResource(String.valueOf(unixDate)+FILE_EXTENSION);
            col.removeResource(res);

        } catch (XMLDBException e) {
            e.printStackTrace();
        } finally {
            if(col != null) {
                try { col.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
            }
        }
    }

    @Override
    public List<Day> findRecord(long date) throws DatabaseFailureException {
        List<Day> list = new ArrayList<>();
        Day day;
        Collection col = null;
        XMLResource res = null;

        try {
            col = org.xmldb.api.DatabaseManager.getCollection(URI + "/db/sample");
            col.setProperty(OutputKeys.INDENT, "no");
            res = (XMLResource)col.getResource(String.valueOf(date)+FILE_EXTENSION);
            if (res == null) {
                return list;
            }

            day = dayFromResource(res);

            list.add(day);
            return list;

        } catch (XMLDBException e) {
            e.printStackTrace();
        } finally {
            if(col != null) {
                try { col.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
            }
        }
        return null;
    }

    @Override
    public List<Day> findRecord(long from, long to) throws DatabaseFailureException {
        List<Day> retList = new ArrayList<>();

        for (long i = from; i <= to; i+= this.DAY_LENGTH) {
            List<Day> list = findRecord(i);
            retList.addAll(list);
        }
        return retList;
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
            File tmp = File.createTempFile(String.valueOf(day.getDate()), FILE_EXTENSION);
            tmp.deleteOnExit();

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result result = new StreamResult(tmp);
            Source input = new DOMSource(this.makeXMLString(day));
            transformer.transform(input, result);

            return tmp;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Document makeXMLString(Day day) {
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
            return doc;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return  null;
    }

    private Day dayFromResource(XMLResource res) {
        Day day;

        try {
            DaySAXHandler handler = new DaySAXHandler();
            res.getContentAsSAX(handler);
            day = handler.getDay();

            return day;
        } catch (XMLDBException e) {
            e.printStackTrace();
        }
        return  null;
    }
}

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

    private static final Logger logger = Logger.getLogger(DatabaseManagerImpl.class.getName());

    private static final long DAY_LENGTH = 86400L;
    private static final String FILE_EXTENSION = ".xml";
    private static final String CONFIG_PATH = "SalaryEvidence/database/src/main.java/config.properties";
    private static String URI;
    private static String recordsCollectionPath;





    public DatabaseManagerImpl() throws DatabaseFailureException {
        Properties config = new Properties();
        InputStream input;

        try {
            logger.log(Level.INFO, "Reading config file");
            input = new FileInputStream(CONFIG_PATH);
            config.load(input);
            this.setupDatabase(config.getProperty("xmldb.driver"));
            this.URI = config.getProperty("xmldb.uri");
            this.recordsCollectionPath = config.getProperty("xmldb.recordCollection");

        } catch (FileNotFoundException e) {
            String msg = "Error, config file not found no path: "+ CONFIG_PATH;
            logger.log(Level.SEVERE, msg, e);
            throw new IllegalArgumentException("Config file not found");
        } catch (IOException e) {
            String msg = "Error, cannot read from config file.";
            logger.log(Level.SEVERE, msg, e);
            throw new DatabaseFailureException("cannot read from config file");
        }


    }

    public void setupDatabase(String driver) {
        try {
            logger.log(Level.INFO, "Setting up database connection");
            Class cl = Class.forName(driver);
            Database database = (Database) cl.newInstance();
            database.setProperty("create-database", "true");
            org.xmldb.api.DatabaseManager.registerDatabase(database);

        } catch (IllegalAccessException e) {
            String msg = "Error, cannot access to database (probably permission denied).";
            logger.log(Level.SEVERE, msg, e);
        } catch (InstantiationException e) {
            String msg = "Error, instantiation exception.";
            logger.log(Level.SEVERE, msg, e);
        } catch (XMLDBException e) {
            String msg = "Error, database probably does not exist.";
            logger.log(Level.SEVERE, msg, e);
        } catch (ClassNotFoundException e) {
            String msg = "Error, class for driver not found.";
            logger.log(Level.SEVERE, msg, e);
        }
    }


    @Override
    public void createRecord(Day day) throws DatabaseFailureException {
        logger.log(Level.INFO, "Creating document and storing to db");
        validate(day);
        Collection col = null;
        XMLResource res = null;
        File f;
        try {
            logger.log(Level.INFO, "Getting collection with xml documents");
            col = org.xmldb.api.DatabaseManager.getCollection(URI + recordsCollectionPath);
            res = (XMLResource)col.getResource(String.valueOf(day.getDate())+FILE_EXTENSION);
            if (res == null) {
                res = (XMLResource) col.createResource(day.getDate().toString() + FILE_EXTENSION, "XMLResource");
            }

            f = createFile(day);
            res.setContent(f);
            logger.log(Level.INFO, "Storing document to db..");
            col.storeResource(res);
        } catch (XMLDBException e) {
            throw new DatabaseFailureException("Error when creating record in database", e);
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
        logger.log(Level.INFO, "Trying to delete document from db");
        Collection col = null;
        XMLResource res;
        try {
            logger.log(Level.INFO, "Getting collection with xml documents");
            col = org.xmldb.api.DatabaseManager.getCollection(URI + recordsCollectionPath);
            col.setProperty(OutputKeys.INDENT, "no");
            res = (XMLResource)col.getResource(String.valueOf(unixDate)+FILE_EXTENSION);
            logger.log(Level.INFO, "Deleting document");
            col.removeResource(res);

        } catch (XMLDBException e) {
            throw new DatabaseFailureException("Error when deleting from database", e);
        } finally {
            if(col != null) {
                try { col.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
            }
        }
    }

    @Override
    public List<Day> findRecord(long date) throws DatabaseFailureException {
        logger.log(Level.INFO, "Trying to find document in db");
        List<Day> list = new ArrayList<>();
        Day day;
        Collection col = null;
        XMLResource res = null;

        try {
            logger.log(Level.INFO, "Getting collection with xml documents");
            col = org.xmldb.api.DatabaseManager.getCollection(URI + recordsCollectionPath);
            col.setProperty(OutputKeys.INDENT, "no");
            res = (XMLResource)col.getResource(String.valueOf(date)+FILE_EXTENSION);
            if (res == null) {
                return list;
            }

            day = dayFromResource(res);

            list.add(day);
            return list;

        } catch (XMLDBException e) {
            throw new DatabaseFailureException("Error when finding record in database. Record id: "+ date + ". ", e);
        } finally {
            if(col != null) {
                try { col.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
            }
        }
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

    private void validate(Day day) {
        logger.log(Level.INFO, "Validating day to be store in db");
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

    private File createFile(Day day) throws DatabaseFailureException {
        logger.log(Level.INFO, "Creating temp file to be stored in db");
        try {
            File tmp = File.createTempFile(String.valueOf(day.getDate()), FILE_EXTENSION);
            tmp.deleteOnExit();

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result result = new StreamResult(tmp);
            Source input = new DOMSource(this.makeXMLString(day));
            transformer.transform(input, result);

            return tmp;
        } catch (IOException e) {
            throw new DatabaseFailureException("Error when creating temp file to be stored in db", e);
        } catch (TransformerConfigurationException e) {
            throw new DatabaseFailureException("Error transformation DOM to file wrong configuration", e);
        } catch (TransformerException e) {
            throw new DatabaseFailureException("Error when transforming(writing) DOM to temp file", e);
        }
    }

    private Document makeXMLString(Day day) throws DatabaseFailureException {
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
            throw new DatabaseFailureException("Error when creating DOM", e);
        }
    }

    private Day dayFromResource(XMLResource res) throws DatabaseFailureException {
        logger.log(Level.INFO, "Parsing XMLResource to Day class");
        Day day;
        try {
            DaySAXHandler handler = new DaySAXHandler();
            res.getContentAsSAX(handler);
            day = handler.getDay();

            return day;
        } catch (XMLDBException e) {
            throw new DatabaseFailureException("Error when parsing XMLResource using SAX", e);
        }
    }
}

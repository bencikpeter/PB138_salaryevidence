package Database;


import Logic.DatabaseFailureException;
import Logic.ValidateException;
import Logic.Day;
//import org.exist.xmldb.EXistResource;
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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

/**
 * Created by oldrichkonecny on 13.05.16.
 */
public class DatabaseManagerImpl implements DatabaseManager{

    private static final Logger logger = Logger.getLogger(DatabaseManagerImpl.class.getName());

    private static final long DAY_LENGTH = 86400L;
    private static final String FILE_EXTENSION = ".xml";
    //private static final String CONFIG_PATH = "SalaryEvidence/database/src/main.java/config.properties";
    private static final String CONFIG_PATH = "SalaryEvidence/src/main/java/Database/config.properties";
    
    private static String URI;
    private static String recordsCollectionPath;


    /**
     * Constructor, read config.properties file initialize IRI and recordsCollectionPath,
     * also set up connection with database
     * @throws DatabaseFailureException when any error
     */
    public DatabaseManagerImpl() throws DatabaseFailureException {
        Properties config = new Properties();
        InputStream input;

        try {
            logger.log(Level.INFO, "Reading config file");
            input = new FileInputStream(CONFIG_PATH);
            config.load(input);
            this.setupDatabase(config.getProperty("xmldb.driver"));
            URI = config.getProperty("xmldb.uri");
            recordsCollectionPath = config.getProperty("xmldb.recordCollection");

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

    /**
     * set up connection with database
     * @param driver String name of used driver
     */
    private void setupDatabase(String driver) {
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
                res = (XMLResource) col.createResource(String.valueOf(day.getDate()) + FILE_EXTENSION, "XMLResource");
            }

            f = createFile(day);
            res.setContent(f);
            logger.log(Level.INFO, "Storing document to db..");
            col.storeResource(res);
        } catch (XMLDBException e) {
            throw new DatabaseFailureException("Error when creating record in database", e);
        } finally {
            if(res != null) {
                //try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
            }
            if(col != null) {
                try { col.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
            }
        }
    }

    @Override
    public void deleteRecord(Day day) throws DatabaseFailureException {
        if (day == null) {
            throw  new IllegalArgumentException("day is null");
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
        XMLResource res;

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

        for (long i = from; i <= to; i+= DAY_LENGTH) {
            List<Day> list = findRecord(i);
            retList.addAll(list);
        }
        return retList;
    }

    /**
     * validate day, check if date, hours and job are correct
     * @param day day to be validate
     */
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

    /**
     * create temp file to be store in db, tem file is deleted on exit
     * @param day Day from which temp file will be created
     * @return temp file
     * @throws DatabaseFailureException when error
     */
    private File createFile(Day day) throws DatabaseFailureException {
        logger.log(Level.INFO, "Creating temp file to be stored in db");
        try {
            File tmp = File.createTempFile(String.valueOf(day.getDate()), FILE_EXTENSION);
            tmp.deleteOnExit();
            
            transformer(new DOMSource(this.makeXMLString(day)), new StreamResult(tmp));

            return tmp;
        } catch (IOException e) {
            throw new DatabaseFailureException("Error when creating temp file to be stored in db", e);
        } catch (TransformerConfigurationException e) {
            throw new DatabaseFailureException("Error transformation DOM to file wrong configuration", e);
        } catch (TransformerException e) {
            throw new DatabaseFailureException("Error when transforming(writing) DOM to temp file", e);
        }
    }

    /**
     * Create xml document from day
     * @param day Day from which xml document will be created
     * @return xml document
     * @throws DatabaseFailureException when error
     */
    private Document makeXMLString(Day day) throws DatabaseFailureException {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            Element root = doc.createElement("day");
            root.setAttribute("date", String.valueOf(day.getDate()));
            doc.appendChild(root);

            Element hoursElem = doc.createElement("hours");
            hoursElem.appendChild(doc.createTextNode(String.valueOf(day.getHours())));
            root.appendChild(hoursElem);

            Element jobElem = doc.createElement("job");
            jobElem.appendChild(doc.createTextNode(day.getJob().toString()));
            root.appendChild(jobElem);

            return doc;

        } catch (ParserConfigurationException e) {
            throw new DatabaseFailureException("Error when creating DOM", e);
        }
    }

    /**
     * create Day class from XMLResource using SAXHandler
     * @param res XMLResource
     * @return Day class
     * @throws DatabaseFailureException when error
     */
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
    
    @Override
    public File createInvoice(List<Logic.Day> listOfDays) throws DatabaseFailureException {
        int sum= 0;
        int hodiny = 0;
        XMLResource res = null;
        Collection col = null;
        try{
            File jobsFile = new File("jobs.xml");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Element findJob = null;
            Element salary = null;

            Document docJobs = docBuilder.parse(jobsFile);
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("invoice"); //Root element
            doc.appendChild(rootElement);

            Element days = doc.createElement("days");           // days
            rootElement.appendChild(days);
            for(Logic.Day listDay : listOfDays){
                Element day = doc.createElement("day");
                day.setAttribute("date", String.valueOf(listDay.getDate() ));   // date
                days.appendChild(day);

                Element hours = doc.createElement("hours");     // hours
                hours.appendChild(doc.createTextNode(String.valueOf(listDay.getHours())));
                day.appendChild(hours);

                Element job = doc.createElement("job");
                job.appendChild(doc.createTextNode(listDay.getJob().toString())); 
                day.appendChild(job);

                findJob =(Element) docJobs.getElementsByTagName(listDay.getJob().name()).item(0);       //Get salary per hour for job
                salary = (Element) findJob.getElementsByTagName("salary").item(0);

                hodiny = hodiny + listDay.getHours();
                sum = sum +  listDay.getHours()*(Integer.parseInt(salary.getTextContent()));    // Complet salary      
            }
            Element hodinyElem = doc.createElement("sum");
            hodinyElem.appendChild(doc.createTextNode(String.valueOf(hodiny)));
            rootElement.appendChild(hodinyElem);
            Element suma = doc.createElement("salarysum");
            suma.appendChild(doc.createTextNode(String.valueOf(sum)));
            rootElement.appendChild(suma);

            long first = listOfDays.get(0).getDate();
            long last = listOfDays.get(listOfDays.size()-1).getDate();
            File tmp = File.createTempFile("faktura-"+first+"-"+last, ".xml");
            tmp.deleteOnExit();

            transformer(new DOMSource(doc),new StreamResult(tmp));
            
            col = org.xmldb.api.DatabaseManager.getCollection(URI + "/db/invoice");

            res = (XMLResource)col.createResource("faktura-"+first+"-"+last+".xml", "XMLResource");
            res.setContent(tmp);
            col.storeResource(res);

            return tmp;



        } catch (ParserConfigurationException | SAXException | IOException | DOMException | NumberFormatException ex) {
            throw new DatabaseFailureException("Error when creating temp file to be stored in db", ex);
        } catch (TransformerException | XMLDBException ex) {
            throw new DatabaseFailureException("Error when creating temp file to be stored in db", ex);
        }finally {
            if(res != null) {
                //try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
            }
            if(col != null) {
                try { col.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
            }
        }  
    }
    /**
     * Transform Source into result
     * @param input Input source
     * @param result Result
     * @throws TransformerException 
     */
    private void transformer(Source input,Result result) throws TransformerException{
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(input, result);
    }
}

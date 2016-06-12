import Logic.Day;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.exist.xmldb.EXistResource;
import org.w3c.dom.Attr;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmldb.api.*;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import static org.exist.examples.xmldb.Put.URI;
import org.w3c.dom.DOMException;

/**
 *
 * @author Peter Tirala
 */
public class AppLogicImpl {

    /**
     * Create and store new invoice xml file to database.
     * @param listOfDays Days to xml
     * @return Created file
     */
    public File createInvoice(List<Day> listOfDays) {
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
            for(Day listDay : listOfDays){
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

            Manager.transformer(new StreamResult(tmp),new DOMSource(doc));


            String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
            String driver = "org.exist.xmldb.DatabaseImpl";
            Class cl = Class.forName(driver);
            Database database = (Database) cl.newInstance();
            database.setProperty("create-database", "true");
            org.xmldb.api.DatabaseManager.registerDatabase(database);
            col = org.xmldb.api.DatabaseManager.getCollection(URI + "/db/invoice");

            res = (XMLResource)col.createResource("faktura-"+first+"-"+last+".xml", "XMLResource");
            res.setContent(tmp);
            col.storeResource(res);

            return tmp;



        } catch (ParserConfigurationException | SAXException | IOException | DOMException | NumberFormatException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(AppLogicImpl.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            if(res != null) {
                try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
            }
            if(col != null) {
                try { col.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
            }
        }
        return null;    // return path ulozeneho xml
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

    /*
    public void transformToDoc(long from,long to){
        List<Day> list = findRecord(from,to);
        File invoice = createInvoice(list);
        Transformer trans = new Transformer();
        trans.transform(invoice, );
    }*/

}
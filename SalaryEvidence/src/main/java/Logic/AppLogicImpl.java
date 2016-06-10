package Logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
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
import org.w3c.dom.Attr;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *Implements AppLogic interface.
 * @author Peter Tirala
 */
public class AppLogicImpl {

    /**
     * 
     * @param listOfDays
     * @return 
     */
    public File createInvoice(List<Day> listOfDays) {
        int sum= 0;
        int hodiny = 0;
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
                day.setAttribute("date", String.valueOf(listDay.getDate() ));   // datum
                days.appendChild(day);
                              
                Element hours = doc.createElement("hours");     // Hodiny
                hours.appendChild(doc.createTextNode(String.valueOf(listDay.getHours())));
                day.appendChild(hours);
                
                Element job = doc.createElement("job");
                job.appendChild(doc.createTextNode(listDay.getJob().name())); // Funckia pre konvert enumu na string
                day.appendChild(job);
               
                findJob =(Element) docJobs.getElementsByTagName(listDay.getJob().name()).item(0);
                salary = (Element) findJob.getElementsByTagName("salary").item(0);
                
                hodiny = hodiny + listDay.getHours();
                sum = sum +  listDay.getHours()*(Integer.parseInt(salary.getTextContent()));    // Vypocita celu sumu       
            }   
            Element hodinyElem = doc.createElement("sum");
            hodinyElem.appendChild(doc.createTextNode(String.valueOf(hodiny)));
            rootElement.appendChild(hodinyElem);
            Element suma = doc.createElement("salarysum");
            suma.appendChild(doc.createTextNode(String.valueOf(sum)));
            rootElement.appendChild(suma);
            
            File tmp = File.createTempFile("faktura-"/*+autoincrement premenna*/, ".xml");              // autoincrement aj do atributu invoice
            tmp.deleteOnExit();

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result result = new StreamResult(tmp);
            Source input = new DOMSource(doc);
            transformer.transform(input, result);
            
             Collection col = org.xmldb.api.DatabaseManager.getCollection(URI + "/db/sample");
            
            XMLResource document = (XMLResource)col.createResource(day.getDate().toString(), "XMLResource");
             document.setContent(tmp);
             col.storeResource(document);
             
            return tmp;
            
            
      
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(AppLogicImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(AppLogicImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(AppLogicImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;    // return path ulozeneho xml
    }
}

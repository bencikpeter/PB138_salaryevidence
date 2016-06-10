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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
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
public class AppLogicImpl implements AppLogic{

    @Override
    public Path createInvoice(List<Day> listOfDays) {
        int sum= 0;
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
                Element day = doc.createElement("day");         // Jednotlivy den
                days.appendChild(day);
                
                Attr attr = doc.createAttribute("date");            // Atribut dna - datum - primary key
                attr.setValue(Long.toString(listDay.getDate()));                        
                day.setAttributeNode(attr);
                
                Element hours = doc.createElement("hours");     // Hodiny
                hours.appendChild(doc.createTextNode(String.valueOf(listDay.getHours())));
                day.appendChild(hours);
                
                Element job = doc.createElement("job");
                job.appendChild(doc.createTextNode(listDay.getJob().name())); // Funckia pre konvert enumu na string
                day.appendChild(job);
               
                findJob =(Element) docJobs.getElementsByTagName(listDay.getJob().name()).item(0);
                salary = (Element) findJob.getElementsByTagName("salary").item(0);
                
                sum = sum +  listDay.getHours()*(Integer.parseInt(salary.getTextContent()));    // Vypocita celu sumu       
            }   
            Element suma = doc.createElement("suma");
            suma.appendChild(doc.createTextNode(String.valueOf(sum)));
            rootElement.appendChild(suma);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
   //         StreamResult result = new StreamResult(new File("C:\\file.xml"));               //ukladanie do DB
            
        }catch(ParserConfigurationException pce){
            pce.printStackTrace();
        } catch (TransformerConfigurationException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            Logger.getLogger(AppLogicImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AppLogicImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;    // return path ulozeneho xml
    }
}

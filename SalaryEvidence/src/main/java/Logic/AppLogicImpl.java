package Logic;

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

/**
 *Implements AppLogic interface.
 * @author Peter Tirala
 */
public class AppLogicImpl implements AppLogic{

    @Override
    public Path createInvoice(List<Day> listOfDays) {
        int sum= 0;
        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("faktura"); //Root element
            doc.appendChild(rootElement);
            
            Element days = doc.createElement("days");           // days
            rootElement.appendChild(days);
            for(Day listDay : listOfDays){
                Element day = doc.createElement("day");         // Jednotlivy den
                days.appendChild(day);
                
                Attr attr = doc.createAttribute("date");            // Atribut dna - datum - primary key
                attr.setValue("String date");                           // Funkciu pre konvert date na string !
                day.setAttributeNode(attr);
                
                Element hours = doc.createElement("hours");     // Hodiny
                hours.appendChild(doc.createTextNode(String.valueOf(listDay.getHours())));
                day.appendChild(hours);
                
                Element job = doc.createElement("job");
                job.appendChild(doc.createTextNode("job")); // Funckia pre konvert enumu na string
                day.appendChild(job);
                
               // sum + = listDay.getHours()*listDay.getJob();      // Vypocita celu sumu       
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
        }
        return null;    // return path ulozeneho xml
    }
}

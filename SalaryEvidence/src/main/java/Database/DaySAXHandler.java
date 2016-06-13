package Database;

import Logic.Day;
import Logic.Jobs;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by olda on 12.06.2016.
 */
public class DaySAXHandler extends DefaultHandler {

    private Day day = null;
    private boolean bHours;
    private boolean bJob;

    public Day getDay() {
        return this.day;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("day")) {
            String sName = attributes.getValue("date");
            day = new Day();
            day.setDate(Long.parseLong(sName));
        }else if (qName.equalsIgnoreCase("hours")) {
            bHours = true;
        }else if (qName.equalsIgnoreCase("job")) {
            bJob = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("day")) {

        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (bHours) {
            day.setHours(Integer.parseInt(new String(ch, start, length)));
            bHours = false;
        }else if (bJob) {
            day.setJob(Jobs.valueOf(new String(ch, start, length)));
            bJob = false;
        }

    }
}

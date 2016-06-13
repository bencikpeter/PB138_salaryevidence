package cz.muni.fi.pb138.salaryevidence.transformations;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

/**
 * @author xbencik1
 */
public class XmlToDocbookTransformationImpl implements XmlToDocbookTransformation {

    private Transformer transformer;

    XmlToDocbookTransformationImpl(File xsltDef) throws TransformerConfigurationException { //bad xsltDef
        TransformerFactory fact = TransformerFactory.newInstance();
        transformer = fact.newTransformer(new StreamSource(xsltDef));
    }

    @Override
    public boolean transform(File source, File dest) {
        try {
            transformer.transform(new StreamSource(source), new StreamResult(dest));
            return true;
        } catch (TransformerException e) {
            return false; //transformation not successful
        }
    }
}

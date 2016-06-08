package main;

import org.xml.sax.SAXException;

import javax.xml.transform.TransformerConfigurationException;
import java.io.File;

/**
 * Created by bencikpeter on 09.05.16.
 */
public final class Transformations {

    private Transformations(){
        throw new UnsupportedOperationException("Transformations is utility class - cannot be instantiated");
    }

    public static XmlSchemaValidator getNewInstanceXmlSchemaValidator(File schema) throws SAXException { //bad schema
        return new XmlSchemaValidatorImpl(schema);
    }

    public static XmlToDocbookTransformation getNewInstanceXmlToDocbook(File xsltDef) throws TransformerConfigurationException { //bad xsltDef
        return new XmlToDocbookTransformationImpl(xsltDef);
    }

    public static DocbookToPdfTransformation getNewInstanceDocbookToPdf(){
        return null;
    }
}


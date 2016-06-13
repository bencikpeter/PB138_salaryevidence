package Transformations;

import org.xml.sax.SAXException;

import javax.xml.transform.TransformerConfigurationException;
import java.io.File;

/**
 * @author xbencik1
 *
 * Transfromations is a utility class that works like a factory for different kind
 * of transformators and validators. As a utility class it cannot be instatiated
 */
public final class Transformations {

    private Transformations(){
        throw new UnsupportedOperationException("Transformations is utility class - cannot be instantiated");
    }

    /**
     * Returns new instance of {@link XmlSchemaValidator}
     * @param schema XML-schema to be associated with walidator
     * @return new instance of XmlSchemaValidator
     * @throws SAXException whan provided schema is not correct
     */
    public static XmlSchemaValidator getNewInstanceXmlSchemaValidator(File schema) throws SAXException { //bad schema
        return new XmlSchemaValidatorImpl(schema);
    }

    /**
     * Returns new instance of {@link XmlToDocbookTransformation}
     * @param xsltDef definition of transformation to be performed
     * @return new instance of XmlToDocbook
     * @throws TransformerConfigurationException when provided transformation is not correct
     */
    public static XmlToDocbookTransformation getNewInstanceXmlToDocbook(File xsltDef) throws TransformerConfigurationException { //bad xsltDef
        return new XmlToDocbookTransformationImpl(xsltDef);
    }

    /**
     * Returns new instance of {@link DocbookToPdfTransformation}
     * @return new instance of DocbookToPdfTransformation
     */
    public static DocbookToPdfTransformation getNewInstanceDocbookToPdf(){
        return null;
    }
}


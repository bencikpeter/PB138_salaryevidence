package main;

import org.xml.sax.SAXException;

import java.io.File;

/**
 * Created by bencikpeter on 09.05.16.
 */
public final class Transformations {

    private Transformations(){
        throw new UnsupportedOperationException("Transformations is utility class - cannot be instantiated");
    }

    public static XmlSchemaValidator getNewInstanceXmlSchemaValidator(File schema) throws SAXException {
        return new XmlSchemaValidatorImpl(schema);
    }

    public static XmlToDocbookTransformation getNewInstanceXmlToDocbook(){
        return null;
    }

    public static DocbookToPdfTransformation getNewInstanceDocbookToPdf(){
        return null;
    }
}


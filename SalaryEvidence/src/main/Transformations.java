package main;

/**
 * Created by bencikpeter on 09.05.16.
 */
public interface Transformations {
    XmlSchemaValidator getNewInstanceXmlSchemaValidator(/*XML SCHEMA FILE*/);

    XmlToDocbookTransformation getNewInstanceXmlToDocbook();

    DocbookToPdfTransformation getNewInstanceDocbookToPdf();
}


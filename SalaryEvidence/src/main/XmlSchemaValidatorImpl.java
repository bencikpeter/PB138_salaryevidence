package main;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by bencikpeter on 06.06.16.
 */
public class XmlSchemaValidatorImpl implements XmlSchemaValidator {
    private Schema schema;

    public XmlSchemaValidatorImpl(File fSchema) throws SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schema = schemaFactory.newSchema(fSchema);
    }

    @Override
    public boolean validate(File file) throws IOException {
        try {
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new FileInputStream(file)));
            return true;
        } catch (SAXException ex) {
            return false;
        }
    }
}

package test;

import Transformations.XmlSchemaValidator;
import Transformations.XmlSchemaValidatorImpl;
import org.xml.sax.SAXException;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author xbencik1
 */
public class XmlSchemaValidatorImplTest {

    @org.junit.Test
    public void testValidSchema() throws Exception {
        XmlSchemaValidator validator = new XmlSchemaValidatorImpl(new File("src/test/resources/validSchema.xsl"));
    }
    @org.junit.Test
    public void testInvalidSchema() throws Exception {
        try {
            XmlSchemaValidator validator = new XmlSchemaValidatorImpl(new File("src/test/resources/invalidSchema.xsl"));
            fail("Invalid schema should cause exception");
        } catch (SAXException ex) {

        }

    }
    @org.junit.Test
    public void testValidateValid() throws Exception {
        XmlSchemaValidator validator = new XmlSchemaValidatorImpl(new File("src/test/resources/validSchema.xsl"));
        boolean result = validator.validate(new File("src/test/resources/valid.xml"));
        assertThat("file should validate",result,is(equalTo(true)));
    }

    @org.junit.Test
    public void testValidateInvalid() throws Exception {
        XmlSchemaValidator validator = new XmlSchemaValidatorImpl(new File("src/test/resources/validSchema.xsl"));
        boolean result = validator.validate(new File("src/test/resources/invalid.xml"));
        assertThat("file should not validate",result,is(equalTo(false)));
    }


}

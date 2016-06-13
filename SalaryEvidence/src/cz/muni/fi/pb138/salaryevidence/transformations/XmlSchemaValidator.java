package cz.muni.fi.pb138.salaryevidence.transformations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author xbencik1
 *
 * XmlSchemaValidator is an interface for getting a validator againts a given schema
 */
public interface XmlSchemaValidator {

    /**
     * Validates a file against given schema
     *
     * @param file to be validated against schema
     * @return true if file is valid against schema, false otherwise
     * @throws IOException when file cannot be accessed
     */
    boolean validate(File file) throws IOException;
}

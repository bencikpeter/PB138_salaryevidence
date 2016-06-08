package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by bencikpeter on 09.05.16.
 */
public interface XmlSchemaValidator {
    boolean validate(File file) throws IOException;
}

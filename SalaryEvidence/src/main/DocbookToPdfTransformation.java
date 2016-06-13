package main;

import javax.xml.transform.TransformerConfigurationException;
import java.io.File;

/**
 * Created by bencikpeter on 09.05.16.
 */
public interface DocbookToPdfTransformation {
    boolean transform(File source, File dest) throws TransformerConfigurationException;
}

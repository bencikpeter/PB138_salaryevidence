package Transformations;



import javax.xml.transform.TransformerConfigurationException;
import java.io.File;

/**
 * @author xbencik1
 *
 * DocbookToPdfTransformation interface provides an outline to how transformator to PDF should work
 * Provides only transform method
 */
public interface DocbookToPdfTransformation {

    /**
     * Transform method transform source (in docbok format) into dest (in PDF format)
     * @param source docbook file to be transformed
     * @param dest file to which the result should be stored
     * @return true if transformation succeeded, false otherwise
     *
     * If transformation fails, content of dest is unknown and should not be used.
     */
    boolean transform(File source, File dest) ;
}

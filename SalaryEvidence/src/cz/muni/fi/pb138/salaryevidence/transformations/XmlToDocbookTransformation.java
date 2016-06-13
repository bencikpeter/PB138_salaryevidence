package cz.muni.fi.pb138.salaryevidence.transformations;

import java.io.File;

/**
 * @author xbencik1
 *
 * This interface provides functionality outline of XML to Docbook transformators' instances
 */
public interface XmlToDocbookTransformation {

    /**
     * Transforms source XML file to Docbook file
     *
     * @param source XML file
     * @param dest empty Docbook file
     * @return true if transformation completed correctly, false otherwise
     *
     * If transformation fails or dest file is not empty at the beginning, the result
     * of transformation is unknown and the content of dest file should not be used.
     */
    boolean transform(File source, File dest);
}

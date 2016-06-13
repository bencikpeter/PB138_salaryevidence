package main;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by bencikpeter on 08.06.16.
 */
public class DocbookToPdfTransformationImpl implements DocbookToPdfTransformation {
    @Override
    public boolean transform(File source, File dest) throws TransformerConfigurationException {
        File dbk2Fo = new File(String.valueOf(Transformations.class.getResource("/docbook2fo/fo/docbook.xsl")));
        //XmlToDocbookTransformation transformator;
        //transformator = Transformations.getNewInstanceXmlToDocbook(dbk2Fo);
        //File fo;

        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

        try {
            //fo = File.createTempFile("tmpFo","fo");
            //transformator.transform(source,fo);

            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF,foUserAgent, new FileOutputStream(dest));
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(dbk2Fo));

            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(new StreamSource(source), res);
        } catch (IOException e) {
            e.printStackTrace();
            //TODO
        } catch (FOPException e) {
            e.printStackTrace();
            //TODO
        } catch (TransformerException e) {
            e.printStackTrace();
            //TODO
        }
        return true;
    }
}

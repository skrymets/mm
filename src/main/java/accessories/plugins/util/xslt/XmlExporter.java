/*
 * XmlExporter.java
 *
 * Created on 27 January 2004, 17:23
 */

package accessories.plugins.util.xslt;

import lombok.extern.slf4j.Slf4j;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class XmlExporter {

    public XmlExporter() {
    }

    public void transForm(File xmlFile, File xsltFile, File resultFile) throws FileNotFoundException {
        Source xmlSource = new StreamSource(xmlFile);
        // System.out.println("set xsl");
        Source xsltSource = new StreamSource(xsltFile);
        // System.out.println("set result");
        FileOutputStream resultOutputStream = new FileOutputStream(resultFile);

        // create an instance of TransformerFactory
        try (resultOutputStream) {
            try {
                Result result = new StreamResult(resultOutputStream);
                // System.out.println("make transform instance");
                TransformerFactory transFact = TransformerFactory.newInstance();

                Transformer trans = transFact.newTransformer(xsltSource);

                trans.transform(xmlSource, result);
            } catch (Exception e) {
                // System.err.println("error applying the xslt file "+e);
                log.error(e.getLocalizedMessage(), e);
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

}

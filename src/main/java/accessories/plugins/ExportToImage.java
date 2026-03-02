/*
 * Created on 28.03.2004
 *
 */
package accessories.plugins;

import freemind.extensions.ExportHook;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.image.BufferedImage;
import java.io.*;

@Slf4j
public class ExportToImage extends ExportHook {

    public ExportToImage() {
        super();
    }

    public void startupMapHook() {
        super.startupMapHook();
        BufferedImage image = createBufferedImage();
        if (image != null) {
            String imageType = getResourceString("image_type");

            exportToImage(image, imageType,
                    getResourceString("image_description"));
        }

    }

    /**
     * Export image.
     */
    public boolean exportToImage(BufferedImage image, String type,
                                 String description) {
        File chosenFile = chooseFile(type, description, null);
        if (chosenFile == null) {
            return false;
        }
        try {
            getController().getFrame().setWaitingCursor(true);
            FileOutputStream out = new FileOutputStream(chosenFile);
            ImageIO.write(image, type, out);
            // OutputStream out = new FileOutputStream(f);
            // JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            // encoder.encode(image);
            out.close();
        } catch (IOException e1) {
            log.error(e1.getLocalizedMessage(), e1);
        }
        getController().getFrame().setWaitingCursor(false);
        return true;
    }

    public void transForm(Source xmlSource, InputStream xsltStream,
                          File resultFile, String areaCode) throws FileNotFoundException {
        // System.out.println("set xsl");
        Source xsltSource = new StreamSource(xsltStream);
        // System.out.println("set result");
        Result result = new StreamResult(new FileOutputStream(resultFile));

        // create an instance of TransformerFactory
        try {
            // System.out.println("make transform instance");
            TransformerFactory transFact = TransformerFactory.newInstance();

            Transformer trans = transFact.newTransformer(xsltSource);
            // set parameter:
            // relative directory <filename>_files
            trans.setParameter("destination_dir", resultFile.getName()
                    + "_files/");
            trans.setParameter("area_code", areaCode);
            trans.setParameter("folding_type", getController().getFrame()
                    .getProperty("html_export_folding"));
            trans.transform(xmlSource, result);
        } catch (Exception e) {
            // System.err.println("error applying the xslt file "+e);
            log.error(e.getLocalizedMessage(), e);
        }
    }

}

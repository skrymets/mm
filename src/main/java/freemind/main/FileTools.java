package freemind.main;

import freemind.common.UnicodeReader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

@Slf4j
public final class FileTools {

    protected static final Set<String> executableExtensions = new HashSet<>(5);

    static {
        executableExtensions.add("exe");
        executableExtensions.add("com");
        executableExtensions.add("vbs");
        executableExtensions.add("bat");
        executableExtensions.add("lnk");
    }

    private FileTools() {
    }

    public static boolean executableByExtension(String file) {
        return executableExtensions.contains(FilenameUtils.getExtension(file).toLowerCase());
    }

    public static void setHidden(File file, boolean hidden, boolean synchronously) {
        // According to Web articles, UNIX systems do not have an attribute hidden
        // in general; rather, they consider files starting with . as hidden.
        if (SystemUtils.IS_OS_WINDOWS) {
            try {
                Runtime.getRuntime().exec(format("attrib %sH \"%s\"", hidden ? "+" : "-", file.getAbsolutePath()));
                // Synchronize the effect because it is asynchronous in general.
                if (!synchronously) {
                    return;
                }
                int timeOut = 10;
                while (file.isHidden() != hidden && timeOut > 0) {
                    Thread.sleep(10);
                    timeOut--;
                }
            } catch (IOException | InterruptedException e) {
                log.error(e.getLocalizedMessage(), e);
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void makeFileHidden(File file, boolean setHidden) {
        try {
            if (!file.exists() || !SystemUtils.IS_OS_WINDOWS) {
                return;
            }
            Path path = file.toPath();
            DosFileAttributes attrs = Files.readAttributes(path, DosFileAttributes.class);
            if (setHidden != attrs.isHidden()) {
                Files.setAttribute(path, "dos:hidden", setHidden);
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    // {{{ setPermissions() method

    /**
     * Sets numeric permissions of a file. On non-Unix platforms, does nothing.
     * From jEdit
     */
    public static void setPermissions(String path, int permissions) {

        if (permissions == 0 || !SystemUtils.IS_OS_UNIX) {
            return;
        }

        String[] cmdarray = {"chmod", Integer.toString(permissions, 8), path};

        try {
            Process process = Runtime.getRuntime().exec(cmdarray);
            process.getInputStream().close();
            process.getOutputStream().close();
            process.getErrorStream().close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static Reader getReaderFromFile(File pInputFile) throws FileNotFoundException {
        return new FileReader(pInputFile);
    }

    /**
     * Creates a default reader that just reads the given file.
     */
    public static Reader getActualReader(Reader pReader) {
        return new BufferedReader(pReader);
    }

    /**
     * Creates a reader that pipes the input file through a XSLT-Script that
     * updates the version to the current.
     */
    public static Reader getUpdateReader(Reader pReader, String xsltScript, Resources resources) {
        // try to convert map with xslt:
        URL updaterUrl = resources.getResource(xsltScript);
        if (updaterUrl == null) {
            throw new IllegalArgumentException(xsltScript + " not found.");
        }

        log.info("Updating the reader {} to the current version.", pReader);
        boolean successful = false;
        String errorMessage = null;

        try (InputStream inputStream = updaterUrl.openStream(); StringWriter writer = new StringWriter()) {
            final var xsltSource = new StreamSource(inputStream);
            final var result = new StreamResult(writer);

            String fileContents = IOUtils.toString(pReader);
            if (fileContents.length() > 10) {
                log.info("File start before UTF8 replacement: '{}'", fileContents.substring(0, 9));
            }
            fileContents = XmlMarshallingTools.replaceUtf8AndIllegalXmlChars(fileContents);
            if (fileContents.length() > 10) {
                log.info("File start after UTF8 replacement: '{}'", fileContents.substring(0, 9));
            }
            final var sr = new StreamSource(new StringReader(fileContents));
            // Dimitry: to avoid a memory leak and properly release resources after the XSLT transformation everything
            // should run in own thread. Only after the thread dies // the resources are released.
            @Getter
            class TransformerRunnable implements Runnable {

                private boolean successful = false;
                private String errorMessage;

                public void run() {
                    // create an instance of TransformerFactory
                    TransformerFactory transFact = TransformerFactory.newInstance();
                    log.info("TransformerFactory class: {}", transFact.getClass());
                    Transformer trans;
                    try {
                        trans = transFact.newTransformer(xsltSource);
                        trans.transform(sr, result);
                        successful = true;
                    } catch (TransformerException e) {
                        log.error(e.getLocalizedMessage(), e);
                        errorMessage = e.toString();
                    }
                }

            }
            final var transformer = new TransformerRunnable();
            var transformerThread = new Thread(transformer, "XSLT");
            transformerThread.start();
            transformerThread.join();
            log.info("Updating the reader {} to the current version. Done.", pReader);
            successful = transformer.isSuccessful();
            errorMessage = transformer.getErrorMessage();
            if (successful) {
                String content = writer.getBuffer().toString();
                String replacedContent = XmlMarshallingTools.replaceUtf8AndIllegalXmlChars(content);
                return new StringReader(replacedContent);
            } else {
                return new StringReader("<map><node TEXT='" + HtmlTools.toXMLEscapedText(errorMessage) + "'/></map>");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new StringReader("<map><node TEXT='" + HtmlTools.toXMLEscapedText(errorMessage) + "'/></map>");
        }
    }

    /**
     * Returns pMinimumLength bytes of the files content.
     *
     * @return an empty string buffer, if something fails.
     */
    public static StringBuilder readFileStart(Reader pReader, int pMinimumLength) {
        BufferedReader in = null;
        var buffer = new StringBuilder();
        try {
            // get the file start into the memory:
            in = new BufferedReader(pReader);
            String str;
            while ((str = in.readLine()) != null) {
                buffer.append(str);
                if (buffer.length() >= pMinimumLength) {
                    break;
                }
            }
            in.close();
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
            return new StringBuilder();
        }
        return buffer;
    }

    public interface ReaderCreator {

        Reader createReader() throws FileNotFoundException;
    }

    public static class FileReaderCreator implements ReaderCreator {

        private final File mFile;

        public FileReaderCreator(File pFile) {
            mFile = pFile;
        }

        public Reader createReader() throws FileNotFoundException {
            return new UnicodeReader(new FileInputStream(mFile), "UTF-8");
        }

        public String toString() {
            return mFile.getName();
        }
    }

    public static class StringReaderCreator implements ReaderCreator {

        private final String mString;

        public StringReaderCreator(String pString) {
            mString = pString;
        }

        public Reader createReader() {
            return new StringReader(mString);
        }

        public String toString() {
            return mString;
        }
    }
}

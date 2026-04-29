package freemind.main;

import freemind.common.UnicodeReader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import javax.swing.*;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.print.Paper;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.util.*;

import static java.lang.String.format;

@Slf4j
public class Tools {

    public static final Set<String> executableExtensions = new HashSet<>(5);

    static {
        executableExtensions.add("exe");
        executableExtensions.add("com");
        executableExtensions.add("vbs");
        executableExtensions.add("bat");
        executableExtensions.add("lnk");
    }

    public static boolean executableByExtension(String file) {
        return executableExtensions.contains(FilenameUtils.getExtension(file).toLowerCase());
    }

    /**
     * Tests a string to be equals with "true".
     *
     * @return true, iff the String is "true".
     */
    public static boolean isPreferenceTrue(String option) {
        return Objects.equals(option, "true");
    }

    public static void setHidden(File file, boolean hidden, boolean synchronously) {
        // According to Web articles, UNIX systems do not have attribute hidden
        // in general, rather, they consider files starting with . as hidden.
        if (SystemUtils.IS_OS_WINDOWS) {
            try {
                Runtime.getRuntime().exec(format("attrib %sH \"%s\"", hidden ? "+" : "-", file.getAbsolutePath()));
                // Synchronize the effect because it is asynchronous in general.
                if (!synchronously) {
                    return;
                }
                int timeOut = 10;
                while (file.isHidden() != hidden && timeOut > 0) {
                    Thread.sleep(10/* miliseconds */);
                    timeOut--;
                }
            } catch (IOException | InterruptedException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     * Creates a reader that pipes the input file through a XSLT-Script that
     * updates the version to the current.
     */
    public static Reader getUpdateReader(Reader pReader, String xsltScript, Resources resources) throws IOException {
        StringWriter writer = null;
        InputStream inputStream = null;

        log.info("Updating the reader {} to the current version.", pReader);
        boolean successful = false;
        String errorMessage = null;
        try {
            // try to convert map with xslt:
            URL updaterUrl = null;
            updaterUrl = resources.getResource(xsltScript);
            if (updaterUrl == null) {
                throw new IllegalArgumentException(xsltScript + " not found.");
            }
            inputStream = updaterUrl.openStream();
            final var xsltSource = new StreamSource(inputStream);
            // get output:
            writer = new StringWriter();
            final var result = new StreamResult(writer);

            String fileContents = IOUtils.toString(pReader);
            if (fileContents.length() > 10) {
                log.info("File start before UTF8 replacement: '{}'", fileContents.substring(0, 9));
            }
            fileContents = XmlMarshallingTools.replaceUtf8AndIllegalXmlChars(fileContents);
            if (fileContents.length() > 10) {
                log.info("File start after UTF8 replacement: '{}'", fileContents.substring(0, 9));
            }
            final var sr = new StreamSource(new StringReader(
                    fileContents));
            // Dimitry: to avoid a memory leak and properly release resources
            // after the XSLT transformation
            // everything should run in own thread. Only after the thread dies
            // the resources are released.
            @Getter
            class TransformerRunnable implements Runnable {

                private boolean successful = false;
                private String errorMessage;

                public void run() {
                    // create an instance of TransformerFactory
                    TransformerFactory transFact = TransformerFactory
                            .newInstance();
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
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            errorMessage = e.getLocalizedMessage();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
        if (successful) {
            String content = writer.getBuffer().toString();
            String replacedContent = XmlMarshallingTools
                    .replaceUtf8AndIllegalXmlChars(content);
            return new StringReader(replacedContent);
        } else {
            return new StringReader("<map><node TEXT='"
                    + HtmlTools.toXMLEscapedText(errorMessage) + "'/></map>");
        }
    }

    /**
     * Creates a default reader that just reads the given file.
     */
    public static Reader getActualReader(Reader pReader) {
        return new BufferedReader(pReader);
    }

    public static Reader getReaderFromFile(File pInputFile) throws FileNotFoundException {
        return new FileReader(pInputFile);
    }

    public static class FileReaderCreator implements Tools.ReaderCreator {

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

    public static class StringReaderCreator implements Tools.ReaderCreator {

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

    public interface ReaderCreator {

        Reader createReader() throws FileNotFoundException;
    }

    public static KeyStroke getKeyStroke(final String keyStrokeDescription) {
        if (keyStrokeDescription == null) {
            return null;
        }
        final KeyStroke keyStroke = KeyStroke
                .getKeyStroke(keyStrokeDescription);
        if (keyStroke != null) {
            return keyStroke;
        }
        return KeyStroke.getKeyStroke("typed " + keyStrokeDescription);
    }

    // {{{ setPermissions() method

    /**
     * Sets numeric permissions of a file. On non-Unix platforms, does nothing.
     * From jEdit
     */
    public static void setPermissions(String path, int permissions) {

        if (permissions != 0) {
            if (SystemUtils.IS_OS_UNIX) {
                String[] cmdarray = {"chmod",
                        Integer.toString(permissions, 8), path};

                try {
                    Process process = Runtime.getRuntime().exec(cmdarray);
                    process.getInputStream().close();
                    process.getOutputStream().close();
                    process.getErrorStream().close();
                } catch (Throwable ignored) {
                }
            }
        }
    } // }}}

    public static void setPageFormatFromString(Paper pPaper,
                                               String pPageFormatProperty) {
        try {
            // parse string:
            var tokenizer = new StringTokenizer(
                    pPageFormatProperty, ";");
            if (tokenizer.countTokens() != 6) {
                log.warn("Page format property has not the correct format:{}", pPageFormatProperty);
                return;
            }
            pPaper.setSize(nt(tokenizer), nt(tokenizer));
            pPaper.setImageableArea(nt(tokenizer), nt(tokenizer),
                    nt(tokenizer), nt(tokenizer));
        } catch (NumberFormatException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    private static double nt(StringTokenizer pTokenizer) {
        String nextToken = pTokenizer.nextToken();
        try {
            return Double.parseDouble(nextToken);
        } catch (NumberFormatException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return 0;
    }

    public static String getPageFormatAsString(Paper pPaper) {
        return pPaper.getWidth() + ";" + pPaper.getHeight() + ";"
                + pPaper.getImageableX() + ";" + pPaper.getImageableY() + ";"
                + pPaper.getImageableWidth() + ";"
                + pPaper.getImageableHeight();
    }

    public static String getFreeMindBasePath() {
        final String freemindLibJar = "lib/freemind.jar";
        final String contentsJavaJar = "Contents/Java/freemind.jar";
        final String resourcesJava = "Contents/Resources/Java/";

        String path = FreeMindStarter.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath();
        String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8);
        log.info("Path: {}", decodedPath);
        if (decodedPath.endsWith(contentsJavaJar)) {
            decodedPath = decodedPath.substring(0, decodedPath.length() - contentsJavaJar.length());
            decodedPath = decodedPath + resourcesJava;
            log.info("macPath: {}", decodedPath);
        } else if (decodedPath.endsWith(freemindLibJar)) {
            decodedPath = decodedPath.substring(0, decodedPath.length() - freemindLibJar.length());
            log.info("reducded Path: {}", decodedPath);
        }
        return decodedPath + "dictionaries/";
    }

    public static Properties copyChangedProperties(Properties props2, Properties defProps2) {
        var toBeStored = new Properties();
        for (var o : props2.keySet()) {
            String key = (String) o;
            if (!Objects.equals(props2.get(key), defProps2.get(key))) {
                toBeStored.put(key, props2.get(key));
            }
        }
        return toBeStored;
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

}

/*
 * FreeMind - a program for creating and viewing mindmaps
 * Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 * See COPYING for details
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package freemind.main;

import freemind.common.UnicodeReader;
import freemind.common.XmlBindingTools;
import freemind.controller.actions.CompoundAction;
import freemind.controller.actions.XmlAction;
import freemind.frok.patches.JIBXGeneratedUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import javax.swing.*;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
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
import java.util.List;

import static java.lang.String.format;

/**
 * @author foltin
 */
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
     * This method converts an absolute url to an url relative to a given
     * base-url. Something like this should be included in the libraries, but I
     * couldn't find it. You can create a new absolute url with
     * "new URL(URL context, URL relative)".
     */
    public static String toRelativeURL(URL base, URL target) {
        // Precondition: If URL is a path to folder, then it must end with '/' character.
        if (base == null || !base.getProtocol().equals(target.getProtocol()) || !base.getHost().equals(target.getHost())) {
            return target.toString();
        }

        String baseString = base.getFile();
        String targetString = target.getFile();
        String result = "";
        // remove filename from URL
        targetString = targetString.substring(0, targetString.lastIndexOf("/") + 1);
        // remove filename from URL
        baseString = baseString.substring(0, baseString.lastIndexOf("/") + 1);

        // Algorithm
        // look for same start:
        int index = targetString.length() - 1;
        while (!baseString.startsWith(targetString.substring(0, index + 1))) {
            // remove last part:
            index = targetString.lastIndexOf("/", index - 1);
            if (index < 0) {
                // no common part. This is strange, as both should start with /, but...
                break;
            }
        }

        // now, baseString is targetString + "/" + rest. we determine
        // rest=baseStringRest now.
        String baseStringRest = baseString.substring(index);

        // Maybe this causes problems under windows
        StringTokenizer baseTokens = new StringTokenizer(baseStringRest, "/");

        // Maybe this causes problems under windows
        StringTokenizer targetTokens = new StringTokenizer(targetString.substring(index + 1), "/");

        String nextTargetToken = "";

        while (baseTokens.hasMoreTokens()) {
            result = result.concat("../");
            baseTokens.nextToken();
        }

        while (targetTokens.hasMoreTokens()) {
            nextTargetToken = targetTokens.nextToken();
            result = result.concat(nextTargetToken + "/");
        }

        String temp = target.getFile();
        result = result.concat(temp.substring(temp.lastIndexOf("/") + 1));
        return result;
    }

    /**
     * If the preferences say, that links should be relative, a relative url is
     * returned.
     *
     * @param input    the file that is treated
     * @param pMapFile the file, that input is made relative to
     * @return in case of trouble the absolute path.
     */
    public static String fileToRelativeUrlString(File input, File pMapFile) {
        URL link;
        String relative;
        try {
            link = Tools.fileToUrl(input);
            relative = link.toString();
            if ("relative".equals(Resources.getInstance().getProperty("links"))) {
                // Create relative URL
                relative = Tools.toRelativeURL(Tools.fileToUrl(pMapFile), link);
            }
            return relative;
        } catch (MalformedURLException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return input.getAbsolutePath();
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
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     * Example: expandPlaceholders("Hello $1.","Dolly"); => "Hello Dolly."
     */
    public static String expandPlaceholders(String message, String s1) {
        String result = message;
        if (s1 != null) {
            s1 = s1.replaceAll("\\\\", "\\\\\\\\"); // Replace \ with \\
            result = result.replaceAll("\\$1", s1);
        }
        return result;
    }

    public static String expandPlaceholders(String message, String s1, String s2) {
        String result = message;
        if (s1 != null) {
            result = result.replaceAll("\\$1", s1);
        }
        if (s2 != null) {
            result = result.replaceAll("\\$2", s2);
        }
        return result;
    }

    public static String expandPlaceholders(String message, String s1, String s2, String s3) {
        String result = expandPlaceholders(message, s1, s2);
        if (s3 != null) {
            result = result.replaceAll("\\$3", s3);
        }
        return result;
    }

    /**
     * Extracts a long from xml. Only useful for dates.
     */
    public static Date xmlToDate(String xmlString) {
        try {
            return new Date(Long.valueOf(xmlString).longValue());
        } catch (Exception e) {
            return new Date(System.currentTimeMillis());
        }
    }

    public static String dateToString(Date date) {
        return Long.toString(date.getTime());
    }

    /**
     * Creates a reader that pipes the input file through a XSLT-Script that
     * updates the version to the current.
     */
    public static Reader getUpdateReader(Reader pReader, String xsltScript) throws IOException {
        StringWriter writer = null;
        InputStream inputStream = null;

        log.info("Updating the reader {} to the current version.", pReader);
        boolean successful = false;
        String errorMessage = null;
        try {
            // try to convert map with xslt:
            URL updaterUrl = null;
            updaterUrl = Resources.getInstance().getResource(xsltScript);
            if (updaterUrl == null) {
                throw new IllegalArgumentException(xsltScript + " not found.");
            }
            inputStream = updaterUrl.openStream();
            final Source xsltSource = new StreamSource(inputStream);
            // get output:
            writer = new StringWriter();
            final Result result = new StreamResult(writer);

            String fileContents = IOUtils.toString(pReader);
            if (fileContents.length() > 10) {
                log.info("File start before UTF8 replacement: '{}'", fileContents.substring(0, 9));
            }
            fileContents = replaceUtf8AndIllegalXmlChars(fileContents);
            if (fileContents.length() > 10) {
                log.info("File start after UTF8 replacement: '{}'", fileContents.substring(0, 9));
            }
            final StreamSource sr = new StreamSource(new StringReader(
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
                    } catch (Exception e) {
                        log.error(e.getLocalizedMessage(), e);
                        errorMessage = e.toString();
                    }
                }

            }
            final TransformerRunnable transformer = new TransformerRunnable();
            Thread transformerThread = new Thread(transformer, "XSLT");
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
            String replacedContent = Tools
                    .replaceUtf8AndIllegalXmlChars(content);
            return new StringReader(replacedContent);
        } else {
            return new StringReader("<map><node TEXT='"
                    + HtmlTools.toXMLEscapedText(errorMessage) + "'/></map>");
        }
    }

    public static String replaceUtf8AndIllegalXmlChars(String fileContents) {
        return HtmlTools.removeInvalidXmlCharacters(fileContents);
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

    /**
     * Removes the "TranslateMe" sign from the end of not translated texts.
     */
    public static String removeTranslateComment(String inputString) {
        if (inputString != null
                && inputString.endsWith(FreeMindCommon.POSTFIX_TRANSLATE_ME)) {
            // remove POSTFIX_TRANSLATE_ME:
            inputString = inputString.substring(0, inputString.length()
                    - FreeMindCommon.POSTFIX_TRANSLATE_ME.length());
        }
        return inputString;
    }

    /**
     * Returns the same URL as input with the addition, that the reference part
     * "#..." is filtered out.
     */
    public static URL getURLWithoutReference(URL input)
            throws MalformedURLException {
        return new URL(input.toString().replaceFirst("#.*", ""));
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

    public static URL fileToUrl(File pFile) throws MalformedURLException {
        if (pFile == null) {
            return null;
        }
        return pFile.toURI().toURL();
    }

    public static String compareText(String pText1, String pText2) {
        if (pText1 == null || pText2 == null) {
            return "One of the Strings is null " + pText1 + ", " + pText2;
        }
        StringBuffer b = new StringBuffer();
        if (pText1.length() > pText2.length()) {
            b.append("First string is longer :").append(pText1.substring(pText2.length())).append("\n");
        }
        if (pText1.length() < pText2.length()) {
            b.append("Second string is longer :").append(pText2.substring(pText1.length())).append("\n");
        }
        for (int i = 0; i < Math.min(pText1.length(), pText2.length()); i++) {
            if (pText1.charAt(i) != pText2.charAt(i)) {
                b.append("Difference at ").append(i).append(": ").append(pText1.charAt(i)).append("!=").append(pText2.charAt(i)).append("\n");
            }
        }
        return b.toString();
    }

    public static File urlToFile(URL pUrl) throws URISyntaxException {
        return new File(new URI(pUrl.toString()));
    }

    public static String marshall(XmlAction action) {
        return XmlBindingTools.getInstance().marshall(action);
    }

    public static XmlAction unMarshall(String inputString) {
        return XmlBindingTools.getInstance().unMarshall(inputString);
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

    public static String arrayToUrls(String[] pArgs) {
        StringBuffer b = new StringBuffer();
        for (String fileName : pArgs) {
            try {
                b.append(fileToUrl(new File(fileName)));
                b.append('\n');
            } catch (MalformedURLException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return b.toString();
    }

    public static List<URL> urlStringToUrls(String pUrls) {
        String[] urls = pUrls.split("\n");
        List<URL> ret = new ArrayList<>();
        for (String url : urls) {
            try {
                ret.add(new URL(url));
            } catch (MalformedURLException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return ret;
    }

    public static void setPageFormatFromString(Paper pPaper,
                                               String pPageFormatProperty) {
        try {
            // parse string:
            StringTokenizer tokenizer = new StringTokenizer(
                    pPageFormatProperty, ";");
            if (tokenizer.countTokens() != 6) {
                log.warn("Page format property has not the correct format:{}", pPageFormatProperty);
                return;
            }
            pPaper.setSize(nt(tokenizer), nt(tokenizer));
            pPaper.setImageableArea(nt(tokenizer), nt(tokenizer),
                    nt(tokenizer), nt(tokenizer));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    private static double nt(StringTokenizer pTokenizer) {
        String nextToken = pTokenizer.nextToken();
        try {
            return Double.parseDouble(nextToken);
        } catch (Exception e) {
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

    public static String printXmlAction(XmlAction pAction) {
        final String classString = pAction.getClass().getName().replaceAll(".*\\.", "");

        if (pAction instanceof CompoundAction) {

            List<XmlAction> xmlActions = JIBXGeneratedUtil.listXmlActions((CompoundAction) pAction);

            StringBuilder buf = new StringBuilder("[");

            for (XmlAction xmlAction : xmlActions) {
                if (buf.length() > 1) {
                    buf.append(',');
                }
                XmlAction subAction = xmlAction;
                buf.append(printXmlAction(subAction));
            }
            buf.append(']');

            return classString + " " + buf;
        }
        return classString;
    }

    public static XmlAction deepCopy(XmlAction action) {
        return unMarshall(marshall(action));
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
        Properties toBeStored = new Properties();
        for (Object o : props2.keySet()) {
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
    public static StringBuffer readFileStart(Reader pReader, int pMinimumLength) {
        BufferedReader in = null;
        StringBuffer buffer = new StringBuffer();
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
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return new StringBuffer();
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

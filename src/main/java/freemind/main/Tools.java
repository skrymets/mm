package freemind.main;

import lombok.extern.slf4j.Slf4j;

import java.awt.print.Paper;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class Tools {

    /**
     * Tests a string to be equals with "true".
     *
     * @return true, iff the String is "true".
     */
    public static boolean isPreferenceTrue(String option) {
        return Objects.equals(option, "true");
    }

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

}

package freemind.main;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Slf4j
public final class UrlTools {

    private UrlTools() {
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
        var baseTokens = new StringTokenizer(baseStringRest, "/");

        // Maybe this causes problems under windows
        var targetTokens = new StringTokenizer(targetString.substring(index + 1), "/");

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
    public static String fileToRelativeUrlString(File input, File pMapFile, Resources resources) {
        URL link;
        String relative;
        try {
            link = UrlTools.fileToUrl(input);
            relative = link.toString();
            if ("relative".equals(resources.getProperty("links"))) {
                // Create relative URL
                relative = UrlTools.toRelativeURL(UrlTools.fileToUrl(pMapFile), link);
            }
            return relative;
        } catch (MalformedURLException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return input.getAbsolutePath();
    }

    /**
     * Returns the same URL as input with the addition, that the reference part
     * "#..." is filtered out.
     */
    public static URL getURLWithoutReference(URL input)
            throws MalformedURLException {
        return new URL(input.toString().replaceFirst("#.*", ""));
    }

    public static URL fileToUrl(File pFile) throws MalformedURLException {
        if (pFile == null) {
            return null;
        }
        return pFile.toURI().toURL();
    }

    public static File urlToFile(URL pUrl) throws URISyntaxException {
        return new File(new URI(pUrl.toString()));
    }

    public static String arrayToUrls(String[] pArgs) {
        var b = new StringBuilder();
        for (var fileName : pArgs) {
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
        var ret = new ArrayList<URL>();
        for (var url : urls) {
            try {
                ret.add(new URL(url));
            } catch (MalformedURLException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return ret;
    }
}

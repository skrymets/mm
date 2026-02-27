package freemind.controller;

import freemind.main.FreeMindMain;
import freemind.main.Tools;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class DefaultLocalLinkConverter implements Controller.LocalLinkConverter {
    private final FreeMindMain frame;

    public DefaultLocalLinkConverter(FreeMindMain frame) {
        this.frame = frame;
    }

    public URL convertLocalLink(String map) throws MalformedURLException {
        /* new handling for relative urls. fc, 29.10.2003. */
        String applicationPath = frame.getFreemindBaseDir();
        // remove "." and make url
        return Tools.fileToUrl(new File(applicationPath + map.substring(1)));
        /* end: new handling for relative urls. fc, 29.10.2003. */
    }
}

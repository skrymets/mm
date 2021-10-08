/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
 *
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/*
 * Created on 12.07.2005
 * Copyright (C) 2005-2008 Dimitri Polivaev, Christian Foltin
 */
package freemind.main;

import freemind.common.NamedObject;
import freemind.common.TextTranslator;
import freemind.frok.patches.FreeMindMainMock;
import freemind.main.FreeMindMain.VersionInformation;
import freemind.modes.FreeMindAwtFileDialog;
import freemind.modes.FreeMindFileDialog;
import freemind.modes.FreeMindJFileDialog;
import lombok.extern.log4j.Log4j2;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author Dimitri Polivaev 12.07.2005
 */
@Log4j2
public class Resources implements TextTranslator {

    private FreeMindMain main;
    static Resources resourcesInstance = null;
    private HashMap<String, String> countryMap;

    private Resources(FreeMindMain frame) {
        this.main = frame;
    }

    static public void createInstance(FreeMindMain frame) {
        if (resourcesInstance == null) {
            resourcesInstance = new Resources(frame);
        }
    }

    public URL getResource(String resource) {
        return main.getResource(resource);
    }

    public String getResourceString(String resource) {
        return main.getResourceString(resource);
    }

    public String getResourceString(String key, String resource) {
        return main.getResourceString(key, resource);
    }

    static public Resources getInstance() {
        if (resourcesInstance == null) {
            createInstance(new FreeMindMainMock());
            System.err.println("Resources without FreeMind called.");
        }
        return resourcesInstance;
    }

    public String getFreemindDirectory() {
        return main.getFreemindDirectory();
    }

    public String getFreemindBaseDir() {
        return main.getFreemindBaseDir();
    }

    public VersionInformation getFreemindVersion() {
        return main.getFreemindVersion();
    }

    public ClassLoader getFreeMindClassLoader() {
        return main.getFreeMindClassLoader();
    }

    public int getIntProperty(String key, int defaultValue) {
        return main.getIntProperty(key, defaultValue);
    }

    public long getLongProperty(String key, long defaultValue) {
        try {
            return Long.parseLong(getProperty(key));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * @param key Property key
     * @return the boolean value of the property resp. the default.
     */
    public boolean getBoolProperty(String key) {
        String boolProperty = getProperty(key);
        return Tools.safeEquals("true", boolProperty);
    }

    public Properties getProperties() {
        return main.getProperties();
    }

    public String getProperty(String key) {
        return main.getProperty(key);
    }

    public ResourceBundle getResources() {
        return main.getResources();
    }

    public HashMap<String, String> getCountryMap() {
        if (countryMap == null) {
            String[] countryMapArray = new String[]{"de", "DE", "en", "UK",
                    "en", "US", "es", "ES", "es", "MX", "fi", "FI", "fr", "FR",
                    "hu", "HU", "it", "CH", "it", "IT", "nl", "NL", "no", "NO",
                    "pt", "PT", "ru", "RU", "sl", "SI", "uk", "UA", "zh", "CN"};

            countryMap = new HashMap<>();
            for (int i = 0; i < countryMapArray.length; i = i + 2) {
                countryMap.put(countryMapArray[i], countryMapArray[i + 1]);
            }
        }
        return countryMap;
    }

    public String format(String resourceKey, Object[] messageArguments) {
        MessageFormat formatter = new MessageFormat(getResourceString(resourceKey));
        String stringResult = formatter.format(messageArguments);
        return stringResult;
    }

    public NamedObject createTranslatedString(String key) {
        String fs = getResourceString(key);
        return new NamedObject(key, fs);
    }

    public String getText(String pKey) {
        return getResourceString(pKey);
    }

    public FreeMindFileDialog getStandardFileChooser(FileFilter filter) {
        FreeMindFileDialog chooser;
        if (!Tools.isMacOsX()) {
            chooser = new FreeMindJFileDialog();
        } else {
            // only for mac
            chooser = new FreeMindAwtFileDialog();
        }
        if (filter != null) {
            chooser.addChoosableFileFilterAsDefault(filter);
        }
        return chooser;
    }

    /**
     * @param baseFileName
     * @return
     */
    public String createThumbnailFileName(File baseFileName) {
        String fileName = baseFileName.getParent()
                + File.separatorChar
                + "." // hidden
                + baseFileName.getName().replaceFirst(
                FreeMindCommon.FREEMIND_FILE_EXTENSION + "$",
                ".png");
        return fileName;
    }

}

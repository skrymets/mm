/*
 * Created on 12.07.2005
 * Copyright (C) 2005-2008 Dimitri Polivaev, Christian Foltin
 */
package freemind.main;

import freemind.common.NamedObject;
import org.apache.commons.lang3.SystemUtils;
import freemind.common.TextTranslator;

import freemind.modes.FreeMindAwtFileDialog;
import freemind.modes.FreeMindFileDialog;
import freemind.modes.FreeMindJFileDialog;
import lombok.extern.slf4j.Slf4j;

import jakarta.inject.Inject;
import freemind.preferences.FreemindPropertyListener;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

@Slf4j
public class Resources implements TextTranslator {

    private final FreeMindMain main;
    static Resources resourcesInstance = null;
    private HashMap<String, String> countryMap;

    private static final List<FreemindPropertyListener> propertyChangeListeners = new ArrayList<>();

    @Inject
    public Resources(FreeMindMain frame) {
        this.main = frame;
        resourcesInstance = this;
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

    /**
     * Non-deprecated static accessor for classes that cannot use Guice injection
     * (static initializers, legacy bridge code, etc.).
     */
    public static Resources get() {
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
        return Objects.equals("true", boolProperty);
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
            String[] countryMapArray = new String[]{
                    "de", "DE",
                    "en", "UK",
                    "en", "US",
                    "es", "ES",
                    "es", "MX",
                    "fi", "FI",
                    "fr", "FR",
                    "hu", "HU",
                    "it", "CH",
                    "it", "IT",
                    "nl", "NL",
                    "no", "NO",
                    "pt", "PT",
                    "ru", "RU",
                    "sl", "SI",
                    "uk", "UA",
                    "zh", "CN"
            };

            countryMap = new HashMap<>();
            for (int i = 0; i < countryMapArray.length; i = i + 2) {
                countryMap.put(countryMapArray[i], countryMapArray[i + 1]);
            }
        }
        return countryMap;
    }

    public String format(String resourceKey, Object[] messageArguments) {
        MessageFormat formatter = new MessageFormat(getResourceString(resourceKey));
        return formatter.format(messageArguments);
    }

    public NamedObject createTranslatedString(String key) {
        String fs = getResourceString(key);
        return new NamedObject(key, fs);
    }

    public String getText(String pKey) {
        return getResourceString(pKey);
    }

    public FreeMindFileDialog getStandardFileChooser(FileFilter filter) {
        FreeMindFileDialog chooser = SystemUtils.IS_OS_MAC ? new FreeMindAwtFileDialog() : new FreeMindJFileDialog();

        if (filter != null) {
            chooser.addChoosableFileFilterAsDefault(filter);
        }
        return chooser;
    }

    public String createThumbnailFileName(File baseFileName) {
        // hidden
        return MessageFormat.format(
                "{0}{1}.{2}",
                baseFileName.getParent(),
                File.separatorChar,
                baseFileName.getName().replaceFirst(FreeMindCommon.FREEMIND_FILE_EXTENSION + "$", ".png")
        );
    }

    public static Collection<FreemindPropertyListener> getPropertyChangeListeners() {
        return Collections.unmodifiableCollection(propertyChangeListeners);
    }

    public static void addPropertyChangeListener(FreemindPropertyListener listener) {
        propertyChangeListeners.add(listener);
    }

    /**
     * @param listener The new listener. All currently available properties are sent
     *                 to the listener after registration. Here, the oldValue
     *                 parameter is set to null.
     */
    public static void addPropertyChangeListenerAndPropagate(FreemindPropertyListener listener) {
        addPropertyChangeListener(listener);
        Resources instance = get();
        if (instance == null) {
            return;
        }
        Properties properties = instance.getProperties();
        for (Object key : properties.keySet()) {
            listener.propertyChanged((String) key, properties.getProperty((String) key), null);
        }
    }

    public static void removePropertyChangeListener(FreemindPropertyListener listener) {
        propertyChangeListeners.remove(listener);
    }

    public static void firePropertyChanged(String propertyName, String newValue, String oldValue) {
        if (oldValue == null || !oldValue.equals(newValue)) {
            for (FreemindPropertyListener listener : getPropertyChangeListeners()) {
                listener.propertyChanged(propertyName, newValue, oldValue);
            }
        }
    }

}

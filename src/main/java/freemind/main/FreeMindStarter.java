/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Dimitri Polivaev, Christian Foltin and others.
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
 *
 * Created on 06.07.2006
 */
/*$Id: FreeMindStarter.java,v 1.1.2.11 2009/03/29 19:37:23 christianfoltin Exp $*/
package freemind.main;

import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;

import static java.lang.String.format;
import static java.nio.file.Files.*;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * @author foltin
 * @author skrymets
 */

@Log4j2
public class FreeMindStarter {

    private static final String DEFAULT_PREFERENCES_RESOURCE = "freemind.properties";
    private static final String REQUIRED_JAVA_VERSION = "1.8.0";
    private static final String USER_PROPERTIES_DIRECTORY = ".freemind";
    private static final Path DEFAULT_HOME_DIRECTORY = Paths.get(System.getProperty("user.home"), USER_PROPERTIES_DIRECTORY);

    private static final String USER_PROPERTIES_FILE = "user.properties";
    private static final Path FREEMIND_DEFAULT_USER_PREFERENCES_FILE = DEFAULT_HOME_DIRECTORY.resolve(USER_PROPERTIES_FILE);

    public static void main(String[] args) throws IOException {
        // First check version of Java
        checkJavaVersion();

        Properties defaultPreferences = loadDefaultPreferences();
        Properties userPreferences = loadUsersPreferences(defaultPreferences);

        createFreeMindHomeDirectory();
        setDefaultLocale(userPreferences);

        tweakAWT();

        try {
            new FreeMind(defaultPreferences, userPreferences).go(args);
        } catch (Exception e) {
            FreeMind.cantGo(e);
        }
    }

    private static void tweakAWT() {
        // Christopher Robin Elmersson: set
        Toolkit xToolkit = Toolkit.getDefaultToolkit();

        try {
            Field awtAppClassNameField = xToolkit.getClass().getDeclaredField("awtAppClassName");
            awtAppClassNameField.setAccessible(true);
            try {
                awtAppClassNameField.set(xToolkit, "FreeMind");
            } catch (IllegalAccessException ex) {
                log.error("Could not set window name", ex);
            }
        } catch (NoSuchFieldException ex) {
            // log.error("Could not get awtAppClassName");
        }
    }

    private static void checkJavaVersion() {
        log.info("Checking Java Version...");

        String javaVersion = System.getProperty("java.version");
        final String javaHome = System.getProperty("java.home");

        if (REQUIRED_JAVA_VERSION.compareTo(javaVersion) >= 0) {
            String message = format(
                    "FreeMind requires Java of version {} or higher (your version: {}, installed in {}).",
                    REQUIRED_JAVA_VERSION,
                    javaVersion,
                    javaHome);
            log.error(message);

            showMessageDialog(null, message, "FreeMind", JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }

        log.debug("Java version: {}. JAVA_HOME:  {}", javaVersion, javaHome);
    }

    private static void createFreeMindHomeDirectory() {
        if (notExists(DEFAULT_HOME_DIRECTORY) && isDirectory(DEFAULT_HOME_DIRECTORY)) {
            try {
                createDirectories(DEFAULT_HOME_DIRECTORY);
            } catch (IOException e) {
                log.error("Cannot create folder for user properties and logging: {}", DEFAULT_HOME_DIRECTORY.toString());
            }
        }
    }

    private static void setDefaultLocale(Properties properties) {
        String lang = properties.getProperty(FreeMindCommon.RESOURCE_LANGUAGE);
        if (lang == null) {
            return;
        }
        Locale localeDef = null;
        switch (lang.length()) {
            case 2:
                localeDef = new Locale(lang);
                break;
            case 5:
                localeDef = new Locale(lang.substring(0, 1), lang.substring(3, 4));
                break;
            default:
                return;
        }
        Locale.setDefault(localeDef);
    }

    private static Properties loadUsersPreferences(Properties defaultPreferences) {

        Properties userPreferences = new Properties(defaultPreferences);
        if (isReadable(FREEMIND_DEFAULT_USER_PREFERENCES_FILE)) {
            try (InputStream in = new FileInputStream(FREEMIND_DEFAULT_USER_PREFERENCES_FILE.toFile())) {
                userPreferences.load(in);
            } catch (Exception ex) {
                log.error("Error while loading default properties.", ex);
            }
        }

        return userPreferences;
    }

    public static Properties loadDefaultPreferences() throws IOException {
        try (InputStream stream = FreeMindStarter.class.getClassLoader().getResourceAsStream(DEFAULT_PREFERENCES_RESOURCE)) {
            Properties defaultProperties = new Properties();
            defaultProperties.load(stream);
            return defaultProperties;
        } catch (IOException e) {
            log.error("Could not load default properties {}. Reason: {}",
                    DEFAULT_PREFERENCES_RESOURCE,
                    e.getMessage()
            );
            throw e;
        }
    }

}

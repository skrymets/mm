package freemind.extensions;

import freemind.controller.actions.Plugin;
import freemind.controller.actions.PluginClasspath;
import freemind.frok.patches.JIBXGeneratedUtil;
import freemind.main.Resources;
import freemind.main.Tools;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class HookDescriptorBase {
    public static final String FREEMIND_BASE_DIR_STRING = "${freemind.base.dir}";

    private static Resources resources;

    public static void init(Resources res) {
        resources = res;
    }

    @Getter
    protected final Plugin pluginBase;

    protected final String mXmlPluginFile;

    public HookDescriptorBase(final Plugin pluginBase, final String xmlPluginFile) {
        super();
        this.pluginBase = pluginBase;
        mXmlPluginFile = xmlPluginFile;
    }

    protected String getFromResourceIfNecessary(String string) {
        if (string == null) {
            return string;
        }
        if (string.startsWith("%")) {
            return resources.getResourceString(string.substring(1));
        }
        return string;
    }

    protected String getFromPropertiesIfNecessary(String string) {
        if (string == null) {
            return string;
        }
        if (string.startsWith("%")) {
            return resources.getProperty(string.substring(1));
        }
        return string;
    }

    /**
     * @return the relative/absolute(?) position of the plugin xml file.
     */
    private String getPluginDirectory() {
        return resources.getFreemindBaseDir() + "/"
                + new File(mXmlPluginFile).getParent();
    }

    public List<PluginClasspath> getPluginClasspath() {
        List<PluginClasspath> returnValue = new ArrayList<>();
        List<Object> pluginChoice = JIBXGeneratedUtil.listPluginChoice(pluginBase);
        for (Object obj : pluginChoice) {
            if (obj instanceof PluginClasspath) {
                PluginClasspath pluginClasspath = (PluginClasspath) obj;
                returnValue.add(pluginClasspath);
            }
        }
        return returnValue;
    }

    public ClassLoader getPluginClassLoader() {
        // construct class loader:
        List<PluginClasspath> pluginClasspathList = getPluginClasspath();
        ClassLoader loader = getClassLoader(pluginClasspathList);
        return loader;
    }

    private static final HashMap<String, ClassLoader> classLoaderCache = new HashMap<>();

    /**
     * This string is used to identify known classloaders as they are cached.
     */
    private String createPluginClasspathString(List<PluginClasspath> pluginClasspathList) {
        StringBuilder result = new StringBuilder();
        for (PluginClasspath type : pluginClasspathList) {
            result.append(type.getJar()).append(",");
        }
        return result.toString();
    }

    private ClassLoader getClassLoader(List<PluginClasspath> pluginClasspathList) {
        String key = createPluginClasspathString(pluginClasspathList);
        if (classLoaderCache.containsKey(key))
            return classLoaderCache.get(key);
        try {
            URL[] urls = new URL[pluginClasspathList.size()];
            int j = 0;
            for (PluginClasspath classPath : pluginClasspathList) {
                String jarString = classPath.getJar();
                // if(jarString.startsWith(FREEMIND_BASE_DIR_STRING)){
                // jarString = frame.getFreemindBaseDir() +
                // jarString.substring(FREEMIND_BASE_DIR_STRING.length());
                // }
                // new version of classpath resolution suggested by ewl under
                // patch [ 1154510 ] Be able to give absolute classpath entries
                // in plugin.xml
                File file = new File(jarString);
                if (!file.isAbsolute()) {
                    file = new File(getPluginDirectory(), jarString);
                }
                // end new version by ewl.
                log.info("file {} exists = {}", Tools.fileToUrl(file), file.exists());
                urls[j++] = Tools.fileToUrl(file);
            }
            ClassLoader loader = new URLClassLoader(urls,
                    resources.getFreeMindClassLoader());
            classLoaderCache.put(key, loader);
            return loader;
        } catch (MalformedURLException e) {
            log.error(e.getLocalizedMessage(), e);
            return this.getClass().getClassLoader();
        }
    }
}

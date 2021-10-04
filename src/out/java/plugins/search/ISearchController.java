package plugins.search;

import java.io.File;
import java.util.logging.Logger;

import javax.swing.JFrame;

/**
 * Separate out methods from {@link SearchControllerHook} that we need in
 * {@link SearchViewPanel} so we can pass in {@link SearchControllerHook} or a test class
 *
 * @author Stephen Leonard
 * @version $Date:: $: Date of last commit
 * @since 1 Mar 2014
 */
public interface ISearchController {
    public void setWaitingCursor(boolean waiting);

    public Logger getLogger(Class<?> className);

    public JFrame getJFrame();

    public File[] getFilesOfOpenTabs();

    public void openMap(String path);

}

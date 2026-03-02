package freemind.modes;

import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public interface FreeMindFileDialog {

    interface DirectoryResultListener {
        void setChosenDirectory(File pDir);
    }

    int showOpenDialog(Component pParent) throws HeadlessException;

    int showSaveDialog(Component pParent) throws HeadlessException;

    void setDialogTitle(String pDialogTitle);

    /**
     * Sets the default file filter (that one that is activated at showup).
     *
     * @see #addChoosableFileFilter(FileFilter)
     */
    void addChoosableFileFilterAsDefault(FileFilter pFilter);

    /**
     * Adds a file filter for optional use. It is not selected by default, but this is UI dependent.
     *
     * @see #addChoosableFileFilterAsDefault(FileFilter)
     */
    void addChoosableFileFilter(FileFilter pFilter);

    /**
     * @param pMode JFileChooser.DIRECTORIES_ONLY, JFileChooser.FILES_ONLY, JFileChooser.FILES_AND_DIRECTORIES
     */
    void setFileSelectionMode(int pMode);

    void setMultiSelectionEnabled(boolean pB);

    boolean isMultiSelectionEnabled();

    File[] getSelectedFiles();

    File getSelectedFile();

    void setCurrentDirectory(File pLastCurrentDir);

    void setSelectedFile(File pFile);

    void registerDirectoryResultListener(DirectoryResultListener pDirectoryResultListener);

}

package freemind.modes;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * File Chooser for Mac
 */
@Slf4j
public class FreeMindAwtFileDialog extends FileDialog implements FreeMindFileDialog {

    private static final String APPLE_AWT_FILE_DIALOG_FOR_DIRECTORIES = "apple.awt.fileDialogForDirectories";

    private final static class NullFilter extends FileFilter {

        public boolean accept(File pF) {
            return true;
        }

        public String getDescription() {
            return "NullFilter";
        }

    }

    private static final class DirFilter extends FileFilter {

        public boolean accept(File pF) {
            return pF.isDirectory();
        }

        public String getDescription() {
            return "DirFilter";
        }

    }

    private static final class FileOnlyFilter extends FileFilter {

        public boolean accept(File pF) {
            return pF.isFile();
        }

        public String getDescription() {
            return "FileFilter";
        }

    }

    private static final class FileAndDirFilter extends FileFilter {

        public boolean accept(File pF) {
            return pF.isFile() || pF.isDirectory();
        }

        public String getDescription() {
            return "FileAndDirFilter";
        }
    }

    private final FreeMindFilenameFilter mFilter;
    private DirectoryResultListener mDirectoryResultListener = null;

    private static final class FreeMindFilenameFilter implements FilenameFilter {

        private FileFilter mCustomFilter = new NullFilter();
        /**
         * Filter for dirs, files or both.
         */
        private FileFilter mPrincipalFilter = new NullFilter();

        private FreeMindFilenameFilter() {
        }

        public boolean accept(File pDir, String pName) {
            File file = new File(pDir, pName);
            return mPrincipalFilter.accept(file) && mCustomFilter.accept(file);
        }

        public void setCustomFilter(FileFilter pFilter) {
            mCustomFilter = pFilter;
        }

        public void setPrincipalFilter(FileFilter pPrincipalFilter) {
            mPrincipalFilter = pPrincipalFilter;
        }
    }

    public FreeMindAwtFileDialog() {
        super((Frame) null);
        mFilter = new FreeMindFilenameFilter();
        super.setFilenameFilter(mFilter);
        System.setProperty(APPLE_AWT_FILE_DIALOG_FOR_DIRECTORIES, "false");

    }

    protected void callDirectoryListener() {
        if (getFile() != null) {
            if (mDirectoryResultListener != null) {
                try {
                    mDirectoryResultListener.setChosenDirectory(getSelectedFile().getParentFile());
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }

    protected int getReturnValue() {
        return (getFile() == null) ? JFileChooser.CANCEL_OPTION : JFileChooser.APPROVE_OPTION;
    }

    public int showOpenDialog(Component pParent) throws HeadlessException {
        setMode(LOAD);
        setVisible(true);
        callDirectoryListener();
        return getReturnValue();
    }

    public int showSaveDialog(Component pParent) throws HeadlessException {
        setMode(SAVE);
        setVisible(true);
        callDirectoryListener();
        return getReturnValue();
    }

    public void setDialogTitle(String pDialogTitle) {
        setTitle(pDialogTitle);
    }

    public void addChoosableFileFilter(FileFilter pFilter) {
        mFilter.setCustomFilter(pFilter);
    }

    public void setFileSelectionMode(int pMode) {
        System.setProperty(APPLE_AWT_FILE_DIALOG_FOR_DIRECTORIES, "false");
        switch (pMode) {
            case JFileChooser.DIRECTORIES_ONLY:
                mFilter.setPrincipalFilter(new DirFilter());
                System.setProperty(APPLE_AWT_FILE_DIALOG_FOR_DIRECTORIES, "true");
                break;
            case JFileChooser.FILES_ONLY:
                mFilter.setPrincipalFilter(new FileOnlyFilter());
                break;
            case JFileChooser.FILES_AND_DIRECTORIES:
                mFilter.setPrincipalFilter(new FileAndDirFilter());
                break;
            default:
                mFilter.setPrincipalFilter(new NullFilter());
                break;
        }
    }

    public void setMultiSelectionEnabled(boolean pB) {
        if (pB) {
            throw new IllegalArgumentException("Not implemented yet.");
        }
    }

    public boolean isMultiSelectionEnabled() {
        return false;
    }

    public File[] getSelectedFiles() {
        throw new IllegalArgumentException("Not implemented yet.");
    }

    public File getSelectedFile() {
        return new File(getDirectory(), getFile());
    }

    public void setCurrentDirectory(File pDir) {
        if (pDir == null) {
            return;
        }
        log.info("Setting dir to {}", pDir);
        super.setDirectory(pDir.getAbsolutePath());
    }

    public void setSelectedFile(File pFile) {
        super.setFile(pFile.getName());
    }

    public void registerDirectoryResultListener(
            DirectoryResultListener pDirectoryResultListener) {
        mDirectoryResultListener = pDirectoryResultListener;
    }

    public static void main(String[] args) throws IOException {

        FreeMindAwtFileDialog dialog = new FreeMindAwtFileDialog();
        dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dialog.showOpenDialog(null);
        File selectedFile = dialog.getSelectedFile();
        log.info("Dir '{}', File: '{}', selected File: '{}'", dialog.getDirectory(), dialog.getFile(), selectedFile);
        System.exit(0);
        dialog.showSaveDialog(null);
        selectedFile = dialog.getSelectedFile();
        log.info("Dir '{}', File: '{}', selected File: '{}'", dialog.getDirectory(), dialog.getFile(), selectedFile);
        StreamResult streamResult = new StreamResult(new FileOutputStream(selectedFile));
        streamResult.getOutputStream().write("bla".getBytes());
        streamResult.getOutputStream().close();
        log.info("File exists: {}", selectedFile.exists());
    }

    public void addChoosableFileFilterAsDefault(FileFilter pFilter) {
        addChoosableFileFilter(pFilter);
    }

}

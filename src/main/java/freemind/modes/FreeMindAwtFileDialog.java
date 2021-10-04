/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2012 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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

package freemind.modes;

import lombok.extern.log4j.Log4j2;

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
 *
 * @author foltin
 * @date 23.02.2012
 */
@SuppressWarnings("serial")
@Log4j2
public class FreeMindAwtFileDialog extends FileDialog implements FreeMindFileDialog {

    private static final String APPLE_AWT_FILE_DIALOG_FOR_DIRECTORIES = "apple.awt.fileDialogForDirectories";

    private final static class NullFilter extends FileFilter {

        /* (non-Javadoc)
         * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
         */
        public boolean accept(File pF) {
            return true;
        }

        /* (non-Javadoc)
         * @see javax.swing.filechooser.FileFilter#getDescription()
         */
        public String getDescription() {
            return "NullFilter";
        }

    }

    private final class DirFilter extends FileFilter {

        public boolean accept(File pF) {
            return pF.isDirectory();
        }

        /* (non-Javadoc)
         * @see javax.swing.filechooser.FileFilter#getDescription()
         */
        public String getDescription() {
            return "DirFilter";
        }

    }

    private final class FileOnlyFilter extends FileFilter {

        public boolean accept(File pF) {
            return pF.isFile();
        }

        /* (non-Javadoc)
         * @see javax.swing.filechooser.FileFilter#getDescription()
         */
        public String getDescription() {
            return "FileFilter";
        }

    }

    private final class FileAndDirFilter extends FileFilter {

        public boolean accept(File pF) {
            return pF.isFile() || pF.isDirectory();
        }

        public String getDescription() {
            return "FileAndDirFilter";
        }
    }

    private FreeMindFilenameFilter mFilter;
    private DirectoryResultListener mDirectoryResultListener = null;

    /**
     * @author foltin
     * @date 27.02.2012
     */
    private final class FreeMindFilenameFilter implements FilenameFilter {
        /**
         *
         */
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
                    mDirectoryResultListener
                            .setChosenDirectory(getSelectedFile()
                                    .getParentFile());
                } catch (Exception e) {
                    freemind.main.Resources.getInstance().logException(e);
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
        log.info("Setting dir to " + pDir);
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
        System.out.println("Dir '" + dialog.getDirectory() + "', File: '" + dialog.getFile() + "', selected File: '" + selectedFile + "'");
        System.exit(0);
        dialog.showSaveDialog(null);
        selectedFile = dialog.getSelectedFile();
        System.out.println("Dir '" + dialog.getDirectory() + "', File: '" + dialog.getFile() + "', selected File: '" + selectedFile + "'");
        StreamResult streamResult = new StreamResult(new FileOutputStream(selectedFile));
        streamResult.getOutputStream().write("bla".getBytes());
        streamResult.getOutputStream().close();
        System.out.println("File exists: " + selectedFile.exists());
    }

    /* (non-Javadoc)
     * @see freemind.modes.FreeMindFileDialog#addChoosableFileFilterAsDefault(javax.swing.filechooser.FileFilter)
     */
    public void addChoosableFileFilterAsDefault(FileFilter pFilter) {
        addChoosableFileFilter(pFilter);
    }

}

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

import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

/**
 * @author foltin
 * @date 23.02.2012
 */
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
     * Adds a further file filter for optional use. It is not selected by default, but this is UI dependent.
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

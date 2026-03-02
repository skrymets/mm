package freemind.modes;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;

/**
 * File Chooser for OS windows and linux (without Mac)
 */
@Slf4j
public class FreeMindJFileDialog extends JFileChooser implements FreeMindFileDialog {

    private DirectoryResultListener mDirectoryResultListener = null;

    @Override
    public void registerDirectoryResultListener(DirectoryResultListener pDirectoryResultListener) {
        mDirectoryResultListener = pDirectoryResultListener;
    }

    protected void callDirectoryListener(final int result) {
        if (result == JFileChooser.APPROVE_OPTION && mDirectoryResultListener != null) {
            try {
                mDirectoryResultListener.setChosenDirectory(getCurrentDirectory());
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    @Override
    public int showOpenDialog(Component pParent) throws HeadlessException {
        final int result = super.showOpenDialog(pParent);
        callDirectoryListener(result);
        return result;
    }

    @Override
    public int showSaveDialog(Component pParent) throws HeadlessException {
        final int result = super.showSaveDialog(pParent);
        callDirectoryListener(result);
        return result;
    }

    @Override
    public void addChoosableFileFilterAsDefault(FileFilter pFilter) {
        addChoosableFileFilter(pFilter);
        setFileFilter(pFilter);
    }
}

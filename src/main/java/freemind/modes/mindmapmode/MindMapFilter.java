package freemind.modes.mindmapmode;

import freemind.main.FreeMindCommon;
import freemind.main.Resources;
import org.apache.commons.io.FilenameUtils;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class MindMapFilter extends FileFilter {

    private final Resources resources;

    public MindMapFilter(Resources resources) {
        this.resources = resources;
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = FilenameUtils.getExtension(f.getName()).toLowerCase();
        if (extension != null) {
            return extension.equals(FreeMindCommon.FREEMIND_FILE_EXTENSION_WITHOUT_DOT);
        }
        return false;
    }

    public String getDescription() {
        return resources.getResourceString("mindmaps_desc");
    }
}

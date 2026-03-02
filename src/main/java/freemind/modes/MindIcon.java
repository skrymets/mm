package freemind.modes;

import freemind.main.Resources;
import freemind.main.Tools;
import freemind.view.ScalableImageIcon;
import lombok.Setter;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This class represents a MindIcon than can be applied to a node or a whole
 * branch.
 */
public class MindIcon implements Comparable<MindIcon>, IconInformation {

    public static final String PROPERTY_STRING_ICONS_LIST = "icons.list";
    /**
     * -- SETTER --
     *  Set the value of name.
     *
     */ /* here, we must check, whether the name is allowed. */ // DanPolansky: I suggest to avoid any checking. If the icon with the
    // name
    // does not exist, let's keep the name and save it again anyway. Let us
    // imagine the set of icons expanding and changing in the future.
    // Vector allIconNames = getAllIconNames();
    // for(int i = 0; i < allIconNames.size(); ++i) {
    // if(((String) allIconNames.get(i)).equals(v)) {
    // //System.out.println("Icon name: " + v);
    // this.name = v;
    // return;
    // }
    // }
    // throw new IllegalArgumentException("'"+v+"' is not a known icon.");
    // DanPolansky: we want to parse the file though. Not existent icon is
    // not
    // that a big tragedy.
    @Setter
    private String name;
    private int number = UNKNOWN;
    /**
     * Stores the once created ImageIcon.
     */
    private ImageIcon associatedIcon;
    private static List<String> mAllIconNames;
    private static ImageIcon iconNotFound;
    /**
     * Set of all created icons. Name -> MindIcon
     */
    private static Resources resources;
    private static final HashMap<String, MindIcon> createdIcons = new HashMap<>();
    private static final int UNKNOWN = -1;
    public static final int LAST = UNKNOWN;
    static int nextNumber = UNKNOWN - 1;
    private JComponent component = null;

    public static void init(Resources res) {
        resources = res;
    }

    private MindIcon(String name) {
        setName(name);
        associatedIcon = null;
    }

    private MindIcon(String name, ImageIcon icon) {
        setName(name);
        associatedIcon = icon;
    }

    public String toString() {
        return "Icon_name: " + name;
    }

    /**
     * Get the value of name.
     *
     * @return Value of name.
     */
    public String getName() {
        // DanPolansky: it's essential that we do not return null
        // for saving of the map.
        return name == null ? "notfound" : name;
    }

    public String getDescription() {
        String resource = "icon_" + getName();
        return resources.getResourceString(resource, resource);
    }

    public String getIconFileName() {
        return getIconsPath() + getIconBaseFileName();
    }

    public String getIconBaseFileName() {
        return getName() + ".png";
    }

    public static String getIconsPath() {
        return "images/icons/";
    }

    public ImageIcon getIcon() {
        // We need the frame to be able to obtain the resource URL of the icon.
        if (iconNotFound == null) {
            iconNotFound = freemind.view.ImageFactory.getInstance().createIcon(resources.getResource(
                    "images/IconNotFound.png"));
        }

        if (associatedIcon != null) {
            return associatedIcon;
        }
        if (name != null) {
            URL imageURL = resources.getResource(
                    getIconFileName());
            if (imageURL == null) { // As standard icon not found, try user's
                try {
                    final File file = new File(resources
                            .getFreemindDirectory(), "icons/" + getName()
                            + ".png");
                    if (file.canRead()) {
                        imageURL = Tools.fileToUrl(file);
                    }
                } catch (Exception ignored) {
                }
            }
            ImageIcon icon = imageURL == null ? iconNotFound : freemind.view.ImageFactory.getInstance().createIcon(
                    imageURL);
            setIcon(icon);
            return icon;
        } else {
            setIcon(iconNotFound);
            return iconNotFound;
        }
    }

    /**
     * Set the value of icon.
     *
     * @param _associatedIcon Value to assign to icon.
     */
    protected void setIcon(ImageIcon _associatedIcon) {
        this.associatedIcon = _associatedIcon;
    }

    public ImageIcon getUnscaledIcon() {
        if (associatedIcon instanceof ScalableImageIcon) {
            ScalableImageIcon scalableIcon = (ScalableImageIcon) associatedIcon;
            return scalableIcon.getUnscaledIcon();
        }
        return associatedIcon;
    }

    public static List<String> getAllIconNames() {
        if (mAllIconNames != null) {
            return mAllIconNames;
        }
        List<String> mAllIconNames = new ArrayList<>();
        String icons = resources.getProperty(
                PROPERTY_STRING_ICONS_LIST);
        StringTokenizer tokenizer = new StringTokenizer(icons, ";");
        while (tokenizer.hasMoreTokens()) {
            mAllIconNames.add(tokenizer.nextToken());
        }
        return mAllIconNames;
    }

    public static MindIcon factory(String iconName) {
        if (createdIcons.containsKey(iconName)) {
            return createdIcons.get(iconName);
        }
        MindIcon icon = new MindIcon(iconName);
        createdIcons.put(iconName, icon);
        return icon;
    }

    public static MindIcon factory(String iconName, ImageIcon icon) {
        if (createdIcons.containsKey(iconName)) {
            return createdIcons.get(iconName);
        }
        MindIcon mindIcon = new MindIcon(iconName, icon);
        getAllIconNames().add(iconName);
        createdIcons.put(iconName, mindIcon);
        return mindIcon;
    }

    public int compareTo(MindIcon icon) {
        int i1 = getNumber();
        int i2 = icon.getNumber();
        return i1 < i2 ? -1 : i1 == i2 ? 0 : +1;
    }

    private int getNumber() {
        if (number == UNKNOWN) {
            number = getAllIconNames().indexOf(name);
        }
        if (number == UNKNOWN) {
            number = nextNumber--;
        }
        return number;
    }

    public JComponent getRendererComponent() {
        if (component == null) {
            component = new JLabel(getIcon());
        }
        return component;
    }

    public String getKeystrokeResourceName() {
        return "keystroke_icon_" + name;
    }

    public KeyStroke getKeyStroke() {
        return null;
    }

}

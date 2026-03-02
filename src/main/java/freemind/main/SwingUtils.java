/*
 * FreeMind - a program for creating and viewing mindmaps
 * Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 * See COPYING for details
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package freemind.main;

import freemind.view.mindmapview.NodeView;
import org.apache.commons.lang3.SystemUtils;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Swing/UI utility methods extracted from {@link Tools}.
 */
@Slf4j
public final class SwingUtils {

    private static Resources resources;

    private SwingUtils() {
        // utility class
    }

    public static void init(Resources res) {
        resources = res;
    }

    // ---- Font methods ----

    private static String[] sEnvFonts = null;

    public static String[] getAvailableFonts() {
        if (sEnvFonts == null) {
            GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            sEnvFonts = gEnv.getAvailableFontFamilyNames();
        }
        return sEnvFonts;
    }

    public static boolean isAvailableFontFamily(String name) {
        for (String s : getAvailableFonts()) {
            if (s.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adapts the font size inside of a component to the zoom.
     *
     * @param zoom           zoom factor
     * @param normalFontSize "unzoomed" normal font size.
     * @return a copy of the input font (if the size was effectively changed)
     * with the correct scale.
     */
    public static Font updateFontSize(Font font, float zoom, int normalFontSize) {
        if (font == null) {
            return null;
        }

        float oldFontSize = font.getSize2D();
        float newFontSize = normalFontSize * zoom;

        return oldFontSize == newFontSize ? font : font.deriveFont(newFontSize);
    }

    // ---- Dialog methods ----

    public static void addEscapeActionToDialog(final JDialog dialog) {
        class EscapeAction extends AbstractAction {

            private static final long serialVersionUID = 238333614987438806L;

            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }

        }
        addEscapeActionToDialog(dialog, new EscapeAction());
    }

    public static void addEscapeActionToDialog(JDialog dialog, Action action) {
        addKeyActionToDialog(dialog, action, "ESCAPE", "end_dialog");
    }

    public static void addKeyActionToDialog(JDialog dialog, Action action,
                                            String keyStroke, String actionId) {
        action.putValue(Action.NAME, actionId);
        // Register keystroke
        dialog.getRootPane()
                .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(keyStroke),
                        action.getValue(Action.NAME));

        // Register action
        dialog.getRootPane().getActionMap()
                .put(action.getValue(Action.NAME), action);
    }

    public static void setDialogLocationRelativeTo(JDialog dialog, Component c) {
        if (c == null) {
            // perhaps, the component is not yet existing.
            return;
        }
        if (c instanceof NodeView) {
            final NodeView nodeView = (NodeView) c;
            nodeView.getMap().getScrollService().scrollNodeToVisible(nodeView);
            c = nodeView.getMainView();
        }
        final Point compLocation = c.getLocationOnScreen();
        final int cw = c.getWidth();
        final int ch = c.getHeight();

        final Container parent = dialog.getParent();
        final Point parentLocation = parent.getLocationOnScreen();
        final int pw = parent.getWidth();
        final int ph = parent.getHeight();

        final int dw = dialog.getWidth();
        final int dh = dialog.getHeight();

        final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = defaultToolkit.getScreenSize();
        final Insets screenInsets = defaultToolkit.getScreenInsets(dialog
                .getGraphicsConfiguration());

        final int minX = Math.max(parentLocation.x, screenInsets.left);
        final int minY = Math.max(parentLocation.y, screenInsets.top);

        final int maxX = Math.min(parentLocation.x + pw, screenSize.width
                - screenInsets.right);
        final int maxY = Math.min(parentLocation.y + ph, screenSize.height
                - screenInsets.bottom);

        int dx, dy;

        if (compLocation.x + cw < minX) {
            dx = minX;
        } else if (compLocation.x > maxX) {
            dx = maxX - dw;
        } else // component X on screen
        {
            final int leftSpace = compLocation.x - minX;
            final int rightSpace = maxX - (compLocation.x + cw);
            if (leftSpace > rightSpace) {
                if (leftSpace > dw) {
                    dx = compLocation.x - dw;
                } else {
                    dx = minX;
                }
            } else if (rightSpace > dw) {
                dx = compLocation.x + cw;
            } else {
                dx = maxX - dw;
            }
        }

        if (compLocation.y + ch < minY) {
            dy = minY;
        } else if (compLocation.y > maxY) {
            dy = maxY - dh;
        } else // component Y on screen
        {
            final int topSpace = compLocation.y - minY;
            final int bottomSpace = maxY - (compLocation.y + ch);
            if (topSpace > bottomSpace) {
                if (topSpace > dh) {
                    dy = compLocation.y - dh;
                } else {
                    dy = minY;
                }
            } else if (bottomSpace > dh) {
                dy = compLocation.y + ch;
            } else {
                dy = maxY - dh;
            }
        }

        dialog.setLocation(dx, dy);
    }

    // ---- Mnemonic/Label methods ----

    /**
     * Ampersand indicates that the character after it is a mnemo, unless the
     * character is a space. In "Find &amp; Replace", ampersand does not label
     * mnemo, while in "&amp;About", mnemo is "Alt + A".
     */
    public static void setLabelAndMnemonic(AbstractButton btn, String inLabel) {
        setLabelAndMnemonic(new ButtonHolder(btn), inLabel);
    }

    /**
     * Ampersand indicates that the character after it is a mnemo, unless the
     * character is a space. In "Find &amp; Replace", ampersand does not label
     * mnemo, while in "&amp;About", mnemo is "Alt + A".
     */
    public static void setLabelAndMnemonic(Action action, String inLabel) {
        setLabelAndMnemonic(new ActionHolder(action), inLabel);
    }

    private static void setLabelAndMnemonic(NameMnemonicHolder mnemonicHolder, String labelToSet) {
        String rawLabel = labelToSet;

        if (rawLabel == null) {
            rawLabel = mnemonicHolder.getText();
        }
        if (rawLabel == null) {
            return;
        }
        mnemonicHolder.setText(removeMnemonic(rawLabel));
        int mnemoSignIndex = rawLabel.indexOf("&");
        if (mnemoSignIndex >= 0 && mnemoSignIndex + 1 < rawLabel.length()) {
            char charAfterMnemoSign = rawLabel.charAt(mnemoSignIndex + 1);
            if (charAfterMnemoSign != ' ') {
                // no mnemonics under Mac OS:
                if (!SystemUtils.IS_OS_MAC) {
                    mnemonicHolder.setMnemonic(charAfterMnemoSign);
                    // sets the underline to exactly this character.
                    mnemonicHolder.setDisplayedMnemonicIndex(mnemoSignIndex);
                }
            }
        }
    }

    public static String removeMnemonic(String rawLabel) {
        return rawLabel.replaceFirst("&([^ ])", "$1");
    }

    // ---- Graphics/Rendering methods ----

    public static void restoreAntialiasing(Graphics2D g, Object renderingHint) {
        if (RenderingHints.KEY_ANTIALIASING.isCompatibleValue(renderingHint)) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, renderingHint);
        }
    }

    public static void scaleAllFonts(float pScale) {
        for (Object next : UIManager.getLookAndFeelDefaults().keySet()) {
            if (next instanceof String) {
                String key = (String) next;
                if (key.endsWith(".font")) {
                    Font font = UIManager.getFont(key);
                    Font biggerFont = font.deriveFont(pScale * font.getSize2D());
                    // change ui default to bigger font
                    UIManager.put(key, biggerFont);
                }
            }
        }
    }

    // ---- Scaling methods ----

    public static float getScalingFactor() {
        return getScalingFactorPlain() / 100.0f;
    }

    public static int getScalingFactorPlain() {
        return resources.getIntProperty(FreeMind.SCALING_FACTOR_PROPERTY, 100);
    }

    // ---- Event methods ----

    public static void waitForEventQueue() {
        try {
            // wait until AWT thread starts
            if (!EventQueue.isDispatchThread()) {
                EventQueue.invokeAndWait((Runnable) () -> {
                }
                );
            } else {
                log.warn("Can't wait for event queue, if I'm inside this queue!");
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * Call this method, if you don't know, if you are in the event thread or
     * not. It checks this and calls the invokeandwait or the runnable directly.
     */
    public static void invokeAndWait(Runnable pRunnable)
            throws InvocationTargetException, InterruptedException {
        if (EventQueue.isDispatchThread()) {
            pRunnable.run();
        } else {
            EventQueue.invokeAndWait(pRunnable);
        }
    }

    // ---- Platform/display methods ----

    public static boolean isHeadless() {
        return GraphicsEnvironment.isHeadless();
    }

    public static boolean isRetina() {
        GraphicsEnvironment env = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        final GraphicsDevice device = env.getDefaultScreenDevice();

        try {
            Field field = device.getClass().getDeclaredField("scale");

            if (field != null) {
                field.setAccessible(true);
                Object scale = field.get(device);

                if (scale instanceof Integer
                        && ((Integer) scale).intValue() == 2) {
                    return true;
                }
            }
        } catch (ReflectiveOperationException ignore) {
        }
        return false;
    }

    public static void correctJSplitPaneKeyMap() {
        InputMap map = (InputMap) UIManager.get("SplitPane.ancestorInputMap");
        KeyStroke keyStrokeF6 = KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0);
        KeyStroke keyStrokeF8 = KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0);
        map.remove(keyStrokeF6);
        map.remove(keyStrokeF8);
    }

    public static void logTransferable(Transferable t) {
        log.debug("BEGIN OF Transferable: {}", t);
        DataFlavor[] dataFlavors = t.getTransferDataFlavors();
        for (DataFlavor dataFlavor : dataFlavors) {
            log.debug("  Flavor: {}", dataFlavor);
            log.debug("    Supported: {}", t.isDataFlavorSupported(dataFlavor));
            try {
                log.debug("    Content: {}", t.getTransferData(dataFlavor));
            } catch (Exception ignored) {
            }
        }
        log.debug("END OF Transferable");
    }

    // ---- Helper inner types for mnemonic handling ----

    interface NameMnemonicHolder {
        String getText();
        void setText(String replaceAll);
        void setMnemonic(char charAfterMnemoSign);
        void setDisplayedMnemonicIndex(int mnemoSignIndex);
    }

    private static class ButtonHolder implements NameMnemonicHolder {

        private final AbstractButton btn;

        public ButtonHolder(AbstractButton btn) {
            super();
            this.btn = btn;
        }

        public String getText() {
            return btn.getText();
        }

        public void setDisplayedMnemonicIndex(int mnemoSignIndex) {
            btn.setDisplayedMnemonicIndex(mnemoSignIndex);
        }

        public void setMnemonic(char charAfterMnemoSign) {
            btn.setMnemonic(charAfterMnemoSign);
        }

        public void setText(String text) {
            btn.setText(text);
        }

    }

    private static class ActionHolder implements NameMnemonicHolder {

        private final Action action;

        public ActionHolder(Action action) {
            super();
            this.action = action;
        }

        public String getText() {
            return action.getValue(Action.NAME).toString();
        }

        public void setDisplayedMnemonicIndex(int mnemoSignIndex) {
        }

        public void setMnemonic(char charAfterMnemoSign) {
            int vk = charAfterMnemoSign;
            if (vk >= 'a' && vk <= 'z') {
                vk -= ('a' - 'A');
            }
            action.putValue(Action.MNEMONIC_KEY, Integer.valueOf(vk));
        }

        public void setText(String text) {
            action.putValue(Action.NAME, text);
        }

    }
}

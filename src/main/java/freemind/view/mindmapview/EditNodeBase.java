package freemind.view.mindmapview;

import freemind.controller.Controller;
import freemind.main.FreeMindCommon;
import freemind.main.FreeMindMain;
import freemind.main.MindMapUtils;
import freemind.modes.ModeController;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.Iterator;
import java.util.LinkedList;

public class EditNodeBase {
    protected static Boolean checkSpelling;
    protected static final int BUTTON_OK = 0;
    protected static final int BUTTON_CANCEL = 1;
    protected static final int BUTTON_SPLIT = 2;
    @Getter
    protected final NodeView node;
    @Getter
    private final EditControl editControl;
    private final ModeController controller;
    @Setter
    protected String text;
    // this enables from outside close the edit mode
    @Setter
    @Getter
    protected FocusListener textFieldListener = null;

    EditNodeBase(final NodeView node, final String text,
                 ModeController controller, EditControl editControl) {
        this.controller = controller;
        this.editControl = editControl;
        this.node = node;
        this.text = text;
        if (checkSpelling == null) {
            checkSpelling = controller.getResources().getBoolProperty(FreeMindCommon.CHECK_SPELLING);
        }
    }

    abstract static class EditDialog extends JDialog {
        private static final long serialVersionUID = 6064679828160694117L;
        private EditNodeBase base;

        class DialogWindowListener extends WindowAdapter {
            public void windowClosing(WindowEvent e) {
                if (isVisible()) {
                    confirmedSubmit();
                }
            }
        }

        class SubmitAction extends AbstractAction {
            private static final long serialVersionUID = -859458051986869388L;

            public void actionPerformed(ActionEvent e) {
                submit();
            }
        }

        class SplitAction extends AbstractAction {
            private static final long serialVersionUID = 6876147686811246433L;

            public void actionPerformed(ActionEvent e) {
                split();
            }
        }

        class CancelAction extends AbstractAction {
            private static final long serialVersionUID = -6277471363654329607L;

            public void actionPerformed(ActionEvent e) {
                confirmedCancel();
            }
        }

        EditDialog(EditNodeBase base) {
            super((JFrame) base.getFrame(), base.getText("edit_long_node"),
                    /*modal = */ true);
            getContentPane().setLayout(new BorderLayout());
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            DialogWindowListener dfl = new DialogWindowListener();
            addWindowListener(dfl);
            this.base = base;
        }

        protected void confirmedSubmit() {
            if (isChanged()) {
                final int action = JOptionPane.showConfirmDialog(this,
                        base.getText("long_node_changed_submit"), "",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                if (action == JOptionPane.CANCEL_OPTION)
                    return;
                if (action == JOptionPane.YES_OPTION) {
                    submit();
                    return;
                }
            }
            cancel();
        }

        protected void confirmedCancel() {
            if (isChanged()) {
                final int action = JOptionPane.showConfirmDialog(this,
                        base.getText("long_node_changed_cancel"), "",
                        JOptionPane.OK_CANCEL_OPTION);
                if (action == JOptionPane.CANCEL_OPTION)
                    return;
            }
            cancel();
        }

        protected void submit() {
            setVisible(false);
        }

        protected void cancel() {
            setVisible(false);
        }

        protected void split() {
            setVisible(false);
        }

        abstract protected boolean isChanged();

        /**
         * @param base The base to set.
         */
        void setBase(EditNodeBase base) {
            this.base = base;
        }

        /**
         * @return Returns the base.
         */
        EditNodeBase getBase() {
            return base;
        }
    }

    public interface EditControl {
        void cancel();

        void ok(String newText);

        void split(String newText, int position);
    }

    protected MapView getView() {
        return controller.getView();
    }

    protected ModeController getModeController() {
        return controller;
    }

    protected Controller getController() {
        return controller.getController();
    }

    protected String getText(String string) {
        return controller.getText(string);
    }

    protected FreeMindMain getFrame() {
        return controller.getFrame();
    }

    protected boolean binOptionIsTrue(String option) {
        return getModeController().getResources().getBoolProperty(option);
    }

    protected class EditCopyAction extends AbstractAction {
        private static final long serialVersionUID = 5104219263806454592L;
        private final JTextComponent textComponent;

        public EditCopyAction(JTextComponent textComponent) {
            super(getText("copy"));
            this.textComponent = textComponent;
        }

        public void actionPerformed(ActionEvent e) {
            String selection = textComponent.getSelectedText();
            if (selection != null) {
                getClipboard().setContents(new StringSelection(selection), null);
            }
        }
    }

    protected class EditPopupMenu extends JPopupMenu {
        private static final long serialVersionUID = -6667980271052571216L;

        public EditPopupMenu(JTextComponent textComponent) {
            EditCopyAction editCopyAction = new EditCopyAction(textComponent);
            String selectedText = textComponent.getSelectedText();
            if (selectedText == null || selectedText.isEmpty()) {
                editCopyAction.setEnabled(false);
            }
            this.add(editCopyAction);
        }
    }

    public void closeEdit() {
        if (textFieldListener != null) {
            textFieldListener.focusLost(null); // hack to close the edit
        }
    }

    protected String getText() {
        return text;
    }

    public Clipboard getClipboard() {
        return MindMapUtils.getClipboard();
    }

    protected void redispatchKeyEvents(final JTextComponent textComponent,
                                       KeyEvent firstKeyEvent) {
        if (textComponent.hasFocus()) {
            return;
        }
        final KeyboardFocusManager currentKeyboardFocusManager =
                KeyboardFocusManager.getCurrentKeyboardFocusManager();
        class KeyEventQueue implements KeyEventDispatcher, FocusListener {
            final LinkedList<KeyEvent> events = new LinkedList<>();

            public boolean dispatchKeyEvent(KeyEvent e) {
                events.add(e);
                return true;
            }

            public void focusGained(FocusEvent e) {
                e.getComponent().removeFocusListener(this);
                currentKeyboardFocusManager.removeKeyEventDispatcher(this);
                final Iterator<KeyEvent> iterator = events.iterator();
                while (iterator.hasNext()) {
                    final KeyEvent ke = iterator.next();
                    ke.setSource(textComponent);
                    textComponent.dispatchEvent(ke);
                }
            }

            public void focusLost(FocusEvent e) {
            }
        }
        final KeyEventQueue keyEventDispatcher = new KeyEventQueue();
        currentKeyboardFocusManager.addKeyEventDispatcher(keyEventDispatcher);
        textComponent.addFocusListener(keyEventDispatcher);
        if (firstKeyEvent == null) {
            return;
        }
        if (firstKeyEvent.getKeyChar() == KeyEvent.CHAR_UNDEFINED) {
            switch (firstKeyEvent.getKeyCode()) {
                case KeyEvent.VK_HOME:
                    textComponent.setCaretPosition(0);
                    break;
                case KeyEvent.VK_END:
                    textComponent.setCaretPosition(textComponent.getDocument()
                            .getLength());
                    break;
            }
        } else {
            textComponent.selectAll(); // to enable overwrite
            // redispath all key events
            textComponent.dispatchEvent(firstKeyEvent);
        }
    }
}

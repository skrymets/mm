package freemind.view.mindmapview;

import com.inet.jortho.SpellChecker;
import freemind.main.Tools;
import freemind.main.SwingUtils;
import freemind.modes.ModeController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EditNodeDialog extends EditNodeBase {
    private final KeyEvent firstEvent;

    /**
     * Private variable to hold the last value of the "Enter confirms" state.
     */
//    private static Boolean[] booleanHolderForConfirmState = new Boolean[]{false};
    private static Boolean booleanHolderForConfirmState = null;

    public EditNodeDialog(final NodeView node, final String text,
                          final KeyEvent firstEvent, ModeController controller,
                          EditControl editControl) {
        super(node, text, controller, editControl);
        this.firstEvent = firstEvent;
    }

    class LongNodeDialog extends EditDialog {
        private static final long serialVersionUID = 6185443281994675732L;
        private final JTextArea textArea;

        LongNodeDialog() {
            super(EditNodeDialog.this);
            textArea = new JTextArea(getText());
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            // wish from
            // https://sourceforge.net/forum/message.php?msg_id=5923410
            // textArea.setTabSize(4);
            // wrap around words rather than characters

            final JScrollPane editorScrollPane = new JScrollPane(textArea);
            editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            int preferredHeight = getNode().getHeight();
            preferredHeight = Math.max(preferredHeight, Integer.parseInt(getFrame().getProperty("el__min_default_window_height")));
            preferredHeight = Math.min(preferredHeight, Integer.parseInt(getFrame().getProperty("el__max_default_window_height")));

            int preferredWidth = getNode().getWidth();
            preferredWidth = Math.max(preferredWidth, Integer.parseInt(getFrame().getProperty("el__min_default_window_width")));
            preferredWidth = Math.min(preferredWidth, Integer.parseInt(getFrame().getProperty("el__max_default_window_width")));

            editorScrollPane.setPreferredSize(new Dimension(preferredWidth,
                    preferredHeight));
            // textArea.setPreferredSize(new Dimension(500, 160));

            final JPanel panel = new JPanel();

            // String performedAction;
            final JButton okButton = new JButton();
            final JButton cancelButton = new JButton();
            final JButton splitButton = new JButton();
            final JCheckBox enterConfirms = new JCheckBox("",
                    binOptionIsTrue("el__enter_confirms_by_default"));

            SwingUtils.setLabelAndMnemonic(okButton, getText("ok"));
            SwingUtils.setLabelAndMnemonic(cancelButton, getText("cancel"));
            SwingUtils.setLabelAndMnemonic(splitButton, getText("split"));
            SwingUtils.setLabelAndMnemonic(enterConfirms, getText("enter_confirms"));

            if (booleanHolderForConfirmState == null) {
                booleanHolderForConfirmState = enterConfirms.isSelected();
            } else {
                enterConfirms.setSelected(booleanHolderForConfirmState);
            }

            okButton.addActionListener(e -> {
                // next try to avoid bug 1159: focus jumps to file-menu after closing html-editing-window
                EventQueue.invokeLater(this::submit);
            });

            cancelButton.addActionListener(e -> cancel());

            splitButton.addActionListener(e -> split());

            enterConfirms.addActionListener(e -> {
                textArea.requestFocus();
                booleanHolderForConfirmState = enterConfirms.isSelected();
            });

            // On Enter act as if OK button was pressed

            textArea.addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e) {
                    // escape key in long text editor (PN)
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        e.consume();
                        confirmedCancel();
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (enterConfirms.isSelected() && (e.getModifiers() & KeyEvent.SHIFT_MASK) != 0) {
                            e.consume();
                            textArea.insert("\n", textArea.getCaretPosition());
                        } else if (enterConfirms.isSelected() || ((e.getModifiers() & KeyEvent.ALT_MASK) != 0)) {
                            e.consume();
                            submit();
                        } else {
                            e.consume();
                            textArea.insert("\n", textArea.getCaretPosition());
                        }
                    }
                }

                public void keyTyped(KeyEvent e) {
                }

                public void keyReleased(KeyEvent e) {
                }
            });

            textArea.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                }

                public void mouseEntered(MouseEvent e) {
                }

                public void mouseExited(MouseEvent e) {
                }

                public void mousePressed(MouseEvent e) {
                    conditionallyShowPopup(e);
                }

                public void mouseReleased(MouseEvent e) {
                    conditionallyShowPopup(e);
                }

                private void conditionallyShowPopup(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        JPopupMenu popupMenu = new EditPopupMenu(textArea);
                        if (checkSpelling) {
                            popupMenu.add(SpellChecker.createCheckerMenu());
                            popupMenu.add(SpellChecker.createLanguagesMenu());
                        }
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                        e.consume();
                    }
                }
            });

            Font font = getNode().getTextFont();
            font = SwingUtils.updateFontSize(font, getView().getZoom(), font.getSize());
            textArea.setFont(font);

            final Color nodeTextColor = getNode().getTextColor();
            textArea.setForeground(nodeTextColor);
            final Color nodeTextBackground = getNode().getTextBackground();
            textArea.setBackground(nodeTextBackground);
            textArea.setCaretColor(nodeTextColor);

            // panel.setPreferredSize(new Dimension(500, 160));
            // editorScrollPane.setPreferredSize(new Dimension(500, 160));

            JPanel buttonPane = new JPanel();
            buttonPane.add(enterConfirms);
            buttonPane.add(okButton);
            buttonPane.add(cancelButton);
            buttonPane.add(splitButton);
            buttonPane.setMaximumSize(new Dimension(1000, 20));

            if ("above".equals(getFrame().getProperty("el__buttons_position"))) {
                panel.add(buttonPane);
                panel.add(editorScrollPane);
            } else {
                panel.add(editorScrollPane);
                panel.add(buttonPane);
            }

            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            setContentPane(panel);

            if (firstEvent instanceof KeyEvent) {
                redispatchKeyEvents(textArea, firstEvent);
            } // 1st key event defined
            else {
                textArea.setCaretPosition(getText().length());
            }

            if (checkSpelling) {
                SpellChecker.register(textArea, false, true, true, true);
            }
        }

        public void show() {
            textArea.requestFocus();
            super.setVisible(true);
        }

        protected void cancel() {
            getEditControl().cancel();
            super.cancel();
        }

        protected void split() {
            getEditControl().split(textArea.getText(), textArea.getCaretPosition());
            super.split();
        }

        protected void submit() {
            getEditControl().ok(textArea.getText());
            super.submit();
        }

        protected boolean isChanged() {
            return !getText().equals(textArea.getText());
        }

        public Component getMostRecentFocusOwner() {
            if (isFocused()) {
                return getFocusOwner();
            } else {
                return textArea;
            }
        }
    }

    public void show() {
        final EditDialog dialog = new LongNodeDialog();
        dialog.pack(); // calculate the size

        // set position
        getView().getScrollService().scrollNodeToVisible(getNode(), 0);
        SwingUtils.setDialogLocationRelativeTo(dialog, getNode());
        dialog.setVisible(true);
    }
}

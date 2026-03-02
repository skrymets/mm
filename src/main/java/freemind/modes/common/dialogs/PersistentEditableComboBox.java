package freemind.modes.common.dialogs;

import freemind.modes.ModeController;

import javax.swing.*;
import java.awt.event.ActionListener;

public class PersistentEditableComboBox extends JComboBox {
    private ActionListener actionListener = null;

    private boolean sendExternalEvents = true;

    private final ModeController mModeController;

    private final String pStorageKey;

    public PersistentEditableComboBox(ModeController modeController,
                                      String storageKey) {
        this.mModeController = modeController;
        this.pStorageKey = storageKey;
        setEditable(true);

        addUrl("", false);
        String storedUrls = mModeController.getFrame().getProperty(pStorageKey);
        if (storedUrls != null) {
            String[] array = storedUrls.split("\t");
            for (String string : array) {
                addUrl(string, false);
            }
        }
        setSelectedIndex(0);
        super.addActionListener(arg0 -> {
            addUrl(getText(), false);
            // notification only if a new string is entered.
            if (sendExternalEvents && actionListener != null) {
                actionListener.actionPerformed(arg0);
            }
        });
    }

    public void addActionListener(ActionListener arg0) {
        this.actionListener = arg0;
    }

    private boolean addUrl(String selectedItem, boolean calledFromSetText) {
        // search:
        for (int i = 0; i < getModel().getSize(); i++) {
            String element = (String) getModel().getElementAt(i);
            if (element.equals(selectedItem)) {
                if (calledFromSetText) {
                    setSelectedIndex(i);
                }
                return false;
            }
        }
        addItem(selectedItem);
        setSelectedIndex(getModel().getSize() - 1);
        if (calledFromSetText) {
            StringBuilder resultBuffer = new StringBuilder();
            for (int i = 0; i < getModel().getSize(); i++) {
                String element = (String) getModel().getElementAt(i);
                resultBuffer.append(element);
                resultBuffer.append("\t");
            }
            mModeController.getFrame().setProperty(pStorageKey,
                    resultBuffer.toString());
        }
        return true;
    }

    public String getText() {
        return getSelectedItem().toString();
    }

    public void setText(String text) {
        sendExternalEvents = false;
        addUrl(text, true);
        sendExternalEvents = true;
    }
}

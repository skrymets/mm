package freemind.common;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import freemind.main.FreeMindMain;
import freemind.modes.IconInformation;
import freemind.modes.MindIcon;
import freemind.modes.common.dialogs.IconSelectionPopupDialog;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class IconProperty extends PropertyBean implements PropertyControl, ActionListener {
    @Getter
    final String description;

    @Getter
    final String label;

    final JButton mButton;

    private final FreeMindMain mFreeMindMain;

    private final List<MindIcon> mIcons;

    private MindIcon mActualIcon = null;

    public IconProperty(String description, String label, FreeMindMain frame, List<MindIcon> icons) {
        super();
        this.description = description;
        this.label = label;
        this.mFreeMindMain = frame;
        this.mIcons = icons;
        mButton = new JButton();
        mButton.addActionListener(this);
    }

    public void setValue(String value) {
        for (MindIcon icon : mIcons) {
            if (icon.getName().equals(value)) {
                mActualIcon = icon;
                setIcon(mActualIcon);
            }
        }
    }

    private void setIcon(MindIcon actualIcon) {
        mButton.setIcon(actualIcon.getIcon());
        mButton.setToolTipText(actualIcon.getDescription());
    }

    public String getValue() {
        return mActualIcon.getName();
    }

    public void layout(DefaultFormBuilder builder, TextTranslator pTranslator) {
        JLabel label = builder.append(pTranslator.getText(getLabel()), mButton);
        label.setToolTipText(pTranslator.getText(getDescription()));
    }

    public void actionPerformed(ActionEvent arg0) {
        List<IconInformation> icons = new ArrayList<>();
        List<String> descriptions = new ArrayList<>();
        for (MindIcon icon : mIcons) {
            icons.add(icon);
            descriptions.add(icon.getDescription());
        }
        IconSelectionPopupDialog dialog = new IconSelectionPopupDialog(mFreeMindMain.getJFrame(), icons, mFreeMindMain);
        dialog.setLocationRelativeTo(mFreeMindMain.getJFrame());
        dialog.setModal(true);
        dialog.setVisible(true);
        int result = dialog.getResult();
        if (result >= 0) {
            MindIcon icon = mIcons.get(result);
            setValue(icon.getName());
            firePropertyChangeEvent();
        }
    }

    public void setEnabled(boolean pEnabled) {
        mButton.setEnabled(pEnabled);
    }

}

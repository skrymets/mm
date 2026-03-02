package freemind.common;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class FontProperty extends PropertyBean implements PropertyControl {
    @Getter
    final String description;

    @Getter
    final String label;

    Font font = null;

    final JComboBox<String> mFontComboBox = new JComboBox<>();

    private final String[] mAvailableFontFamilyNames;

    public FontProperty(String description, String label, TextTranslator pTranslator) {
        super();
        this.description = description;
        this.label = label;
        mAvailableFontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        mFontComboBox.setModel(new DefaultComboBoxModel<>(mAvailableFontFamilyNames));
        mFontComboBox.addActionListener(pE -> firePropertyChangeEvent());
    }

    public void layout(DefaultFormBuilder builder, TextTranslator pTranslator) {
        JLabel label = builder.append(pTranslator.getText(getLabel()), mFontComboBox);
        label.setToolTipText(pTranslator.getText(getDescription()));

    }

    public void setValue(String pValue) {
        for (int i = 0; i < mAvailableFontFamilyNames.length; i++) {
            String fontName = mAvailableFontFamilyNames[i];
            if (fontName.equals(pValue)) {
                mFontComboBox.setSelectedIndex(i);
                return;
            }
        }
        log.warn("Unknown value: {}", pValue);
        if (mFontComboBox.getModel().getSize() > 0) {
            mFontComboBox.setSelectedIndex(0);
        }
    }

    public String getValue() {
        return mAvailableFontFamilyNames[mFontComboBox.getSelectedIndex()];
    }

    public void setEnabled(boolean pEnabled) {
        mFontComboBox.setEnabled(pEnabled);
    }

}

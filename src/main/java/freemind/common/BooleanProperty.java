package freemind.common;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import lombok.Getter;

import javax.swing.*;

import static java.text.MessageFormat.format;

public class BooleanProperty extends PropertyBean implements PropertyControl {
    static public final String FALSE_VALUE = "false";

    static public final String TRUE_VALUE = "true";

    protected String mFalseValue = FALSE_VALUE;

    protected String mTrueValue = TRUE_VALUE;

    @Getter
    final String description;

    @Getter
    final String label;

    final JCheckBox mCheckBox = new JCheckBox();

    public BooleanProperty(String description, String label) {
        super();
        this.description = description;
        this.label = label;
        mCheckBox.addItemListener(pE -> firePropertyChangeEvent());
    }

    public void setValue(String value) {
        if (value == null || !(value.toLowerCase().equals(mTrueValue) || value.toLowerCase().equals(mFalseValue))) {
            throw new IllegalArgumentException(format("Cannot set a boolean to ''{0}'', allowed are {1} and {2}.", value, mTrueValue, mFalseValue));
        }
        mCheckBox.setSelected(value.toLowerCase().equals(mTrueValue));
    }

    public String getValue() {
        return mCheckBox.isSelected() ? mTrueValue : mFalseValue;
    }

    public void layout(DefaultFormBuilder builder, TextTranslator pTranslator) {
        JLabel label = builder.append(pTranslator.getText(getLabel()), mCheckBox);
        label.setToolTipText(pTranslator.getText(getDescription()));
    }

    public void setEnabled(boolean pEnabled) {
        mCheckBox.setEnabled(pEnabled);
    }

}

package freemind.common;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class StringProperty extends PropertyBean implements PropertyControl {
    @Getter
    final String description;

    @Getter
    final String label;

    JTextField mTextField;

    public StringProperty(String description, String label) {
        super();
        initializeTextfield();
        this.description = description;
        this.label = label;

        mTextField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent pE) {
                firePropertyChangeEvent();
            }
        });

    }

    /**
     * To be overwritten by PasswordProperty
     */
    protected void initializeTextfield() {
        mTextField = new JTextField();
    }

    public void setValue(String value) {
        mTextField.setText(value);
        mTextField.selectAll();
    }

    public String getValue() {
        return mTextField.getText();
    }

    public void layout(DefaultFormBuilder builder, TextTranslator pTranslator) {
        JLabel label = builder.append(pTranslator.getText(getLabel()), mTextField);
        label.setToolTipText(pTranslator.getText(getDescription()));
    }

    public void setEnabled(boolean pEnabled) {
        mTextField.setEnabled(pEnabled);
    }

}

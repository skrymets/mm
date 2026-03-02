package freemind.common;

import freemind.main.EncryptionUtils;

import javax.swing.*;

public class PasswordProperty extends StringProperty {

    public PasswordProperty(String pDescription, String pLabel) {
        super(pDescription, pLabel);
    }

    protected void initializeTextfield() {
        mTextField = new JPasswordField();
    }

    public String getValue() {
        return EncryptionUtils.compress(mTextField.getText());
    }

    public void setValue(String value) {
        String pwInPlain = EncryptionUtils.decompress(value);
        super.setValue(pwInPlain);
    }

}

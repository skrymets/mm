package freemind.common;

import com.jgoodies.forms.builder.DefaultFormBuilder;

public class NextLineProperty implements PropertyControl {

    public NextLineProperty() {
        super();
    }

    public String getDescription() {
        return null;
    }

    public String getLabel() {
        return null;
    }

    public void layout(DefaultFormBuilder builder, TextTranslator pTranslator) {
        builder.nextLine();
    }

    public void setEnabled(boolean pEnabled) {

    }

}
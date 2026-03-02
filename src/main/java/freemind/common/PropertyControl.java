package freemind.common;

import com.jgoodies.forms.builder.DefaultFormBuilder;

public interface PropertyControl {

    String getDescription();

    String getLabel();

    void layout(DefaultFormBuilder builder, TextTranslator pTranslator);

    void setEnabled(boolean pEnabled);
}
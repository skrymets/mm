package freemind.common;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import lombok.Getter;

@Getter
public class SeparatorProperty implements PropertyControl {

    final String label;

    public SeparatorProperty(String label) {
        super();
        this.label = label;
    }

    public String getDescription() {
        return null;
    }

    public void layout(DefaultFormBuilder builder, TextTranslator pTranslator) {
        builder.appendSeparator(pTranslator.getText("separator." + getLabel()));
    }

    public void setEnabled(boolean pEnabled) {

    }

}

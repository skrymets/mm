package freemind.common;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

@Slf4j
public class NumberProperty extends PropertyBean implements PropertyControl {
    @Getter
    final String description;
    @Getter
    final String label;
    private final JSpinner spinner;
    private final int min;
    private final int max;
    private final int step;

    public NumberProperty(String description, String label, int min, int max,
                          int step) {
        this.min = min;
        this.max = max;
        this.step = step;
        spinner = new JSpinner(new SpinnerNumberModel(min, min, max, step));

        this.description = description;
        this.label = label;
        spinner.addChangeListener(pE -> firePropertyChangeEvent());

    }

    public void setValue(String value) {
        int intValue = min;
        try {
            intValue = Integer.parseInt(value);
            int stepModul = (intValue - min) % step;
            if (intValue < min || intValue > max || (stepModul != 0)) {
                log.warn("Actual value of property {} is not in the allowed range: {}", getLabel(), value);
                intValue = min;
            }
        } catch (NumberFormatException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        spinner.setValue(intValue);
    }

    public String getValue() {
        return spinner.getValue().toString();
    }

    public int getIntValue() {
        return (Integer) (spinner.getValue());
    }

    public void layout(DefaultFormBuilder builder, TextTranslator pTranslator) {
        JLabel label = builder.append(pTranslator.getText(getLabel()), spinner);
        label.setToolTipText(pTranslator.getText(getDescription()));
    }

    public void setEnabled(boolean pEnabled) {
        spinner.setEnabled(pEnabled);
    }

}

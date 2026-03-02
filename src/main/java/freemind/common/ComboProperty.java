package freemind.common;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ComboProperty extends PropertyBean implements PropertyControl {
    @Getter
    final String description;

    @Getter
    final String label;

    protected final JComboBox<String> mComboBox = new JComboBox<>();

    protected List<String> possibleValues;

    public ComboProperty(String description, String label, String[] possibles, TextTranslator pTranslator) {
        super();
        this.description = description;
        this.label = label;
        fillPossibleValues(possibles);
        List<String> possibleTranslations = new ArrayList<>();
        for (String key : possibleValues) {
            possibleTranslations.add(pTranslator.getText(key));
        }
        mComboBox.setModel(new DefaultComboBoxModel<>(possibleTranslations.toArray(new String[0])));
        addActionListener();
    }

    protected void addActionListener() {
        mComboBox.addActionListener(pE -> firePropertyChangeEvent());
    }

    public ComboProperty(String description, String label, String[] possibles, List<String> possibleTranslations) {
        this.description = description;
        this.label = label;
        fillPossibleValues(possibles);
        mComboBox.setModel(new DefaultComboBoxModel<>(possibleTranslations.toArray(new String[0])));
        addActionListener();
    }

    public ComboProperty(String description, String label, List<String> possibles, List<String> possibleTranslations) {
        this.description = description;
        this.label = label;
        fillPossibleValues(possibles);
        mComboBox.setModel(new DefaultComboBoxModel<>(possibleTranslations.toArray(new String[0])));
    }

    private void fillPossibleValues(String[] possibles) {
        fillPossibleValues(Arrays.asList(possibles));
    }

    private void fillPossibleValues(List<String> possibles) {
        this.possibleValues = new ArrayList<>();
        possibleValues.addAll(possibles);
    }

    /**
     * If your combo base changes, call this method to update the values. The
     * old selected value is not selected, but the first in the list. Thus, you
     * should call this method only shortly before setting the value with
     * setValue.
     */
    public void updateComboBoxEntries(List<String> possibles, List<String> possibleTranslations) {
        mComboBox.setModel(new DefaultComboBoxModel<>(possibleTranslations.toArray(new String[0])));
        fillPossibleValues(possibles);
        if (!possibles.isEmpty()) {
            mComboBox.setSelectedIndex(0);
        }
    }

    public void setValue(String value) {
        if (possibleValues.contains(value)) {
            mComboBox.setSelectedIndex(possibleValues.indexOf(value));
        } else {
            log.warn("Can't set the value: {} into the combo box {}/{}", value, getLabel(), getDescription());
            if (mComboBox.getModel().getSize() > 0) {
                mComboBox.setSelectedIndex(0);
            }
        }
    }

    public String getValue() {
        return possibleValues.get(mComboBox.getSelectedIndex());
    }

    public void layout(DefaultFormBuilder builder, TextTranslator pTranslator) {
        JLabel label = builder.append(pTranslator.getText(getLabel()), mComboBox);
        label.setToolTipText(pTranslator.getText(getDescription()));
    }

    public void setEnabled(boolean pEnabled) {
        mComboBox.setEnabled(pEnabled);
    }

}

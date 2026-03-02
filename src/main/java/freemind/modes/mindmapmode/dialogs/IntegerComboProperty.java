package freemind.modes.mindmapmode.dialogs;

import freemind.common.ComboProperty;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Adjusts string values of integers to the nearest integer as string.
 */
@Slf4j
public class IntegerComboProperty extends ComboProperty {

    public IntegerComboProperty(String pDescription,
                                String pLabel,
                                String[] pPossibles,
                                List<String> pSizesVector) {
        super(pDescription, pLabel, pPossibles, pSizesVector);
    }

    public void setValue(String pValue) {
        String lastMatchedValue = null;
        if (possibleValues.contains(pValue)) {
            super.setValue(pValue);
            return;
        } else {
            int givenVal;
            try {
                givenVal = Integer.parseInt(pValue);
                for (String stringValue : possibleValues) {
                    int val = Integer.parseInt(stringValue);
                    if (val > givenVal && lastMatchedValue != null) {
                        super.setValue(lastMatchedValue);
                        return;
                    }
                    lastMatchedValue = stringValue;
                }
            } catch (NumberFormatException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        super.setValue(pValue);
    }

}

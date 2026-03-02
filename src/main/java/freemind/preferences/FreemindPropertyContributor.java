package freemind.preferences;

import freemind.common.PropertyControl;
import freemind.common.TextTranslator;

import java.util.List;

/**
 * Implement this interface to take part in the property dialog.
 */
public interface FreemindPropertyContributor {

    List<PropertyControl> getControls(TextTranslator pTextTranslator);

}

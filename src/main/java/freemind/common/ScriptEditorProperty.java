package freemind.common;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import freemind.main.HtmlTools;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapController.MindMapControllerPlugin;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Slf4j
public class ScriptEditorProperty extends PropertyBean implements PropertyControl, ActionListener {

    public interface ScriptEditorStarter extends MindMapControllerPlugin {
        String startEditor(String scriptInput);
    }

    @Getter
    final String description;
    @Getter
    final String label;
    String script;
    final JButton mButton;

    final JPopupMenu menu = new JPopupMenu();

    private final MindMapController mMindMapController;

    public ScriptEditorProperty(String description, String label, MindMapController pMindMapController) {
        super();
        this.description = description;
        this.label = label;
        mMindMapController = pMindMapController;

        mButton = new JButton();
        mButton.addActionListener(this);
        script = "";
    }

    public void setValue(String value) {
        setScriptValue(value);
    }

    public String getValue() {
        return HtmlTools.unicodeToHTMLUnicodeEntity(HtmlTools.toXMLEscapedText(script), false);
    }

    public void layout(DefaultFormBuilder builder, TextTranslator pTranslator) {
        JLabel label = builder.append(pTranslator.getText(getLabel()), mButton);
        label.setToolTipText(pTranslator.getText(getDescription()));
    }

    public void actionPerformed(ActionEvent arg0) {
        // search for plugin that handles the script editor.
        for (MindMapControllerPlugin plugin : mMindMapController.getPlugins()) {
            if (plugin instanceof ScriptEditorStarter) {
                ScriptEditorStarter starter = (ScriptEditorStarter) plugin;
                String resultScript = starter.startEditor(script);
                if (resultScript != null) {
                    script = resultScript;
                    firePropertyChangeEvent();
                }
            }
        }
    }

    private void setScriptValue(String result) {
        if (result == null) {
            result = "";
        }
        script = HtmlTools.toXMLUnescapedText(HtmlTools.unescapeHTMLUnicodeEntity(result));
        log.trace("Setting script to {}", script);
        mButton.setText(script);
    }

    public void setEnabled(boolean pEnabled) {
        mButton.setEnabled(pEnabled);
    }

}

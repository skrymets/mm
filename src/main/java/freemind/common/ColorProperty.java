package freemind.common;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import freemind.controller.Controller;
import freemind.main.ColorUtils;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ColorProperty extends PropertyBean implements PropertyControl, ActionListener {
    @Getter
    final String description;

    @Getter
    final String label;

    Color color;

    final JButton mButton;
    final JPopupMenu menu = new JPopupMenu();

    private final String defaultColor;

    private final TextTranslator mTranslator;

    public ColorProperty(String description, String label, String defaultColor, TextTranslator pTranslator) {
        super();
        this.description = description;
        this.label = label;
        this.defaultColor = defaultColor;
        mTranslator = pTranslator;
        mButton = new JButton();
        mButton.addActionListener(this);
        color = Color.BLACK;
    }

    public void setValue(String value) {
        setColorValue(ColorUtils.xmlToColor(value));
    }

    public String getValue() {
        return ColorUtils.colorToXml(getColorValue());
    }

    public void layout(DefaultFormBuilder builder, TextTranslator pTranslator) {
        JLabel label = builder.append(pTranslator.getText(getLabel()), mButton);
        label.setToolTipText(pTranslator.getText(getDescription()));
        // add "reset to standard" popup:

        // Create and add a menu item
        JMenuItem item = new JMenuItem(mTranslator.getText("ColorProperty.ResetColor"));
        item.addActionListener(e -> setValue(defaultColor));
        menu.add(item);

        // Set the component to show the popup menu
        mButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    menu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }

            public void mouseReleased(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    menu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        });
    }

    public void actionPerformed(ActionEvent arg0) {
        Color result = Controller.showCommonJColorChooserDialog(mButton.getRootPane(), mTranslator.getText(getLabel()), getColorValue());
        if (result != null) {
            setColorValue(result);
            firePropertyChangeEvent();
        }
    }

    private void setColorValue(Color result) {
        color = result;
        if (result == null) {
            result = Color.WHITE;
        }
        mButton.setBackground(result);
        mButton.setText(ColorUtils.colorToXml(result));
    }

    private Color getColorValue() {
        return color;
    }

    public void setEnabled(boolean pEnabled) {
        mButton.setEnabled(pEnabled);
    }

}

/*
 * Created on 06.05.2005
 *
 */
package freemind.controller.filter.condition;

import freemind.modes.MindIcon;

import javax.swing.*;
import java.awt.*;

public class ConditionRenderer implements ListCellRenderer<Object> {

    final public static Color SELECTED_BACKGROUND = new Color(207, 247, 202);

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        if (value == null)
            return new JLabel(ConditionFactory.getResources().getResourceString(
                    "filter_no_filtering"));
        JComponent component;
        if (value instanceof MindIcon) {
            component = new JLabel(((MindIcon) value).getIcon());
        } else if (value instanceof Condition) {
            Condition cond = (Condition) value;
            component = cond.getListCellRendererComponent();
        } else {
            component = new JLabel(value.toString());
        }
        component.setOpaque(true);
        if (isSelected) {
            component.setBackground(SELECTED_BACKGROUND);
        } else {
            component.setBackground(Color.WHITE);
        }
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        return component;
    }

}

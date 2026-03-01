/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 *
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Created on 25.02.2006
 */
/*$Id: ComboProperty.java,v 1.1.2.5.2.2 2006/07/25 20:28:19 christianfoltin Exp $*/
package freemind.common;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import lombok.Getter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComboProperty extends PropertyBean implements PropertyControl {
    @Getter
    final String description;

    @Getter
    final String label;

    protected final JComboBox<String> mComboBox = new JComboBox<>();

    protected List<String> possibleValues;

    public ComboProperty(String description, String label, String[] possibles,
                         TextTranslator pTranslator) {
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

    public ComboProperty(String description, String label, String[] possibles,
                         List<String> possibleTranslations) {
        this.description = description;
        this.label = label;
        fillPossibleValues(possibles);
        mComboBox.setModel(new DefaultComboBoxModel<>(possibleTranslations.toArray(new String[0])));
        addActionListener();
    }

    public ComboProperty(String description, String label, List<String> possibles,
                         List<String> possibleTranslations) {
        this.description = description;
        this.label = label;
        fillPossibleValues(possibles);
        mComboBox.setModel(new DefaultComboBoxModel<>(possibleTranslations.toArray(new String[0])));
    }

    /**
     *
     */
    private void fillPossibleValues(String[] possibles) {
        fillPossibleValues(Arrays.asList(possibles));
    }

    /**
     *
     */
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
            System.err.println("Can't set the value:" + value
                    + " into the combo box " + getLabel() + "/"
                    + getDescription());
            if (mComboBox.getModel().getSize() > 0) {
                mComboBox.setSelectedIndex(0);
            }
        }
    }

    public String getValue() {
        return possibleValues.get(mComboBox.getSelectedIndex());
    }

    public void layout(DefaultFormBuilder builder, TextTranslator pTranslator) {
        JLabel label = builder.append(pTranslator.getText(getLabel()),
                mComboBox);
        label.setToolTipText(pTranslator.getText(getDescription()));
    }

    public void setEnabled(boolean pEnabled) {
        mComboBox.setEnabled(pEnabled);
    }

}

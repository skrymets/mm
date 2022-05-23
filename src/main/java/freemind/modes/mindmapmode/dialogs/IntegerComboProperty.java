/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2013 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
 */

package freemind.modes.mindmapmode.dialogs;

import freemind.common.ComboProperty;
import lombok.extern.log4j.Log4j2;

import java.util.Vector;

/**
 * Adjusts string values of integers to the nearest integer as string.
 *
 * @author foltin
 * @date 26.09.2013
 */
@Log4j2
public class IntegerComboProperty extends ComboProperty {


    public IntegerComboProperty(String pDescription,
                                String pLabel,
                                String[] pPossibles,
                                Vector<String> pSizesVector) {
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
                log.error(e);
            }
        }
        super.setValue(pValue);
    }

}

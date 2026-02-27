/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
/*
 * Created on 12.07.2005
 * Copyright (C) 2005 Dimitri Polivaev
 */
package freemind.controller.filter.condition;

import freemind.controller.Controller;
import freemind.main.FreeMindXml;
import freemind.model.MindMapNode;
import freemind.modes.attributes.Attribute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Dimitri Polivaev 12.07.2005
 */
public class AttributeCompareCondition extends CompareConditionAdapter {
    static final String COMPARATION_RESULT = "comparation_result";
    static final String ATTRIBUTE = "attribute";
    static final String NAME = "attribute_compare_condition";
    static final String SUCCEED = "succeed";
    private final String attribute;
    private final int comparationResult;
    private final boolean succeed;

    /**
     *
     */
    public AttributeCompareCondition(String attribute, String value,
                                     boolean ignoreCase, int comparationResult, boolean succeed) {
        super(value, ignoreCase);
        this.attribute = attribute;
        this.comparationResult = comparationResult;
        this.succeed = succeed;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * freemind.controller.filter.condition.Condition#checkNode(freemind.modes
     * .MindMapNode)
     */
    public boolean checkNode(Controller c, MindMapNode node) {
        for (int i = 0; i < node.getAttributeTableLength(); i++) {
            try {
                Attribute attribute2 = node.getAttribute(i);
                if (attribute2.getName().equals(attribute)
                        && succeed == (compareTo(attribute2.getValue()) == comparationResult))
                    return true;
            } catch (NumberFormatException ignored) {
            }
        }
        return false;
    }

    public void save(Document doc, Element parent) {
        Element child = doc.createElement(NAME);
        super.saveAttributes(child);
        child.setAttribute(ATTRIBUTE, attribute);
        child.setAttribute(COMPARATION_RESULT, String.valueOf(comparationResult));
        child.setAttribute(SUCCEED, String.valueOf(succeed));
        parent.appendChild(child);
    }

    static Condition load(Element element) {
        return new AttributeCompareCondition(
                FreeMindXml.getStringAttribute(element, ATTRIBUTE),
                FreeMindXml.getStringAttribute(element, AttributeCompareCondition.VALUE),
                "true".equals(FreeMindXml.getStringAttribute(element, AttributeCompareCondition.IGNORE_CASE)),
                FreeMindXml.getIntAttribute(element, COMPARATION_RESULT, 0),
                "true".equals(FreeMindXml.getStringAttribute(element, SUCCEED)));
    }

    protected String createDesctiption() {
        return super.createDescription(attribute, comparationResult, succeed);
    }
}

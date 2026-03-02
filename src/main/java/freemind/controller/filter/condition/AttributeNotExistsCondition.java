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

public class AttributeNotExistsCondition extends NodeCondition {
    static final String ATTRIBUTE = "attribute";
    static final String NAME = "attribute_not_exists_condition";
    private final String attribute;

    public AttributeNotExistsCondition(String attribute) {
        super();
        this.attribute = attribute;
    }

    public boolean checkNode(Controller c, MindMapNode node) {
        for (int i = 0; i < node.getAttributeTableLength(); i++) {
            Attribute attribute2 = node.getAttribute(i);
            if (attribute2.getName().equals(attribute))
                return true;
        }
        return false;
    }

    public void save(Document doc, Element parent) {
        Element child = doc.createElement(NAME);
        super.saveAttributes(child);
        child.setAttribute(ATTRIBUTE, attribute);
        parent.appendChild(child);
    }

    static Condition load(Element element) {
        return new AttributeNotExistsCondition(
                FreeMindXml.getStringAttribute(element, ATTRIBUTE));
    }

    protected String createDesctiption() {
        final String simpleCondition = ConditionFactory.getResources()
                .getResourceString(ConditionFactory.FILTER_DOES_NOT_EXIST);
        return ConditionFactory.createDescription(attribute, simpleCondition,
                null, false);
    }
}

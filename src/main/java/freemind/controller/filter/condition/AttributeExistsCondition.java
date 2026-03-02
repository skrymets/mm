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

public class AttributeExistsCondition extends NodeCondition {
    static final String ATTRIBUTE = "attribute";
    static final String NAME = "attribute_exists_condition";
    private final String attribute;

    public AttributeExistsCondition(String attribute) {
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
        return new AttributeExistsCondition(
                FreeMindXml.getStringAttribute(element, ATTRIBUTE));
    }

    protected String createDesctiption() {
        final String simpleCondition = ConditionFactory.getResources()
                .getResourceString(ConditionFactory.FILTER_EXIST);
        return ConditionFactory.createDescription(attribute, simpleCondition,
                null, false);
    }
}

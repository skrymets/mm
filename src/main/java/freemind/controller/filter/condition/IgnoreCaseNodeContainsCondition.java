/*
 * Created on 17.05.2005
 *
 */
package freemind.controller.filter.condition;

import freemind.controller.Controller;
import freemind.main.FreeMindXml;
import freemind.model.MindMapNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

class IgnoreCaseNodeContainsCondition extends NodeCondition {

    static final String VALUE = "value";
    static final String NAME = "ignore_case_node_contains_condition";
    private final String value;

    IgnoreCaseNodeContainsCondition(String value) {
        super();
        this.value = value.toLowerCase();
    }

    public boolean checkNode(Controller c, MindMapNode node) {
        return node.getText().toLowerCase().indexOf(value) > -1;
    }

    public void save(Document doc, Element parent) {
        Element child = doc.createElement(NAME);
        super.saveAttributes(child);
        child.setAttribute(VALUE, value);
        parent.appendChild(child);
    }

    static Condition load(Element element) {
        return new IgnoreCaseNodeContainsCondition(
                FreeMindXml.getStringAttribute(element, VALUE));
    }

    protected String createDesctiption() {
        final String nodeCondition = ConditionFactory.FILTER_NODE.getName();
        final String simpleCondition = ConditionFactory.FILTER_CONTAINS.getName();
        return ConditionFactory.createDescription(nodeCondition,
                simpleCondition, value, true);
    }
}

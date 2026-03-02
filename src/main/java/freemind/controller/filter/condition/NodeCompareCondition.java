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

class NodeCompareCondition extends CompareConditionAdapter {

    static final String COMPARATION_RESULT = "comparation_result";
    static final String VALUE = "value";
    static final String NAME = "node_compare_condition";
    static final String SUCCEED = "succeed";
    private final int comparationResult;
    private final boolean succeed;

    NodeCompareCondition(String value, boolean ignoreCase,
                         int comparationResult, boolean succeed) {
        super(value, ignoreCase);
        this.comparationResult = comparationResult;
        this.succeed = succeed;
    }

    public boolean checkNode(Controller c, MindMapNode node) {
        try {
            return succeed == (compareTo(node.getText()) == comparationResult);
        } catch (NumberFormatException fne) {
            return false;
        }
    }

    public void save(Document doc, Element parent) {
        Element child = doc.createElement(NAME);
        super.saveAttributes(child);
        child.setAttribute(COMPARATION_RESULT, String.valueOf(comparationResult));
        child.setAttribute(SUCCEED, String.valueOf(succeed));
        parent.appendChild(child);
    }

    static Condition load(Element element) {
        return new NodeCompareCondition(FreeMindXml.getStringAttribute(element, VALUE),
                "true".equals(FreeMindXml.getStringAttribute(element, NodeCompareCondition.IGNORE_CASE)),
                FreeMindXml.getIntAttribute(element, COMPARATION_RESULT, 0),
                "true".equals(FreeMindXml.getStringAttribute(element, SUCCEED)));
    }

    protected String createDesctiption() {
        final String nodeCondition = ConditionFactory.FILTER_NODE.getName();
        return super.createDescription(nodeCondition, comparationResult,
                succeed);
    }
}

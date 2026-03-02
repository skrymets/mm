/*
 * Created on 17.05.2005
 *
 */
package freemind.controller.filter.condition;

import freemind.common.NamedObject;
import org.w3c.dom.Element;

abstract class CompareConditionAdapter extends NodeCondition {

    static final String IGNORE_CASE = "ignore_case";
    static final String VALUE = "value";
    private final String conditionValue;
    private final boolean ignoreCase;

    CompareConditionAdapter(String value, boolean ignoreCase) {
        super();
        this.conditionValue = value;
        this.ignoreCase = ignoreCase;
    }

    protected int compareTo(String nodeValue) throws NumberFormatException {
        try {
            int i2 = Integer.parseInt(conditionValue);
            int i1 = Integer.parseInt(nodeValue);
            return i1 < i2 ? -1 : (i1 == i2 ? 0 : 1);
        } catch (NumberFormatException ignored) {
        }
        double d2;
        try {
            d2 = Double.parseDouble(conditionValue);
            double d1 = Double.parseDouble(nodeValue);
            return Double.compare(d1, d2);
        } catch (NumberFormatException fne) {
            return ignoreCase ? nodeValue.compareToIgnoreCase(conditionValue)
                    : nodeValue.compareTo(conditionValue);
        }
    }

    public void saveAttributes(Element child) {
        super.saveAttributes(child);
        child.setAttribute(VALUE, conditionValue);
        child.setAttribute(IGNORE_CASE, String.valueOf(ignoreCase));
    }

    public String createDescription(String attribute, int comparationResult,
                                    boolean succeed) {
        NamedObject simpleCondition;
        switch (comparationResult) {
            case -1:
                simpleCondition = succeed ? ConditionFactory.FILTER_LT
                        : ConditionFactory.FILTER_GE;
                break;
            case 0:
                simpleCondition = succeed ? ConditionFactory.FILTER_IS_EQUAL_TO
                        : ConditionFactory.FILTER_IS_NOT_EQUAL_TO;
                break;
            case 1:
                simpleCondition = succeed ? ConditionFactory.FILTER_GT
                        : ConditionFactory.FILTER_LE;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return ConditionFactory.createDescription(attribute, simpleCondition.getName(),
                conditionValue, ignoreCase);
    }

}

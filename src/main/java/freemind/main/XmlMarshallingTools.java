package freemind.main;

import freemind.common.XmlBindingTools;
import freemind.controller.actions.xml.operations.CompoundAction;
import freemind.controller.actions.xml.operations.XmlAction;
import freemind.frok.patches.JIBXGeneratedUtil;

import java.util.List;

public final class XmlMarshallingTools {

    private XmlMarshallingTools() {
    }

    public static String replaceUtf8AndIllegalXmlChars(String fileContents) {
        return HtmlTools.removeInvalidXmlCharacters(fileContents);
    }

    public static String marshall(XmlAction action) {
        return XmlBindingTools.getInstance().marshall(action);
    }

    public static XmlAction unMarshall(String inputString) {
        return XmlBindingTools.getInstance().unMarshall(inputString);
    }

    public static String printXmlAction(XmlAction pAction) {
        final String classString = pAction.getClass().getName().replaceAll(".*\\.", "");

        if (pAction instanceof CompoundAction compoundAction) {

            List<XmlAction> xmlActions = JIBXGeneratedUtil.listXmlActions(compoundAction);

            var buf = new StringBuilder("[");

            for (var xmlAction : xmlActions) {
                if (buf.length() > 1) {
                    buf.append(',');
                }
                XmlAction subAction = xmlAction;
                buf.append(printXmlAction(subAction));
            }
            buf.append(']');

            return classString + " " + buf;
        }
        return classString;
    }

    public static XmlAction deepCopy(XmlAction action) {
        return unMarshall(marshall(action));
    }
}

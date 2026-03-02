package freemind.extensions;

import freemind.main.FreeMindXml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * If a hook can't be find at map loading, this substition is used.
 * The class saves xml data such that it is preserved until the hook is back.
 */
public class PermanentNodeHookSubstituteUnknown extends
        PermanentNodeHookAdapter {

    private final String hookName;

    public PermanentNodeHookSubstituteUnknown(String name) {
        super();
        hookName = name;
    }

    private Element child;

    public void loadFrom(Element child) {
        this.child = child;
        super.loadFrom(child);
    }

    public void save(Document doc, Element xml) {
        super.save(doc, xml);
        for (Element childchild : FreeMindXml.getChildElements(child)) {
            Node imported = doc.importNode(childchild, true);
            xml.appendChild(imported);
        }
    }

    public String getName() {
        return hookName;
    }

}

package freemind.model.services;

import freemind.model.NodeAdapter;
import freemind.model.attributes.Attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Owns the per-node attributes (key/value pairs separate from the node's text).
 * Pure CRUD over a lazy List&lt;Attribute&gt;. Extracted from {@link NodeAdapter}.
 */
public class NodeAttributesService {

    private final NodeAdapter node;
    private List<Attribute> attributeVector = null; // lazy

    public NodeAttributesService(NodeAdapter node) {
        this.node = node;
    }

    public List<String> getAttributeKeyList() {
        if (attributeVector == null) {
            return Collections.emptyList();
        }
        List<String> returnValue = new ArrayList<>();
        for (Attribute attr : attributeVector) {
            returnValue.add(attr.getName());
        }
        return returnValue;
    }

    public List<Attribute> getAttributes() {
        if (attributeVector == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(attributeVector);
    }

    public int getAttributeTableLength() {
        if (attributeVector == null) {
            return 0;
        }
        return attributeVector.size();
    }

    public Attribute getAttribute(int position) {
        checkAttributePosition(position);
        return new Attribute(getAttributeVector().get(position));
    }

    public String getAttribute(String key) {
        if (attributeVector == null) {
            return null;
        }
        for (Attribute attr : attributeVector) {
            if (Objects.equals(attr.getName(), key)) {
                return attr.getValue();
            }
        }
        return null;
    }

    public int getAttributePosition(String key) {
        if (attributeVector == null) {
            return -1;
        }
        int index = 0;
        for (Attribute attr : attributeVector) {
            if (Objects.equals(attr.getName(), key)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public void setAttribute(int position, Attribute attribute) {
        checkAttributePosition(position);
        attributeVector.set(position, attribute);
    }

    public int addAttribute(Attribute attribute) {
        getAttributeVector().add(attribute);
        return getAttributeVector().indexOf(attribute);
    }

    public void insertAttribute(int position, Attribute attribute) {
        checkAttributePosition(position);
        getAttributeVector().add(position, attribute);
    }

    public void removeAttribute(int position) {
        checkAttributePosition(position);
        attributeVector.remove(position);
    }

    public void checkAttributePosition(int position) {
        if (attributeVector == null || getAttributeTableLength() <= position || position < 0) {
            throw new IllegalArgumentException("Attribute position out of range: " + position);
        }
    }

    private List<Attribute> getAttributeVector() {
        if (attributeVector == null) {
            attributeVector = new ArrayList<>();
        }
        return attributeVector;
    }
}

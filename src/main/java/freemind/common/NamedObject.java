/*
 * Created on 08.05.2005
 *
 */
package freemind.common;

import lombok.Getter;

/**
 * Utility Class for displaying local object names in GUI components.
 */
@Getter
public class NamedObject {
    private String name;
    private Object object;

    private NamedObject() {
    }

    public NamedObject(Object object, String name) {
        this.object = object;
        this.name = name;

    }

    static public NamedObject literal(String literal) {
        NamedObject result = new NamedObject();
        result.object = literal;
        result.name = literal;
        return result;
    }

    public boolean equals(Object o) {
        if (o instanceof NamedObject) {
            NamedObject ts = (NamedObject) o;
            return object.equals(ts.object);
        }
        return object.equals(o);
    }

    public String toString() {
        return name;
    }

}

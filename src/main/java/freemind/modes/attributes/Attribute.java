/*
 * Created on 18.06.2005
 * Copyright (C) 2005 Dimitri Polivaev
 */
package freemind.modes.attributes;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Attribute {
    private String name;
    private String value;

    public Attribute(String name) {
        this.name = name;
        this.value = "";
    }

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * @param pAttribute deep copy.
     */
    public Attribute(Attribute pAttribute) {
        this.name = pAttribute.name;
        this.value = pAttribute.value;
    }

    public String toString() {
        return "[" + name + ", " + value + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Attribute other = (Attribute) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}

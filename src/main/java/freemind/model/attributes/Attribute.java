package freemind.model.attributes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(of = {"name", "value"})
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

    public Attribute(Attribute pAttribute) {
        this.name = pAttribute.name;
        this.value = pAttribute.value;
    }

    public String toString() {
        return "[" + name + ", " + value + "]";
    }

}

package freemind.diagram;

import java.util.Objects;

public record ResourceRef(ResourceId resourceId) {

    public ResourceRef {
        Objects.requireNonNull(resourceId, "resourceId");
    }
}

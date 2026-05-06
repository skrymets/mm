package freemind.diagram;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Free-form key/value attributes attached to a node. Immutable. */
public record AttributeBag(Map<String, String> values) {

    public AttributeBag {
        Objects.requireNonNull(values, "values");
        values = Map.copyOf(values);
    }

    public static AttributeBag empty() {
        return new AttributeBag(Map.of());
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(values.get(key));
    }

    public AttributeBag with(String key, String value) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");
        var next = new HashMap<>(values);
        next.put(key, value);
        return new AttributeBag(next);
    }
}

package freemind.main;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Pair<T> {
    final T first;
    final T second;
}

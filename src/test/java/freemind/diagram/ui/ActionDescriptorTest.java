package freemind.diagram.ui;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class ActionDescriptorTest {

    @Test
    void minimalDescriptorBuildsCleanly() {
        var a = new ActionDescriptor(
            new ActionId("save"),
            "action.save",
            Optional.empty(),
            Optional.empty(),
            new CommandRef("file.save"));
        assertEquals("save", a.id().value());
        assertEquals("file.save", a.command().value());
    }

    @Test
    void rejectsBlankLabelKey() {
        assertThrows(IllegalArgumentException.class,
            () -> new ActionDescriptor(new ActionId("x"), "  ",
                Optional.empty(), Optional.empty(), new CommandRef("c")));
    }
}

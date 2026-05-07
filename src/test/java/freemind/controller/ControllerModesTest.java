package freemind.controller;

import freemind.diagram.mindmap.MindMapPlugin;
import freemind.diagram.plugin.DiagramPlugin;
import freemind.diagram.plugin.DiagramPluginRegistry;
import freemind.diagram.plugin.InMemoryDiagramPluginRegistry;
import freemind.main.FreeMindMain;
import freemind.main.Resources;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerModesTest {

    @Test
    void controllerExposesMindMapModeAfterRegistryBoot() {
        DiagramPluginRegistry registry = new InMemoryDiagramPluginRegistry();
        registry.register(new MindMapPlugin());

        FreeMindMain frame = Mockito.mock(FreeMindMain.class);
        Resources resources = Mockito.mock(Resources.class);

        Controller controller = new Controller(frame, resources, registry);

        assertTrue(controller.getModes().contains("MindMap"),
                "modes should include 'MindMap' from MindMapPlugin");
        assertEquals(1, controller.getModes().size(),
                "modes should have exactly one entry");
    }

    @Test
    void controllerThrowsIfRegistryIsEmpty() {
        DiagramPluginRegistry empty = new InMemoryDiagramPluginRegistry();
        FreeMindMain frame = Mockito.mock(FreeMindMain.class);
        Resources resources = Mockito.mock(Resources.class);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> new Controller(frame, resources, empty));
        assertTrue(ex.getMessage().contains("no diagram plugins"),
                "exception message should mention missing plugins, was: " + ex.getMessage());
    }
}

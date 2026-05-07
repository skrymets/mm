package freemind.main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import freemind.diagram.mindmap.MindMapDiagramImpl;
import freemind.diagram.plugin.DiagramPluginRegistry;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FreeMindModuleTest {

    @Test
    void registryIsBoundAndContainsMindMapPlugin() {
        Injector injector = Guice.createInjector(
                new FreeMindModule(new Properties(), new Properties()));

        DiagramPluginRegistry registry = injector.getInstance(DiagramPluginRegistry.class);

        assertNotNull(registry, "DiagramPluginRegistry must be bound");
        assertEquals(1, registry.all().size(),
                "registry should contain exactly one plugin (MindMapPlugin)");
        assertEquals(MindMapDiagramImpl.TYPE_ID, registry.all().get(0).typeId(),
                "the registered plugin's typeId should be MindMapDiagramImpl.TYPE_ID");
    }
}

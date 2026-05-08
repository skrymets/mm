package freemind.main;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import jakarta.inject.Singleton;

import freemind.controller.Controller;
import freemind.diagram.mindmap.MindMapPlugin;
import freemind.diagram.plugin.DiagramPluginRegistry;
import freemind.diagram.plugin.InMemoryDiagramPluginRegistry;
import freemind.events.FreeMindEventBus;
import java.util.Properties;

/**
 * Guice module for FreeMind application wiring.
 * Binds core application components for dependency injection.
 */
public class FreeMindModule extends AbstractModule {

    private final Properties defaultPreferences;
    private final Properties userPreferences;

    public FreeMindModule(Properties defaultPreferences, Properties userPreferences) {
        this.defaultPreferences = defaultPreferences;
        this.userPreferences = userPreferences;
    }

    @Override
    protected void configure() {
        // FreeMind must be a singleton: FreeMindStarter pulls one instance to call go() on,
        // and Controller @Inject also resolves FreeMindMain. Without a shared scope, Guice
        // hands Controller a second, un-initialised FreeMind whose scrollPane is null,
        // and the map-module-change observer chain NPEs on first setView.
        bind(FreeMind.class).in(Singleton.class);
        bind(FreeMindMain.class).to(FreeMind.class);
        bind(FreeMindEventBus.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    @Named("default")
    Properties provideDefaultPreferences() {
        return defaultPreferences;
    }

    @Provides
    @Singleton
    @Named("user")
    Properties provideUserPreferences() {
        return userPreferences;
    }

    @Provides
    @Singleton
    Resources provideResources(FreeMindMain frame) {
        // Constructor sets the static resourcesInstance field for backward compatibility
        return new Resources(frame);
    }

    @Provides
    @Singleton
    Controller provideController(FreeMindMain frame, Resources resources,
                                 DiagramPluginRegistry registry) {
        return new Controller(frame, resources, registry);
    }

    @Provides
    @Singleton
    DiagramPluginRegistry provideDiagramPluginRegistry(MindMapPlugin mindMapPlugin) {
        var registry = new InMemoryDiagramPluginRegistry();
        registry.register(mindMapPlugin);
        return registry;
    }
}

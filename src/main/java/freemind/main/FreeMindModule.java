package freemind.main;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import jakarta.inject.Singleton;
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
        bind(FreeMindMain.class).to(FreeMind.class);
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
        // Resources singleton is created in FreeMind constructor via Resources.createInstance()
        // Return the existing singleton for Guice-managed injection
        return Resources.getInstance();
    }
}

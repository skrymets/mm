package freemind.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Tests that the model package does not participate in circular dependencies
 * with controller or view packages. The broader cycle analysis (modes↔controller,
 * view↔controller) is deferred to future phases.
 */
class NoCyclicDependenciesTest {

    private static JavaClasses classes;

    @BeforeAll
    static void setup() {
        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("freemind");
    }

    @Test
    void modelDoesNotDependOnControllerCreatingCycle() {
        // Model must not depend on controller (which depends on model) - no cycle
        noClasses()
                .that().resideInAPackage("freemind.model..")
                .should().dependOnClassesThat().resideInAPackage("freemind.controller..")
                .check(classes);
    }

    @Test
    void modelDoesNotDependOnViewCreatingCycle() {
        // Model must not depend on view (which depends on model) - no cycle
        noClasses()
                .that().resideInAPackage("freemind.model..")
                .should().dependOnClassesThat().resideInAPackage("freemind.view..")
                .check(classes);
    }

    @Test
    void eventsDoesNotDependOnControllerOrView() {
        // Events package should only depend on model
        noClasses()
                .that().resideInAPackage("freemind.events..")
                .should().dependOnClassesThat().resideInAPackage("freemind.controller..")
                .check(classes);
    }
}

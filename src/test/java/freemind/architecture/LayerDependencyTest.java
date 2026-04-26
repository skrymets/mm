package freemind.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class LayerDependencyTest {

    private static JavaClasses classes;

    @BeforeAll
    static void setup() {
        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("freemind");
    }

    @Test
    void modelShouldNotDependOnController() {
        noClasses()
                .that().resideInAPackage("freemind.model..")
                .should().dependOnClassesThat().resideInAPackage("freemind.controller..")
                .check(classes);
    }

    @Test
    void modelShouldNotDependOnView() {
        noClasses()
                .that().resideInAPackage("freemind.model..")
                .should().dependOnClassesThat().resideInAPackage("freemind.view..")
                .check(classes);
    }

    @Test
    void modelShouldNotDependOnModes() {
        noClasses()
                .that().resideInAPackage("freemind.model..")
                .should().dependOnClassesThat().resideInAPackage("freemind.modes..")
                .check(classes);
    }
}

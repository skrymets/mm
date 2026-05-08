package freemind.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Architectural constraints on the new {@code freemind.diagram.*} packages
 * introduced by Plan 1 of the diagram-abstraction foundation work.
 */
class DiagramArchitectureTest {

    private static JavaClasses classes;

    @BeforeAll
    static void importClasses() {
        classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("freemind");
    }

    @Test
    void diagramCorePackagesMustNotImportSwingOrAwt() {
        // L1 (Plan 2b-modeless): freemind.diagram.mindmap is the named
        // integration unit and may transitively import Swing via
        // MindMapController. Other diagram packages stay strict.
        noClasses()
            .that().resideInAnyPackage(
                "freemind.diagram",
                "freemind.diagram.topology..",
                "freemind.diagram.capabilities..",
                "freemind.diagram.plugin..",
                "freemind.diagram.persistence..",
                "freemind.diagram.ui..",
                "freemind.diagram.style..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("javax.swing..", "java.awt..")
            .check(classes);
    }

    @Test
    void onlyFreemindDiagramSwingMayImportSwingTreePackage() {
        noClasses()
            .that().resideInAPackage("freemind.diagram..")
            .and().resideOutsideOfPackage("freemind.diagram.swing..")
            .should().dependOnClassesThat().resideInAPackage("javax.swing.tree..")
            .check(classes);
    }

    @Test
    void diagramModelMustNotDependOnControllerOrView() {
        noClasses()
            .that().resideInAnyPackage(
                "freemind.diagram",
                "freemind.diagram.topology..",
                "freemind.diagram.capabilities..",
                "freemind.diagram.style..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("freemind.controller..", "freemind.view..")
            .check(classes);
    }

    @Test
    void diagramPluginContractMustNotDependOnSwingOrAwt() {
        noClasses()
            .that().resideInAPackage("freemind.diagram.plugin..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("javax.swing..", "java.awt..")
            .check(classes);
    }

    @Test
    void diagramUiPackageMustNotDependOnSwingOrAwt() {
        noClasses()
            .that().resideInAPackage("freemind.diagram.ui..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("javax.swing..", "java.awt..")
            .check(classes);
    }
}

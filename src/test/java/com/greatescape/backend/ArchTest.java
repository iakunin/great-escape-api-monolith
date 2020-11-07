package com.greatescape.backend;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.greatescape.backend");

        noClasses()
            .that()
                .resideInAnyPackage("com.greatescape.backend.service..")
            .or()
                .resideInAnyPackage("com.greatescape.backend.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.greatescape.backend.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}

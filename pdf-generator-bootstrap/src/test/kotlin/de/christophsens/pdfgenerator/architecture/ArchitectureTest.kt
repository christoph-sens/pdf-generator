package de.christophsens.pdfgenerator.architecture

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

@AnalyzeClasses(
    packages = ["de.christophsens.pdfgenerator"],
    importOptions = [ImportOption.DoNotIncludeTests::class]
)
class ArchitectureTest {

    @ArchTest
    val coreIsFrameworkFree: ArchRule = noClasses()
        .that().resideInAPackage("..pdfgenerator.domain..")
        .should().dependOnClassesThat().resideInAnyPackage("org.springframework..", "jakarta..")

    @ArchTest
    val coreDoesNotDependOnAdapters: ArchRule = noClasses()
        .that().resideInAPackage("..pdfgenerator.domain..")
        .should().dependOnClassesThat().resideInAPackage("..pdfgenerator.adapter..")
}

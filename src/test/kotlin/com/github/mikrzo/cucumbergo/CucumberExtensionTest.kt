package com.github.mikrzo.cucumbergo

import com.goide.GoCodeInsightFixtureTestCase
import com.goide.psi.GoFile
import org.jetbrains.plugins.cucumber.psi.GherkinFile

class CucumberExtensionTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData"

    private val extension = CucumberExtension()

    fun testLoadStepsForModuleWithDefinitions() {
        myFixture.copyDirectoryToProject("highlighting/stepParameter", "")
        val steps = extension.loadStepsFor(null, module)
        assertFalse("Expected steps to be found in module with step definitions", steps.isEmpty())
    }

    fun testLoadStepsForEmptyModule() {
        val steps = extension.loadStepsFor(null, module)
        assertTrue("Expected empty list for module with no step definitions", steps.isEmpty())
    }

    fun testIsStepLikeFileGoFile() {
        val goFile = myFixture.configureByText("step_test.go", "package test\n")
        assertTrue(extension.isStepLikeFile(goFile))
    }

    fun testIsStepLikeFileNonGoFile() {
        val featureFile = myFixture.configureByText("test.feature", "Feature: test\n")
        assertFalse(extension.isStepLikeFile(featureFile))
    }

    fun testIsWritableStepLikeFileGoFile() {
        val goFile = myFixture.configureByText("step_test.go", "package test\n")
        assertTrue(extension.isWritableStepLikeFile(goFile))
    }

    fun testGetStepDefinitionContainers() {
        myFixture.copyDirectoryToProject("highlighting/stepParameter", "")
        myFixture.configureByFile("highlighting/stepParameter/test.feature")
        val containers = extension.getStepDefinitionContainers(myFixture.file as GherkinFile)
        assertFalse("Expected step definition containers to be found", containers.isEmpty())
        assertTrue("Container should be a Go file", containers.first() is GoFile)
    }
}

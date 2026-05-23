package com.github.mikrzo.cucumbergo.godog

import com.goide.GoCodeInsightFixtureTestCase

class GodogFrameworkTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData"

    fun testUsesGodogWhenImportPresent() {
        myFixture.configureByText(
            "step_test.go",
            "package steptest\n\nimport \"github.com/cucumber/godog\"\n\nvar _ = godog.TestSuite{}"
        )
        assertTrue(GodogFramework.INSTANCE.isAvailableOnFile(myFixture.file))
    }

    fun testNotGodogWhenImportAbsent() {
        myFixture.configureByText(
            "plain_test.go",
            "package steptest\n\nimport \"fmt\"\n\nvar _ = fmt.Sprintf(\"\")"
        )
        assertFalse(GodogFramework.INSTANCE.isAvailableOnFile(myFixture.file))
    }

    fun testNotGodogWhenNotGoFile() {
        myFixture.configureByText(
            "test.feature",
            "Feature: x\n  Scenario: y\n    Given z\n"
        )
        assertFalse(GodogFramework.INSTANCE.isAvailableOnFile(myFixture.file))
    }

    fun testPackageConfigurationNameIsGodog() {
        assertEquals("Godog", GodogFramework.INSTANCE.getPackageConfigurationName("anything"))
        assertEquals("Godog", GodogFramework.INSTANCE.getPackageConfigurationName(""))
    }
}

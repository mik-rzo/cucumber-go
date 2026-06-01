package com.github.mikrzo.cucumbergo.inspection

import com.goide.GoCodeInsightFixtureTestCase
import org.jetbrains.plugins.cucumber.inspections.CucumberStepInspection

class CucumberGoStepInspectionTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData/inspection"

    private fun doTest() {
        myFixture.enableInspections(CucumberStepInspection())
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        myFixture.testHighlighting(true, true, true)
    }

    fun testResolvedStep() = doTest()

    fun testUnresolvedStep() = doTest()

    fun testStepParameter() = doTest()

    fun testScenarioParameter() = doTest()

    fun testStepWithDocstring() = doTest()

    fun testOrGroup() = doTest()

    fun testAmbiguousStep() = doTest()
}

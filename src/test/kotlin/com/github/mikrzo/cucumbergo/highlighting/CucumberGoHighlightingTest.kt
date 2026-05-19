package com.github.mikrzo.cucumbergo.highlighting

import com.goide.GoCodeInsightFixtureTestCase
import org.jetbrains.plugins.cucumber.inspections.CucumberStepInspection

class CucumberGoHighlightingTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData/highlighting"

    private fun doTest() {
        myFixture.enableInspections(CucumberStepInspection())
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        myFixture.testHighlighting(true, true, true)
    }

    fun testResolvedStep() = doTest()

    fun testUnresolvedStep() = doTest()
}
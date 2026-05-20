package com.github.mikrzo.cucumbergo.resolve

import com.github.mikrzo.cucumbergo.steps.StepDefinition
import com.goide.GoCodeInsightFixtureTestCase
import com.goide.psi.GoCallExpr

class CucumberGoResolveTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData/resolve"

    fun testStepResolveBacktickPattern() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        val ref = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("No reference at caret position in step text", ref)
        val resolved = ref!!.resolve()
        assertNotNull("Step did not resolve to a definition", resolved)
        val regex = StepDefinition(resolved as GoCallExpr).getCucumberRegex()
        assertEquals("""^there's a step definition for this step$""", regex)
    }
}
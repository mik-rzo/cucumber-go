package com.github.mikrzo.cucumbergo.completion

import com.goide.GoCodeInsightFixtureTestCase

class CucumberGoCompletionTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData/completion"

    fun testStepCompletion() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "_before.feature")
        myFixture.completeBasic()
        myFixture.checkResultByFile(getTestName(true) + "_after.feature")
    }

    fun testStepCompletionNoMatch() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + ".feature")
        val result = myFixture.completeBasic()
        assertTrue("Expected no completion items", result == null || result.isEmpty())
    }

    fun testStepCompletionMultipleMatches() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "_before.feature")
        val result = myFixture.completeBasic()
        assertNotNull("Expected completion items", result)
        assertTrue("Expected at least 2 completion items", result!!.size >= 2)
        myFixture.lookup.currentItem =
            result.first { it.lookupString.contains("perform an action") }
        myFixture.type('\n')
        myFixture.checkResultByFile(getTestName(true) + "_after.feature")
    }
}

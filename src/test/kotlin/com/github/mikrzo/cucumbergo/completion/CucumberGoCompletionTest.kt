package com.github.mikrzo.cucumbergo.completion

import com.goide.GoCodeInsightFixtureTestCase

class CucumberGoCompletionTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData/completion"

    fun testStepCompletion() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "_before.feature")
        val result = myFixture.completeBasic()
        assertNull("Expected single match to auto-insert (null lookup result)", result)
        myFixture.checkResultByFile(getTestName(true) + "_after.feature")
    }

    fun testStepCompletionNoMatch() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + ".feature")
        val result = myFixture.completeBasic()
        val stepLookupStrings = result?.map { it.lookupString }.orEmpty()
        assertTrue(
            "Expected no completion items, got $stepLookupStrings",
            stepLookupStrings.isEmpty(),
        )
    }

    fun testStepCompletionMultipleMatches() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "_before.feature")
        val result = myFixture.completeBasic()
        assertNotNull("Expected completion items", result)
        val lookupStrings = result!!.map { it.lookupString }
        assertEquals("Expected exactly 2 completion items, got $lookupStrings", 2, result.size)
        assertTrue(
            "Expected lookup for 'I perform an action', got $lookupStrings",
            lookupStrings.contains("I perform an action"),
        )
        assertTrue(
            "Expected lookup for 'I perform another action', got $lookupStrings",
            lookupStrings.contains("I perform another action"),
        )
        myFixture.lookup.currentItem =
            result.first { it.lookupString == "I perform an action" }
        myFixture.type('\n')
        myFixture.checkResultByFile(getTestName(true) + "_after.feature")
    }
}

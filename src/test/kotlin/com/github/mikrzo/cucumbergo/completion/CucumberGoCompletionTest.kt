package com.github.mikrzo.cucumbergo.completion

import com.goide.GoCodeInsightFixtureTestCase

class CucumberGoCompletionTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData/completion"

    fun testStepCompletion() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/" + getTestName(true) + "_before.feature")
        val result = myFixture.completeBasic()
        assertNull("Expected single match to auto-insert (null lookup result)", result)
        myFixture.checkResultByFile(getTestName(true) + "/" + getTestName(true) + "_after.feature")
    }

    fun testStepCompletionNoMatch() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/" + getTestName(true) + ".feature")
        val result = myFixture.completeBasic()
        val stepLookupStrings = result?.map { it.lookupString }.orEmpty()
        assertTrue(
            "Expected no completion items, got $stepLookupStrings",
            stepLookupStrings.isEmpty(),
        )
    }

    fun testStepCompletionRegexCapture() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/" + getTestName(true) + ".feature")
        val result = myFixture.completeBasic()
        val lookupStrings = result?.map { it.lookupString }.orEmpty()
        assertNotNull("Expected completion items", result)
        assertEquals(
            "Expected exactly 1 completion item for parametrized step, got $lookupStrings",
            1,
            result!!.size,
        )
        val lookupString = lookupStrings.single()
        assertTrue(
            "Expected lookup to start with literal step prefix, got '$lookupString'",
            lookupString.startsWith("I perform"),
        )
        assertTrue(
            "Expected lookup to retain literal step suffix, got '$lookupString'",
            lookupString.endsWith("actions"),
        )
    }

    fun testStepCompletionMultipleMatches() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/" + getTestName(true) + "_before.feature")
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
        myFixture.checkResultByFile(getTestName(true) + "/" + getTestName(true) + "_after.feature")
    }
}

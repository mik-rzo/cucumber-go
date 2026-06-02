package com.github.mikrzo.cucumbergo.completion

import com.goide.GoCodeInsightFixtureTestCase

class CucumberGoCompletionTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData/completion"

    fun testStep() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/" + getTestName(true) + "_before.feature")
        val result = myFixture.completeBasic()
        assertNull("Expected single match to auto-insert (null lookup result)", result)
        myFixture.checkResultByFile(getTestName(true) + "/" + getTestName(true) + "_after.feature")
    }

    fun testStepNoMatch() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/" + getTestName(true) + ".feature")
        // A no-match completion returns an empty array, never null (only a single auto-inserted
        // match yields null), so assert that contract explicitly.
        val result = myFixture.completeBasic()
        assertNotNull("Expected an empty (non-null) result for no match", result)
        assertEmpty("Expected no completion items", result!!.map { it.lookupString })
    }

    fun testStepRegexCapture() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/" + getTestName(true) + "_before.feature")
        val result = myFixture.completeBasic()
        assertNull("Expected single match to auto-insert (null lookup result)", result)
        myFixture.checkResultByFile(getTestName(true) + "/" + getTestName(true) + "_after.feature")
    }

    fun testStepMidWord() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/" + getTestName(true) + "_before.feature")
        // A mid-word match isn't auto-inserted (it would overwrite trailing text), so unlike a
        // single end-of-line match the lookup stays open and completeBasic returns it (non-null).
        val result = myFixture.completeBasic()
        assertNotNull("Expected a one-item lookup; a mid-word match is not auto-inserted", result)
        val lookupStrings = result!!.map { it.lookupString }
        assertEquals("Expected exactly 1 completion item, got $lookupStrings", 1, result.size)
        myFixture.lookup.currentItem = result[0]
        myFixture.type('\n')
        myFixture.checkResultByFile(getTestName(true) + "/" + getTestName(true) + "_after.feature")
    }

    fun testStepMidWordRegexCapture() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/" + getTestName(true) + "_before.feature")
        // A mid-word match isn't auto-inserted (it would overwrite trailing text), so unlike a
        // single end-of-line match the lookup stays open and completeBasic returns it (non-null).
        val result = myFixture.completeBasic()
        assertNotNull("Expected a one-item lookup; a mid-word match is not auto-inserted", result)
        val lookupStrings = result!!.map { it.lookupString }
        assertEquals("Expected exactly 1 completion item, got $lookupStrings", 1, result.size)
        myFixture.lookup.currentItem = result[0]
        myFixture.type('\n')
        myFixture.checkResultByFile(getTestName(true) + "/" + getTestName(true) + "_after.feature")
    }

    fun testStepMidWordReplacesUnmatchedTail() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/" + getTestName(true) + "_before.feature")
        // A mid-word match isn't auto-inserted (it would overwrite trailing text), so unlike a
        // single end-of-line match the lookup stays open and completeBasic returns it (non-null).
        val result = myFixture.completeBasic()
        assertNotNull("Expected a one-item lookup; a mid-word match is not auto-inserted", result)
        val lookupStrings = result!!.map { it.lookupString }
        assertEquals("Expected exactly 1 completion item, got $lookupStrings", 1, result.size)
        myFixture.lookup.currentItem = result[0]
        myFixture.type('\n')
        myFixture.checkResultByFile(getTestName(true) + "/" + getTestName(true) + "_after.feature")
    }

    fun testStepMultipleMatches() {
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

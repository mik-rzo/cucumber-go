package com.github.mikrzo.cucumbergo.rename

import com.goide.GoCodeInsightFixtureTestCase
import com.intellij.refactoring.util.CommonRefactoringUtil

class CucumberGoStepRenameTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData/rename"

    private fun doTest(newName: String) {
        val name = getTestName(true)
        myFixture.copyDirectoryToProject("$name/before", "")
        myFixture.configureByFile("test.feature")
        myFixture.renameElementAtCaretUsingHandler(newName)
        myFixture.checkResultByFile("test.feature", "$name/after/test.feature", false)
        myFixture.checkResultByFile("step_test.go", "$name/after/step_test.go", false)
    }

    fun testQuoteLiteral() = doTest("I possess the amount of (-?\\d+) USD on my acc")

    fun testPreservesNonAscii() = doTest("teraz jestem bardzo głodny")

    fun testLeavesOtherStepDefsUntouched() = doTest("Me be satisfied")

    fun testAnchoredQuote() = doTest("I am not happy at all")

    fun testAnchoredQuoteWithCaptureGroup() = doTest("I spend (-?\\d+) USD")

    fun testFailsWithNoDefinition() {
        val name = getTestName(true)
        myFixture.copyDirectoryToProject("$name/before", "")
        myFixture.configureByFile("test.feature")
        try {
            myFixture.renameElementAtCaretUsingHandler("whatever")
            fail("Expected IncorrectOperationException")
        } catch (_: CommonRefactoringUtil.RefactoringErrorHintException) {
            // expected — in unit test mode showErrorHint throws instead of showing a balloon
        }
    }

    fun testBacktickLiteral() = doTest("Me be satisfied")

    fun testRegexpMustCompileLiteral() = doTest("there is now a different must compile step")
}

package com.github.mikrzo.cucumbergo.resolve

import com.github.mikrzo.cucumbergo.steps.StepDefinition
import com.goide.GoCodeInsightFixtureTestCase
import com.goide.psi.GoCallExpr
import com.goide.psi.GoFile

class CucumberGoResolveTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData/resolve"

    fun testStepResolveQuotePattern() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        val ref = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("No reference at caret position in step text", ref)
        val resolved = ref!!.resolve()
        assertNotNull("Step did not resolve to a definition", resolved)
        val resolvedCall = resolved as GoCallExpr
        val regex = StepDefinition(resolvedCall).getCucumberRegex()
        assertEquals("""^there's a step definition for this step$""", regex)
        assertEquals(
            "Resolved to the wrong ctx.Step call",
            "theresAStepDefinition",
            resolvedCall.argumentList.expressionList.getOrNull(1)?.text,
        )
    }

    fun testStepResolveBacktickPattern() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        val ref = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("No reference at caret position in step text", ref)
        val resolved = ref!!.resolve()
        assertNotNull("Step did not resolve to a definition", resolved)
        val resolvedCall = resolved as GoCallExpr
        val regex = StepDefinition(resolvedCall).getCucumberRegex()
        assertEquals("""^there's a step definition for this step$""", regex)
        assertEquals(
            "Resolved to the wrong ctx.Step call",
            "theresAStepDefinition",
            resolvedCall.argumentList.expressionList.getOrNull(1)?.text,
        )
    }

    fun testStepResolvePackageIsolation() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile("featurepkg/features/test.feature")
        val ref = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("No reference at caret position in step text", ref)
        val resolved = ref!!.resolve()
        assertNotNull("Step did not resolve to a definition", resolved)
        val resolvedCall = resolved as GoCallExpr
        assertEquals(
            "Resolved to wrong package's step definition",
            "featurepkg",
            (resolvedCall.containingFile as GoFile).packageName,
        )
    }

    fun testStepResolveRegexpPattern() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        val ref = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("No reference at caret position in step text", ref)
        val resolved = ref!!.resolve()
        assertNotNull("Step did not resolve to a definition", resolved)
        val resolvedCall = resolved as GoCallExpr
        val regex = StepDefinition(resolvedCall).getCucumberRegex()
        assertEquals("""^there is a regexp step$""", regex)
        assertEquals(
            "Resolved to the wrong ctx.Step call",
            "thereIsARegexpStep",
            resolvedCall.argumentList.expressionList.getOrNull(1)?.text,
        )
    }

    fun testStepResolveRegexpBacktickEscape() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        val ref = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("No reference at caret position in step text", ref)
        val resolved = ref!!.resolve()
        assertNotNull("Step did not resolve to a definition", resolved)
        val resolvedCall = resolved as GoCallExpr
        val regex = StepDefinition(resolvedCall).getCucumberRegex()
        assertEquals("""^\\d$""", regex)
        assertEquals(
            "Resolved to the wrong ctx.Step call",
            "backslashThenD",
            resolvedCall.argumentList.expressionList.getOrNull(1)?.text,
        )
    }

    fun testStepResolveRegexpQuotedEscape() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        val ref = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("No reference at caret position in step text", ref)
        val resolved = ref!!.resolve()
        assertNotNull("Step did not resolve to a definition", resolved)
        val resolvedCall = resolved as GoCallExpr
        val regex = StepDefinition(resolvedCall).getCucumberRegex()
        assertEquals("""^\d$""", regex)
        assertEquals(
            "Resolved to the wrong ctx.Step call",
            "aDigit",
            resolvedCall.argumentList.expressionList.getOrNull(1)?.text,
        )
    }

    fun testStepResolveNoMatch() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        val ref = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("No reference at caret position in step text", ref)
        assertNull("Step with no matching definition should resolve to null", ref!!.resolve())
    }
}
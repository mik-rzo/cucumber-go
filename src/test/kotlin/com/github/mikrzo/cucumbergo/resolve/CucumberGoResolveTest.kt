package com.github.mikrzo.cucumbergo.resolve

import com.github.mikrzo.cucumbergo.steps.StepDefinition
import com.goide.GoCodeInsightFixtureTestCase
import com.goide.psi.GoCallExpr
import com.goide.psi.GoFile

class CucumberGoResolveTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData/resolve"

    fun testQuotePattern() {
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

    fun testBacktickPattern() {
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

    fun testPackageIsolation() {
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

    fun testRegexpPattern() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        val carets = myFixture.editor.caretModel.allCarets

        val ref = myFixture.file.findReferenceAt(carets[0].offset)
        assertNotNull("No reference at regexp.MustCompile step", ref)
        val resolved = ref!!.resolve()
        assertNotNull("regexp.MustCompile step did not resolve to a definition", resolved)
        val resolvedCall = resolved as GoCallExpr
        val regex = StepDefinition(resolvedCall).getCucumberRegex()
        assertEquals("""^there is a regexp must compile step$""", regex)
        assertEquals(
            "Resolved to the wrong ctx.Step call",
            "thereIsARegexpMustCompileStep",
            resolvedCall.argumentList.expressionList.getOrNull(1)?.text,
        )

        // regexp.Compile (unlike MustCompile) isn't a valid ctx.Step argument, so it must not be recognised as a step.
        val ref2 = myFixture.file.findReferenceAt(carets[1].offset)
        val resolved2 = ref2?.resolve()
        assertNull("regexp.Compile is not valid Go for ctx.Step and must not resolve", resolved2)
    }

    fun testRegexpBacktickNoEscape() {
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

    fun testRegexpQuotedEscape() {
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

    fun testNoMatch() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        val ref = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("No reference at caret position in step text", ref)
        assertNull("Step with no matching definition should resolve to null", ref!!.resolve())
    }
}
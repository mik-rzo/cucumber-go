package com.github.mikrzo.cucumbergo.steps

import com.goide.GoCodeInsightFixtureTestCase
import com.goide.psi.GoCallExpr
import com.goide.psi.GoFile
import com.intellij.psi.util.PsiTreeUtil

class StepDefinitionTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData"

    private val bt = '`'

    private fun configureBacktickStep(patternContent: String): GoCallExpr {
        val source = "package test\n\nimport \"github.com/cucumber/godog\"\n\nfunc Init(ctx *godog.ScenarioContext) {\n    ctx.Step(" + bt + patternContent + bt + ", nil)\n}"
        val file = myFixture.configureByText("step_test.go", source) as GoFile
        return PsiTreeUtil.findChildrenOfType(file, GoCallExpr::class.java).first()
    }

    private fun configureQuotedStep(patternContent: String): GoCallExpr {
        val source = "package test\n\nimport \"github.com/cucumber/godog\"\n\nfunc Init(ctx *godog.ScenarioContext) {\n    ctx.Step(\"" + patternContent + "\", nil)\n}"
        val file = myFixture.configureByText("step_test.go", source) as GoFile
        return PsiTreeUtil.findChildrenOfType(file, GoCallExpr::class.java).first()
    }

    fun testBacktickRegexPattern() {
        val callExpr = configureBacktickStep("^the response code should be (\\d+)\$")
        assertEquals("^the response code should be (\\d+)\$", StepDefinition(callExpr).getCucumberRegex())
    }

    fun testDoubleQuoteRegexPattern() {
        val callExpr = configureQuotedStep("^foo bar\$")
        assertEquals("^foo bar\$", StepDefinition(callExpr).getCucumberRegex())
    }

    fun testCucumberExpressionPattern() {
        val callExpr = configureBacktickStep("the response code should be {int}")
        val regex = StepDefinition(callExpr).getCucumberRegex()
        assertNotNull("Expected non-null regex for cucumber expression", regex)
        assertTrue("Expected regex to start with ^", regex!!.startsWith("^"))
    }

    fun testDoubleSlashCollapsed() {
        // Go backtick string `^foo\\bar$` has two literal backslashes; getStepDefinitionText collapses to one
        val callExpr = configureBacktickStep("^foo\\\\bar\$")
        assertEquals("^foo\\bar\$", StepDefinition(callExpr).getCucumberRegex())
    }
}

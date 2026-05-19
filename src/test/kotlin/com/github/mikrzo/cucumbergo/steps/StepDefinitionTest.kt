package com.github.mikrzo.cucumbergo.steps

import com.goide.GoCodeInsightFixtureTestCase
import com.goide.psi.GoCallExpr
import com.goide.psi.GoFile
import com.intellij.psi.util.PsiTreeUtil

class StepDefinitionTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData"

    private val bt = '`'

    private fun configureBacktickStep(patternContent: String): GoCallExpr {
        val source =
            "package test\n\nimport \"github.com/cucumber/godog\"\n\nfunc Init(ctx *godog.ScenarioContext) {\n    ctx.Step($bt$patternContent$bt, nil)\n}"
        val file = myFixture.configureByText("step_test.go", source) as GoFile
        return PsiTreeUtil.findChildrenOfType(file, GoCallExpr::class.java).first()
    }

    private fun configureQuotedStep(patternContent: String): GoCallExpr {
        val source =
            "package test\n\nimport \"github.com/cucumber/godog\"\n\nfunc Init(ctx *godog.ScenarioContext) {\n    ctx.Step(\"$patternContent\", nil)\n}"
        val file = myFixture.configureByText("step_test.go", source) as GoFile
        return PsiTreeUtil.findChildrenOfType(file, GoCallExpr::class.java).first()
    }

    fun testBacktickRegexPattern() {
        val callExpr = configureBacktickStep("^the response code should be (\\d+)\$")
        assertEquals("^the response code should be (\\d+)\$", StepDefinition(callExpr).cucumberRegex)
    }

    fun testDoubleQuoteRegexPattern() {
        val callExpr = configureQuotedStep("^foo bar\$")
        assertEquals("^foo bar\$", StepDefinition(callExpr).cucumberRegex)
    }

    fun testCucumberExpressionPattern() {
        val callExpr = configureBacktickStep("the response code should be {int}")
        val regex = StepDefinition(callExpr).cucumberRegex
        assertNotNull("Expected non-null regex for cucumber expression", regex)
        assertFalse("Expected {int} placeholder to be expanded into a regex group", regex!!.contains("{int}"))
        assertTrue("Expected expanded regex to contain a digit class", regex.contains("\\d"))
    }

    fun testDoubleSlashCollapsed() {
        // Go backtick string `^foo\\bar$` has two literal backslashes; getStepDefinitionText collapses to one
        val callExpr = configureBacktickStep("^foo\\\\bar\$")
        assertEquals("^foo\\bar\$", StepDefinition(callExpr).cucumberRegex)
    }
}

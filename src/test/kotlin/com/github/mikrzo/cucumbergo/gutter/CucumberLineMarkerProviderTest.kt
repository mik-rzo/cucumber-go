package com.github.mikrzo.cucumbergo.gutter

import com.goide.GoCodeInsightFixtureTestCase
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerImpl
import icons.CucumberIcons

class CucumberLineMarkerProviderTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData"

    private val bt = '`'

    private fun configureGoFile(callExpr: String) {
        myFixture.configureByText(
            "step_test.go",
            "package steptest\n\nimport \"github.com/cucumber/godog\"\n\nfunc stub() error { return nil }\n\nfunc InitializeScenario(ctx *godog.ScenarioContext) {\n    $callExpr\n}"
        )
    }

    private fun lineMarkers(): List<LineMarkerInfo<*>> {
        myFixture.doHighlighting()
        return DaemonCodeAnalyzerImpl.getLineMarkers(myFixture.editor.document, project)
    }

    private fun assertOneCucumberMarker() {
        val cucumberMarkers = lineMarkers().filter { it.icon === CucumberIcons.Cucumber }
        assertEquals("Expected one Cucumber gutter icon", 1, cucumberMarkers.size)
    }

    private fun assertNoCucumberMarker() {
        val cucumberMarkers = lineMarkers().filter { it.icon === CucumberIcons.Cucumber }
        assertTrue("Expected no Cucumber gutter icon, found: $cucumberMarkers", cucumberMarkers.isEmpty())
    }

    fun testGivenStepHasMarker() {
        configureGoFile("ctx.Given(${bt}a step is registered${bt}, stub)")
        assertOneCucumberMarker()
    }

    fun testWhenStepHasMarker() {
        configureGoFile("ctx.When(${bt}an action is performed${bt}, stub)")
        assertOneCucumberMarker()
    }

    fun testThenStepHasMarker() {
        configureGoFile("ctx.Then(${bt}the result is verified${bt}, stub)")
        assertOneCucumberMarker()
    }

    fun testStepStepHasMarker() {
        configureGoFile("ctx.Step(${bt}a generic step definition${bt}, stub)")
        assertOneCucumberMarker()
    }

    fun testUnknownKeywordNoMarker() {
        configureGoFile("ctx.Execute(${bt}not a step keyword${bt}, stub)")
        assertNoCucumberMarker()
    }
}

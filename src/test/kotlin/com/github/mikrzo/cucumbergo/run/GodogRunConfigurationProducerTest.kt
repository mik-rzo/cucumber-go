package com.github.mikrzo.cucumbergo.run

import com.github.mikrzo.cucumbergo.godog.GodogFramework
import com.goide.GoCodeInsightFixtureTestCase
import com.goide.execution.testing.GoTestRunConfiguration
import com.goide.execution.testing.GoTestRunConfigurationType
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement

class GodogRunConfigurationProducerTest : GoCodeInsightFixtureTestCase() {

    private class TestableProducer : GodogRunConfigurationProducer()

    override fun getTestDataPath() = "src/test/testData/run"

    private fun createTemplateConfiguration(): GoTestRunConfiguration {
        val factory = GoTestRunConfigurationType.getInstance().configurationFactories[0]
        return GoTestRunConfiguration("", myFixture.project, factory)
    }

    private fun getConfigurationContext(): ConfigurationContext {
        val dataContext = DataManager.getInstance().getDataContext(myFixture.editor.component)
        return ConfigurationContext.getFromContext(dataContext, ActionPlaces.UNKNOWN)
    }

    private fun elementAtCaret(): PsiElement =
        myFixture.file.findElementAt(myFixture.caretOffset)!!

    private fun runProducer(sourceElement: PsiElement): Pair<Boolean, GoTestRunConfiguration> {
        val config = createTemplateConfiguration()
        val accepted = TestableProducer().setupConfigurationFromContext(
            config,
            getConfigurationContext(),
            Ref<Any>(sourceElement)
        )
        return accepted to config
    }

    fun testScenarioRun() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        val (accepted, config) = runProducer(elementAtCaret())
        assertTrue(accepted)
        assertEquals("""^\QTestTest\E$/^TheApplicationStarts$""", config.pattern)
        assertEquals(GodogFramework.INSTANCE, config.testFramework)
    }

    fun testScenarioOutlineRun() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        val (accepted, config) = runProducer(elementAtCaret())
        assertTrue(accepted)
        assertEquals("""^\QTestTest\E$/^TheResponseCodeIsChecked(#\d+)?$""", config.pattern)
        assertEquals(GodogFramework.INSTANCE, config.testFramework)
    }

    fun testFeatureLevelRun() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        val (accepted, config) = runProducer(elementAtCaret())
        assertTrue(accepted)
        assertEquals("""^\QTestTest\E$""", config.pattern)
        assertEquals(GodogFramework.INSTANCE, config.testFramework)
    }

    fun testNoStepContainers() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        val (accepted, _) = runProducer(elementAtCaret())
        assertFalse(accepted)
    }
}

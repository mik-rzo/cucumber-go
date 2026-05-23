package com.github.mikrzo.cucumbergo.run

import com.github.mikrzo.cucumbergo.godog.GodogFramework
import com.goide.GoCodeInsightFixtureTestCase
import com.goide.execution.testing.GoTestRunConfiguration
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.ActionPlaces

class GodogRunConfigurationProducerTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData/run"

    private fun produceFromContext(): GoTestRunConfiguration? {
        val dataContext = DataManager.getInstance().getDataContext(myFixture.editor.component)
        val context = ConfigurationContext.getFromContext(dataContext, ActionPlaces.UNKNOWN)
        val ours = context.configurationsFromContext.orEmpty()
            .firstOrNull { it.isProducedBy(GodogRunConfigurationProducer::class.java) }
        return ours?.configuration as GoTestRunConfiguration?
    }

    fun testScenarioRun() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        val config = produceFromContext()
        assertNotNull("Godog producer did not produce a config", config)
        assertEquals("""^\QTestTest\E$/^TheApplicationStarts$""", config!!.pattern)
        assertEquals(GodogFramework.INSTANCE, config.testFramework)
    }

    fun testScenarioOutlineRun() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        val config = produceFromContext()
        assertNotNull("Godog producer did not produce a config", config)
        assertEquals("""^\QTestTest\E$/^TheResponseCodeIsChecked(#\d+)?$""", config!!.pattern)
        assertEquals(GodogFramework.INSTANCE, config.testFramework)
    }

    fun testFeatureLevelRun() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        val config = produceFromContext()
        assertNotNull("Godog producer did not produce a config", config)
        assertEquals("""^\QTestTest\E$""", config!!.pattern)
        assertEquals(GodogFramework.INSTANCE, config.testFramework)
    }

    fun testNoStepContainers() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        assertNull("expected no Godog config when no step containers exist", produceFromContext())
    }
}
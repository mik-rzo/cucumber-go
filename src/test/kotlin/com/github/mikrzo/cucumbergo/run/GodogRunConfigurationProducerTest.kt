package com.github.mikrzo.cucumbergo.run

import com.github.mikrzo.cucumbergo.godog.GodogFramework
import com.goide.GoCodeInsightFixtureTestCase
import com.goide.execution.GoBuildingRunConfiguration.Kind
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

    private fun assertCommonGodogConfig(config: GoTestRunConfiguration) {
        assertEquals(GodogFramework.INSTANCE, config.testFramework)
        assertEquals(Kind.PACKAGE, config.kind)
        val stepFile = myFixture.findFileInTempDir("${getTestName(true)}/step_test.go")
        assertNotNull("step_test.go not found in temp dir", stepFile)
        assertEquals(stepFile!!.parent.path, config.workingDirectory)
    }

    fun testScenario() {
        myFixture.copyDirectoryToProject(getTestName(true), getTestName(true))
        myFixture.configureByFile(getTestName(true) + "/scenario.feature")
        val config = produceFromContext()
        assertNotNull("Godog producer did not produce a config", config)
        assertEquals("""^\QTestScenario\E$/^TheApplicationStarts$""", config!!.pattern)
        assertCommonGodogConfig(config)
    }

    fun testScenarioOutline() {
        myFixture.copyDirectoryToProject(getTestName(true), getTestName(true))
        myFixture.configureByFile(getTestName(true) + "/outline.feature")
        val config = produceFromContext()
        assertNotNull("Godog producer did not produce a config", config)
        assertEquals("""^\QTestOutline\E$/^TheResponseCodeIsChecked(#\d+)?$""", config!!.pattern)
        assertCommonGodogConfig(config)
    }

    fun testFeatureLevel() {
        myFixture.copyDirectoryToProject(getTestName(true), getTestName(true))
        myFixture.configureByFile(getTestName(true) + "/whole.feature")
        val config = produceFromContext()
        assertNotNull("Godog producer did not produce a config", config)
        assertEquals("""^\QTestWhole\E$""", config!!.pattern)
        assertCommonGodogConfig(config)
    }

    fun testStepContainerDirectoryMatch() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile("featureDir/feature.feature")
        val config = produceFromContext()
        assertNotNull("Godog producer did not produce a config", config)
        val stepFile = myFixture.findFileInTempDir("featureDir/step_test.go")
        assertNotNull("step_test.go not found in featureDir", stepFile)
        assertEquals(stepFile!!.parent.path, config!!.workingDirectory)
    }

    fun testNoStepContainers() {
        myFixture.copyDirectoryToProject(getTestName(true), getTestName(true))
        myFixture.configureByFile(getTestName(true) + "/orphan.feature")
        assertNull("expected no Godog config when no step containers exist", produceFromContext())
    }
}

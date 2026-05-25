package com.github.mikrzo.cucumbergo.run

import com.github.mikrzo.cucumbergo.CucumberExtension
import com.github.mikrzo.cucumbergo.toPascalCase
import com.github.mikrzo.cucumbergo.godog.GodogFramework
import com.goide.execution.GoBuildingRunConfiguration.Kind
import com.goide.execution.GoRunUtil
import com.goide.execution.testing.GoTestRunConfiguration
import com.goide.execution.testing.GoTestRunConfigurationProducerBase
import com.goide.psi.GoFile
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.plugins.cucumber.psi.GherkinFile
import org.jetbrains.plugins.cucumber.psi.GherkinScenario
import org.jetbrains.plugins.cucumber.psi.GherkinScenarioOutline

class GodogRunConfigurationProducer private constructor() :
    GoTestRunConfigurationProducerBase(GodogFramework.INSTANCE) {
    override fun setupConfigurationFromContext(
        configuration: GoTestRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<*>
    ): Boolean {
        val element = sourceElement.get() as PsiElement
        if (element.containingFile is GherkinFile) {
            val file = element.containingFile.virtualFile

            // assuming all steps are in the same directory as the feature file
            val featureDir = file.parent
            CucumberExtension().getStepDefinitionContainers(element.containingFile as GherkinFile)
                .firstOrNull { it.virtualFile.parent == featureDir }.let {
                if (it == null) {
                    return false
                }
                val module = ModuleUtilCore.findModuleForPsiElement(it) ?: return false
                configuration.setModule(module)
                configuration.workingDirectory = it.virtualFile.parent.path
                configuration.`package` = (it as GoFile).getImportPath(false).toString()
            }

            configuration.testFramework = GodogFramework.INSTANCE
            configuration.kind = Kind.PACKAGE
            configuration.goToolParams = GoRunUtil.filterOutInstallParameter(configuration.goToolParams)
            val scenario = PsiTreeUtil.getParentOfType(
                element,
                GherkinScenario::class.java
            )

            val scenarioOutline = PsiTreeUtil.getParentOfType(
                element,
                GherkinScenarioOutline::class.java
            )

            var pattern = $$"^\\QTest$${toPascalCase(file.nameWithoutExtension)}\\E$"

            if (scenario != null) {
                pattern = pattern + "/^" + toPascalCase(scenario.scenarioName) + "$"
            } else if (scenarioOutline != null) {
                pattern = pattern + "/^" + toPascalCase(scenarioOutline.scenarioName) + "(#\\d+)?$"
            }
            configuration.pattern = pattern

            configuration.setGeneratedName()
            return true
        }
        return false
    }
}
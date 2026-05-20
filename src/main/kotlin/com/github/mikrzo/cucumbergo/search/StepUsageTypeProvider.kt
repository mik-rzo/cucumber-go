package com.github.mikrzo.cucumbergo.search

import com.intellij.psi.PsiElement
import com.intellij.usages.impl.rules.UsageType
import com.intellij.usages.impl.rules.UsageTypeProvider
import org.jetbrains.plugins.cucumber.psi.GherkinStep

private val STEP_USAGE_TYPE = UsageType("Gherkin step")

class StepUsageTypeProvider : UsageTypeProvider {
    override fun getUsageType(element: PsiElement): UsageType? {
        return if (element is GherkinStep) STEP_USAGE_TYPE else null
    }
}

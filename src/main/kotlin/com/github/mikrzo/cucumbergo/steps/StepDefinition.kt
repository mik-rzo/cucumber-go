package com.github.mikrzo.cucumbergo.steps

import com.github.mikrzo.cucumbergo.extractStepPattern
import com.goide.psi.GoCallExpr
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.cucumber.CucumberUtil
import org.jetbrains.plugins.cucumber.steps.AbstractStepDefinition


class StepDefinition(callExpr: GoCallExpr) : AbstractStepDefinition(callExpr) {
    companion object {
        const val REGEX_START = "^"
        const val REGEX_END = "$"
    }

    override fun getVariableNames(): List<String> {
        return listOf()
    }

    override fun getCucumberRegexFromElement(element: PsiElement?): String? {

        val text = getStepDefinitionText() ?: return null
        if (text.startsWith(REGEX_START) || text.endsWith(REGEX_END)) {
            return text
        }
        return CucumberUtil.buildRegexpFromCucumberExpression(text, GoParameterTypeManager)
    }

    private fun getStepDefinitionText(): String? {
        val callExpression = element as? GoCallExpr
        val argument = callExpression?.argumentList?.expressionList?.getOrNull(0) ?: return null
        return extractStepPattern(argument)
    }
}
package com.github.mikrzo.cucumbergo.steps

import com.github.mikrzo.cucumbergo.extractStepPattern
import com.github.mikrzo.cucumbergo.toGoBacktickLiteral
import com.github.mikrzo.cucumbergo.toGoQuotedLiteral
import com.goide.psi.GoCallExpr
import com.goide.psi.GoStringLiteral
import com.goide.psi.impl.GoElementFactory
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.cucumber.steps.AbstractStepDefinition


class StepDefinition(callExpr: GoCallExpr) : AbstractStepDefinition(callExpr) {

    override fun getVariableNames(): List<String> = listOf()

    override fun getCucumberRegexFromElement(element: PsiElement?): String? = getStepDefinitionText()

    override fun setValue(newValue: String) {
        val callExpr = element as? GoCallExpr ?: return
        val arg = callExpr.argumentList.expressionList.getOrNull(0) ?: return

        fun newLiteralFor(existing: GoStringLiteral): GoStringLiteral {
            val text = if (existing.text.startsWith("`")) toGoBacktickLiteral(newValue)
                       else toGoQuotedLiteral(newValue)
            return GoElementFactory.createStringLiteral(callExpr.project, text)
        }

        when (arg) {
            is GoStringLiteral -> arg.replace(newLiteralFor(arg))
            is GoCallExpr -> {
                val funcText = arg.children.getOrNull(0)?.text
                if (funcText == "regexp.MustCompile") {
                    val inner = arg.argumentList.expressionList.getOrNull(0) as? GoStringLiteral ?: return
                    inner.replace(newLiteralFor(inner))
                }
            }
        }
    }

    private fun getStepDefinitionText(): String? {
        val callExpression = element as? GoCallExpr
        val argument = callExpression?.argumentList?.expressionList?.getOrNull(0) ?: return null
        return extractStepPattern(argument)
    }
}

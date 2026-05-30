package com.github.mikrzo.cucumbergo

import com.goide.documentation.GoDocumentationProvider
import com.goide.psi.GoCallExpr
import com.goide.psi.GoReferenceExpression
import com.intellij.lang.documentation.psi.createPsiDocumentationTarget
import com.intellij.platform.backend.documentation.DocumentationTarget
import com.intellij.platform.backend.documentation.PsiDocumentationTargetProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.plugins.cucumber.psi.GherkinStep

class GoStepDocumentationTargetProvider : PsiDocumentationTargetProvider {
    override fun documentationTarget(element: PsiElement, originalElement: PsiElement?): DocumentationTarget? {
        val step = PsiTreeUtil.getParentOfType(originalElement, GherkinStep::class.java, false) ?: return null
        val definition = step.findDefinitions().firstOrNull() ?: return null
        val callExpr = definition.element as? GoCallExpr ?: return null
        val handlerArg = callExpr.argumentList.expressionList.getOrNull(1) ?: return null
        val resolved = (handlerArg as? GoReferenceExpression)?.resolve() ?: return null
        if (GoDocumentationProvider.getCommentsForElement(resolved).isEmpty()) return null
        return createPsiDocumentationTarget(resolved, originalElement)
    }
}

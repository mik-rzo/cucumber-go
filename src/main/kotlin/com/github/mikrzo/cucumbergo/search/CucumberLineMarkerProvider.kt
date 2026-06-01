package com.github.mikrzo.cucumbergo.search

import com.github.mikrzo.cucumbergo.extractStepPattern
import com.goide.psi.GoCallExpr
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer.Alignment.RIGHT
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import icons.CucumberIcons.Cucumber

class CucumberLineMarkerProvider : LineMarkerProvider {
    private val keywords = listOf("Given", "When", "Then", "Step")

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (!validCandidate(element)) {
            return null
        }
        val textElement = element.children[1].children[0]
        // stepName = null means first arg is a call we don't recognise as a step pattern
        // (not a string literal, not regexp.MustCompile/Compile) — skip the marker
        val stepName = extractStepPattern(textElement) ?: return null
        // Anchor must be a leaf; firstChild is a leaf for string literals but a
        // reference expression for regexp.MustCompile(...), so descend to the leaf.
        val anchor = PsiTreeUtil.getDeepestFirst(textElement)
        return LineMarkerInfo(
            anchor,
            textElement.textRange,
            Cucumber,
            { stepName },
            null,
            RIGHT,
            { stepName })

    }

    private fun validCandidate(element: PsiElement) =
        element is GoCallExpr && element.children.size == 2 && keywords.contains(element.children[0].lastChild.text)
}
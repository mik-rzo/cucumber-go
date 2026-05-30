package com.github.mikrzo.cucumbergo

import com.goide.documentation.GoDocumentationProvider
import com.goide.psi.GoNamedElement
import com.intellij.model.Pointer
import com.intellij.openapi.application.ReadAction
import com.intellij.platform.backend.documentation.DocumentationResult
import com.intellij.platform.backend.documentation.DocumentationTarget
import com.intellij.platform.backend.presentation.TargetPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.SmartPsiElementPointer

class GoStepDocumentationTarget(
    private val pointer: SmartPsiElementPointer<PsiElement>,
    private val originalElement: PsiElement?,
) : DocumentationTarget {

    companion object {
        private val NILABILITY_SECTION_REGEX = Regex(
            "<table class='sections'>(?:(?!</table>).)*?Nilability info.*?</table>",
            setOf(RegexOption.DOT_MATCHES_ALL),
        )
    }

    override fun createPointer(): Pointer<out DocumentationTarget> = Pointer {
        pointer.element?.let {
            GoStepDocumentationTarget(SmartPointerManager.createPointer(it), originalElement)
        }
    }

    override fun computePresentation(): TargetPresentation = ReadAction.computeBlocking<TargetPresentation, RuntimeException> {
        val element = pointer.element
        val name = (element as? GoNamedElement)?.name ?: ""
        TargetPresentation.builder(name).icon(element?.getIcon(0)).presentation()
    }

    override fun computeDocumentation(): DocumentationResult? {
        val element = pointer.element ?: return null
        val html = ReadAction.computeBlocking<String?, RuntimeException> {
            GoDocumentationProvider().generateDoc(element, originalElement)
        } ?: return null
        return DocumentationResult.documentation(NILABILITY_SECTION_REGEX.replace(html, ""))
    }
}
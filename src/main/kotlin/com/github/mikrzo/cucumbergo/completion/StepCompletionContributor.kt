package com.github.mikrzo.cucumbergo.completion

import com.goide.psi.GoCallExpr
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementDecorator
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import org.jetbrains.plugins.cucumber.psi.GherkinElementTypes
import org.jetbrains.plugins.cucumber.psi.GherkinStep

class StepCompletionContributor : CompletionContributor() {
    init {
        val inTable = psiElement().inside(psiElement().withElementType(GherkinElementTypes.TABLE))
        val inStep = psiElement()
            .inside(psiElement().withElementType(GherkinElementTypes.STEP))
            .andNot(inTable)

        extend(CompletionType.BASIC, inStep, object : CompletionProvider<CompletionParameters>() {
            override fun addCompletions(
                parameters: CompletionParameters,
                context: ProcessingContext,
                result: CompletionResultSet,
            ) {
                // Wrap each step definition element with a fixed insert handler.
                result.runRemainingContributors(parameters) { completionResult ->
                    val element = completionResult.getLookupElement()
                    if (element.psiElement is GoCallExpr) {
                        val wrapped = LookupElementDecorator.withInsertHandler(element) { ctx, _ ->
                            // LookupElementDecorator.withInsertHandler calls super.handleInsert
                            // before this lambda, so the original StepInsertHandler has already
                            // run and the lookup string has been inserted. Any already-typed
                            // suffix beyond the caret (e.g. the "form" in "I per|form") is still
                            // present in the document. Delete it.
                            ctx.commitDocument()
                            val atTail = ctx.file.findElementAt(ctx.tailOffset - 1) ?: return@withInsertHandler
                            val step = PsiTreeUtil.getParentOfType(atTail, GherkinStep::class.java)
                                ?: return@withInsertHandler
                            val ref = step.references.firstOrNull() ?: return@withInsertHandler
                            val refEnd = step.textRange.startOffset + ref.rangeInElement.endOffset
                            if (refEnd > ctx.tailOffset) {
                                ctx.document.deleteString(ctx.tailOffset, refEnd)
                            }
                        }
                        result.passResult(completionResult.withLookupElement(wrapped))
                    } else {
                        result.passResult(completionResult)
                    }
                }
            }
        })
    }
}

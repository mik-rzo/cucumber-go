package com.github.mikrzo.cucumbergo.completion

import com.goide.psi.GoCallExpr
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
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
                        val wrapped = object : LookupElementDecorator<LookupElement>(element) {
                            override fun handleInsert(ctx: InsertionContext) {
                                // Delete any already-typed suffix BEFORE the original handler runs, so
                                // the live template (builder.run for capture groups) starts on a clean
                                // document and is not killed by a subsequent edit.
                                val insertedEnd = ctx.startOffset + lookupString.length
                                ctx.commitDocument()
                                val atStart = ctx.file.findElementAt(ctx.startOffset)
                                val step = atStart?.let {
                                    PsiTreeUtil.getParentOfType(it, GherkinStep::class.java)
                                }
                                val ref = step?.references?.firstOrNull()
                                if (ref != null && step != null) {
                                    val refEnd = step.textRange.startOffset + ref.rangeInElement.endOffset
                                    if (refEnd > insertedEnd) {
                                        ctx.document.deleteString(insertedEnd, refEnd)
                                        ctx.commitDocument()
                                    }
                                }
                                super.handleInsert(ctx)
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

package com.github.mikrzo.cucumbergo

import com.github.mikrzo.cucumbergo.steps.StepDefinition
import com.goide.GoFileType
import com.goide.psi.GoCallExpr
import com.goide.psi.GoFile
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.indexing.FileBasedIndex
import org.jetbrains.plugins.cucumber.BDDFrameworkType
import org.jetbrains.plugins.cucumber.StepDefinitionCreator
import org.jetbrains.plugins.cucumber.psi.GherkinFile
import org.jetbrains.plugins.cucumber.steps.AbstractCucumberExtension
import org.jetbrains.plugins.cucumber.steps.AbstractStepDefinition
import com.github.mikrzo.cucumbergo.steps.StepDefinitionCreator as GoStepDefinitionCreator

class CucumberExtension : AbstractCucumberExtension() {
    override fun isStepLikeFile(child: PsiElement): Boolean {
        return child is GoFile
    }

    override fun isWritableStepLikeFile(child: PsiElement): Boolean {
        return (child as? GoFile)?.containingFile?.virtualFile?.isWritable ?: false
    }

    override fun getStepFileType(): BDDFrameworkType {
        return BDDFrameworkType(GoFileType.INSTANCE)
    }

    override fun getStepDefinitionCreator(): StepDefinitionCreator {
        return GoStepDefinitionCreator()
    }

    override fun loadStepsFor(featureFile: PsiFile?, module: Module): List<AbstractStepDefinition> =
        loadStepsFor(module)

    // No `override` — compiles against 261.x where this method doesn't exist yet, but satisfies the
    // abstract method added in 262.x at runtime via JVM signature resolution.
    fun loadStepsFor(module: Module): List<AbstractStepDefinition> {
        val fileBasedIndex = FileBasedIndex.getInstance()
        val project = module.project
        val scope = module
            .getModuleWithDependenciesAndLibrariesScope(true)
            .uniteWith(module.moduleContentWithDependenciesScope)
        val result = mutableListOf<AbstractStepDefinition>()

        fileBasedIndex.processValues(INDEX_ID, true, null, { file, value ->
            ProgressManager.checkCanceled()
            val psiFile = PsiManager.getInstance(project).findFile(file)
            if (psiFile == null) {
                true
            } else {
                for (offset in value) {
                    val element = psiFile.findElementAt(offset + 1)
                    val stepDefPsi = PsiTreeUtil.getParentOfType(element, GoCallExpr::class.java)
                    stepDefPsi?.let {
                        result.add(StepDefinition(stepDefPsi))
                    }
                }
                true
            }
        }, scope)

        return result
    }

    override fun getStepDefinitionContainers(featureFile: GherkinFile): Collection<PsiFile> {
        val module = ModuleUtilCore.findModuleForPsiElement(featureFile)
        val steps = module?.let { mod ->
            loadStepsFor(featureFile, mod)
        }
        val psiFiles = steps
            ?.mapNotNull { step -> step.element?.containingFile }
            ?.filter { isWritableStepLikeFile(it) }
            ?.distinct()
            ?: emptyList()
        return psiFiles
    }
}
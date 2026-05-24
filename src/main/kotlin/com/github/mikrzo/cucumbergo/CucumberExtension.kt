package com.github.mikrzo.cucumbergo

import com.github.mikrzo.cucumbergo.steps.StepDefinition
import com.goide.GoFileType
import com.goide.GoTypes
import com.goide.psi.GoCallExpr
import com.goide.psi.GoFile
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiRecursiveElementWalkingVisitor
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.GlobalSearchScopes
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

    override fun loadStepsFor(featureFile: PsiFile?, module: Module): List<AbstractStepDefinition> {
        val stepContainerDir = featureFile?.virtualFile?.let { findStepContainerDir(it) }
        val scope = if (stepContainerDir != null)
            GlobalSearchScopes.directoryScope(module.project, stepContainerDir, false)
        else
            module.getModuleWithDependenciesAndLibrariesScope(true)
                .uniteWith(module.moduleContentWithDependenciesScope)
        return loadStepsFor(module, scope)
    }

    // No `override` — compiles against 261.x where this method doesn't exist yet, but satisfies the
    // abstract method added in 262.x at runtime via JVM signature resolution.
    fun loadStepsFor(module: Module): List<AbstractStepDefinition> =
        loadStepsFor(module, module.getModuleWithDependenciesAndLibrariesScope(true)
            .uniteWith(module.moduleContentWithDependenciesScope))

    private fun loadStepsFor(module: Module, scope: GlobalSearchScope): List<AbstractStepDefinition> {
        val fileBasedIndex = FileBasedIndex.getInstance()
        val project = module.project
        val result = mutableListOf<AbstractStepDefinition>()
        val processedFiles = mutableSetOf<VirtualFile>()

        val fileDocManager = FileDocumentManager.getInstance()

        fileBasedIndex.processValues(INDEX_ID, true, null, { file, value ->
            ProgressManager.checkCanceled()
            processedFiles.add(file)
            val psiFile = PsiManager.getInstance(project).findFile(file) ?: return@processValues true
            val cachedDoc = fileDocManager.getCachedDocument(file)
            if (cachedDoc != null && fileDocManager.isDocumentUnsaved(cachedDoc)) {
                // Stored offsets are stale for files with unsaved in-memory changes
                // (e.g. immediately after step creation). Scan PSI directly instead.
                psiFile.findStepCallExprs().forEach { result.add(StepDefinition(it)) }
            } else {
                for (offset in value) {
                    val element = psiFile.findElementAt(offset + 1)
                    val stepDefPsi = PsiTreeUtil.getParentOfType(element, GoCallExpr::class.java)
                    stepDefPsi?.let { result.add(StepDefinition(stepDefPsi)) }
                }
            }
            true
        }, scope)

        // Fallback: processValues may skip dirty files entirely in some IntelliJ versions.
        // Scan any qualifying unsaved documents not already covered above.
        for (doc in fileDocManager.unsavedDocuments) {
            val vf = fileDocManager.getFile(doc) ?: continue
            if (vf in processedFiles || !scope.contains(vf)) continue
            val goFile = PsiManager.getInstance(project).findFile(vf) as? GoFile ?: continue
            if (!goFile.imports.any { it.path == GODOG_PACKAGE }) continue
            goFile.findStepCallExprs().forEach { result.add(StepDefinition(it)) }
        }

        return result
    }

    private fun findStepContainerDir(featureFile: VirtualFile): VirtualFile? {
        var dir = featureFile.parent
        while (dir != null) {
            if (dir.children.any { it.name.endsWith("_test.go") }) return dir
            if (dir.findChild("go.mod") != null) break
            dir = dir.parent
        }
        return null
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

private fun PsiFile.findStepCallExprs(): List<GoCallExpr> {
    val result = mutableListOf<GoCallExpr>()
    accept(object : PsiRecursiveElementWalkingVisitor() {
        override fun visitElement(element: PsiElement) {
            if (element.node?.elementType == GoTypes.IDENTIFIER &&
                StepUtils.checkIdentifierName(element.text)) {
                PsiTreeUtil.getParentOfType(element, GoCallExpr::class.java)
                    ?.let { result.add(it) }
            }
            super.visitElement(element)
        }
    })
    return result
}
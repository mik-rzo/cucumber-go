package com.github.mikrzo.cucumbergo.search

import com.github.mikrzo.cucumbergo.StepDeclaration
import com.github.mikrzo.cucumbergo.steps.StepDefinition
import com.goide.GoCodeInsightFixtureTestCase
import com.goide.psi.GoCallExpr
import com.goide.psi.GoFile
import com.intellij.pom.references.PomService
import com.intellij.psi.util.PsiTreeUtil

class CucumberGoFindUsagesTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData/search"

    fun testStepUsages() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        val usages = myFixture.testFindUsagesUsingAction("step_test.go").map { it.toString() }.toTypedArray()
        assertEquals(2, usages.size)
        assertSameElements(
            usages,
            "4|Given| |I say \"hello\"",
            "7|Given| |I say \"world\"",
        )
    }

    fun testStepNoUsages() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile("step_test.go")
        val goFile = myFixture.file as GoFile
        val callExpr = PsiTreeUtil.findChildrenOfType(goFile, GoCallExpr::class.java).first()
        val stepName = StepDefinition(callExpr).getCucumberRegex() ?: ""
        val pomElement = PomService.convertToPsi(project, StepDeclaration(callExpr, stepName))
        val usages = myFixture.findUsages(pomElement)
        assertEquals(0, usages.size)
    }

    fun testStepUsagesIntParameter() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        val usages = myFixture.testFindUsagesUsingAction("step_test.go").map { it.toString() }.toTypedArray()
        assertEquals(2, usages.size)
        assertSameElements(
            usages,
            "4|Given| |the response code is 200",
            "7|Given| |the response code is 404",
        )
    }
}
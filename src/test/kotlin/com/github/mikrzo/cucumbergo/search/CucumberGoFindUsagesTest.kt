package com.github.mikrzo.cucumbergo.search

import com.github.mikrzo.cucumbergo.StepDeclaration
import com.goide.GoCodeInsightFixtureTestCase
import com.goide.psi.GoCallExpr
import com.intellij.pom.PomTarget
import com.intellij.pom.references.PomService
import com.intellij.psi.util.PsiTreeUtil

class CucumberGoFindUsagesTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData/search"

    // Raw regex pattern: returned verbatim by the Cucumber regex resolver
    fun testWithUsages() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        val usages = myFixture.testFindUsagesUsingAction("step_test.go").map { it.toString() }.toTypedArray()
        assertEquals(2, usages.size)
        assertSameElements(
            usages,
            "4|Given| |I say \"hello\"",
            "7|Given| |I say \"world\"",
        )
    }

    // regexp.MustCompile-wrapped pattern: unwrapped by extractStepPattern before matching
    fun testRegexpUsages() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        val usages = myFixture.testFindUsagesUsingAction("step_test.go").map { it.toString() }.toTypedArray()
        assertEquals(2, usages.size)
        assertSameElements(
            usages,
            "4|Given| |I say \"hello\"",
            "7|Given| |I say \"world\"",
        )
    }

    fun testNoUsages() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile("step_test.go")
        val callExpr = PsiTreeUtil.findChildrenOfType(myFixture.file, GoCallExpr::class.java).first()
        val declarations = mutableListOf<PomTarget>()
        StepDeclarationSearcher().findDeclarationsAt(callExpr.argumentList, 0) { declarations.add(it) }
        val declaration = requireNotNull(declarations.singleOrNull() as? StepDeclaration) {
            "Expected exactly one StepDeclaration at caret"
        }
        val pomElement = PomService.convertToPsi(project, declaration)
        val usages = myFixture.findUsages(pomElement)
        assertEquals(0, usages.size)
    }

    // Cucumber Expression with {int}: goes through buildRegexpFromCucumberExpression before matching
    fun testCucumberExpression() {
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
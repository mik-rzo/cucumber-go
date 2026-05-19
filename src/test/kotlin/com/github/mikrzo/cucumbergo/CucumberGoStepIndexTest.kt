package com.github.mikrzo.cucumbergo

import com.goide.GoCodeInsightFixtureTestCase
import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex

class CucumberGoStepIndexTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData"

    private val bt = '`'

    private fun collectOffsets(file: PsiFile): List<Int> {
        val vFile = file.virtualFile
        val scope = GlobalSearchScope.fileScope(project, vFile)
        val result = mutableListOf<Int>()
        FileBasedIndex.getInstance().processValues(INDEX_ID, true, vFile, { _, offsets ->
            result.addAll(offsets)
            true
        }, scope)
        return result
    }

    fun testGivenIdentifierIndexed() {
        val source = "package test\n\nimport \"github.com/cucumber/godog\"\n\nfunc Init(ctx *godog.ScenarioContext) {\n    ctx.Given(" + bt + "^foo$" + bt + ", nil)\n}"
        val file = myFixture.configureByText("step_test.go", source)
        val offsets = collectOffsets(file)
        assertEquals(1, offsets.size)
        assertEquals("Given", file.text.substring(offsets[0]).take("Given".length))
    }

    fun testAllKeywordsIndexed() {
        val source = "package test\n\nimport \"github.com/cucumber/godog\"\n\nfunc Init(ctx *godog.ScenarioContext) {\n    ctx.Given(" + bt + "^a$" + bt + ", nil)\n    ctx.When(" + bt + "^b$" + bt + ", nil)\n    ctx.Then(" + bt + "^c$" + bt + ", nil)\n    ctx.Step(" + bt + "^d$" + bt + ", nil)\n}"
        val file = myFixture.configureByText("step_test.go", source)
        val offsets = collectOffsets(file)
        assertEquals(4, offsets.size)
        val names = offsets.map { offset ->
            val end = file.text.indexOf('(', offset)
            file.text.substring(offset, end)
        }.toSet()
        assertEquals(setOf("Given", "When", "Then", "Step"), names)
    }

    fun testBroadCaptureIncludesCoincidentalIdentifiers() {
        // Index is intentionally broad: captures any IDENTIFIER token with a step keyword name,
        // regardless of whether it is an actual step registration. Refinement happens at resolution time.
        val source = "package test\n\nimport \"github.com/cucumber/godog\"\n\nfunc Init(ctx *godog.ScenarioContext) {\n    ctx.Step(" + bt + "^real step$" + bt + ", nil)\n    Step := \"coincidental\"\n    _ = Step\n}"
        val file = myFixture.configureByText("step_test.go", source)
        val offsets = collectOffsets(file)
        assertTrue("Index should capture coincidental step keyword names", offsets.size > 1)
    }
}

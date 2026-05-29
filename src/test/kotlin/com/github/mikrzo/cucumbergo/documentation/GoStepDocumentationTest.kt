package com.github.mikrzo.cucumbergo.documentation

import com.github.mikrzo.cucumbergo.GoStepDocumentationTargetProvider
import com.goide.GoCodeInsightFixtureTestCase
import com.intellij.lang.documentation.ide.IdeDocumentationTargetProvider
import com.intellij.openapi.application.ReadAction
import com.intellij.platform.backend.documentation.DocumentationTarget
import com.intellij.platform.backend.documentation.impl.computeDocumentationBlocking
import com.intellij.testFramework.PlatformTestUtil

class GoStepDocumentationTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData/documentation"

    fun testPlainFunction() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")

        val editor = myFixture.editor
        val file = myFixture.file
        val offset = editor.caretModel.offset

        val targets: List<DocumentationTarget> = checkNotNull(
            PlatformTestUtil.callOnBgtSynchronously(
                {
                    ReadAction.computeBlocking<List<DocumentationTarget>, RuntimeException> {
                        IdeDocumentationTargetProvider.getInstance(project).documentationTargets(editor, file, offset)
                    }
                },
                10,
            )
        )
        assertFalse("Expected at least one documentation target", targets.isEmpty())

        val html = targets.mapNotNull { computeDocumentationBlocking(it.createPointer())?.html }.joinToString()
        assertTrue("Documentation should contain the handler's doc comment", html.contains("verifies the basket item count"))
    }

    fun testMethodValue() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")

        val editor = myFixture.editor
        val file = myFixture.file
        val offset = editor.caretModel.offset

        val targets: List<DocumentationTarget> = checkNotNull(
            PlatformTestUtil.callOnBgtSynchronously(
                {
                    ReadAction.computeBlocking<List<DocumentationTarget>, RuntimeException> {
                        IdeDocumentationTargetProvider.getInstance(project).documentationTargets(editor, file, offset)
                    }
                },
                10,
            )
        )
        assertFalse("Expected at least one documentation target", targets.isEmpty())

        val html = targets.mapNotNull { computeDocumentationBlocking(it.createPointer())?.html }.joinToString()
        assertTrue("Documentation should contain the handler's doc comment", html.contains("verifies the basket item count"))
    }

    fun testAnonymousFunction() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!
        val target = GoStepDocumentationTargetProvider().documentationTarget(element, element)
        assertNull("Expected no documentation target for step with anonymous function handler", target)
    }
}

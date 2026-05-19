package com.github.mikrzo.cucumbergo

import com.goide.GoCodeInsightFixtureTestCase
import com.goide.psi.GoFile

class FixtureSmokeTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath(): String = "src/test/testData"

    fun testSimpleFixtureGoFileImportsNonEmpty() {
        myFixture.configureByFile("simple/simple_test.go")
        val psiFile = myFixture.file
        assertTrue(
            "Expected ${psiFile?.name} to parse as GoFile but got ${psiFile?.javaClass?.name}",
            psiFile is GoFile,
        )
        val imports = (psiFile as GoFile).imports
        assertFalse(
            "simple_test.go has imports in source but PSI reported none — fixture pipeline may need a cucumberGoProjectDescriptor().",
            imports.isEmpty(),
        )
    }
}

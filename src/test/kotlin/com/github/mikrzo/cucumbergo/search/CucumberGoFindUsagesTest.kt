package com.github.mikrzo.cucumbergo.search

import com.goide.GoCodeInsightFixtureTestCase

class CucumberGoFindUsagesTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData/findUsages"

    fun testStepUsages() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        val usages = myFixture.testFindUsagesUsingAction("step_test.go")
        assertEquals(2, usages.size)
    }
}
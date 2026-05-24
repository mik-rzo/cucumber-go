package com.github.mikrzo.cucumbergo.inspections

import com.github.mikrzo.cucumbergo.steps.StepDefinitionCreator
import com.goide.GoCodeInsightFixtureTestCase
import com.goide.psi.GoFile
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.plugins.cucumber.inspections.CucumberStepInspection
import org.jetbrains.plugins.cucumber.psi.GherkinFile
import org.jetbrains.plugins.cucumber.psi.GherkinStep

class StepDefinitionCreatorTest : GoCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/testData/inspections"

    private val creator = StepDefinitionCreator()

    private fun projectPsiDir(): PsiDirectory {
        val vf = myFixture.findFileInTempDir("test.feature")!!.parent
        return PsiManager.getInstance(project).findDirectory(vf)!!
    }

    private fun gherkinStep(featureFile: String, stepText: String): GherkinStep {
        val file = myFixture.configureByFile(featureFile) as GherkinFile
        return PsiTreeUtil.findChildrenOfType(file, GherkinStep::class.java)
            .first { it.name == stepText }
    }

    private fun createContainer(dir: PsiDirectory, name: String): GoFile =
        WriteCommandAction.writeCommandAction(project).compute<GoFile, RuntimeException> {
            creator.createStepDefinitionContainer(dir, name) as GoFile
        }

    private fun addStep(step: GherkinStep, file: GoFile) {
        WriteCommandAction.runWriteCommandAction(project) {
            creator.createStepDefinition(step, file, false)
        }
    }

    fun testCreateStepDefinitionContainerStructure() {
        val dir = PsiManager.getInstance(project)
            .findDirectory(myFixture.tempDirFixture.findOrCreateDir("."))!!
        val goFile = createContainer(dir, "demo_test.go")
        val text = goFile.text
        assertTrue(text.contains("package steps"))
        assertTrue(text.contains("\"testing\""))
        assertTrue(text.contains("\"context\""))
        assertTrue(text.contains("\"github.com/cucumber/godog\""))
        assertTrue(text.contains("func TestDemo(t *testing.T)"))
        assertTrue(text.contains("func InitializeDemoScenario(ctx *godog.ScenarioContext)"))
    }

    fun testCreateStepDefinitionNoParams() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        val step = gherkinStep(getTestName(true) + "/test.feature", "I do something")
        val goFile = createContainer(projectPsiDir(), "test_test.go")
        addStep(step, goFile)
        val text = goFile.text
        assertTrue(text.contains("func iDoSomething("))
        assertTrue(text.contains("ctx.Step("))
    }

    fun testCreateStepDefinitionWithParam() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        val step = gherkinStep(getTestName(true) + "/test.feature", "I have <count> items")
        val goFile = createContainer(projectPsiDir(), "test_test.go")
        addStep(step, goFile)
        assertTrue(goFile.text.contains("count string"))
    }

    fun testCreateStepDefinitionMultipleParams() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        val step = gherkinStep(getTestName(true) + "/test.feature", "I have <a> and <b>")
        val goFile = createContainer(projectPsiDir(), "test_test.go")
        addStep(step, goFile)
        val text = goFile.text
        assertTrue(text.contains("a string"))
        assertTrue(text.contains("b string"))
    }

    fun testCreateStepDefinitionSpecialChars() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        val step = gherkinStep(getTestName(true) + "/test.feature", "I say \"hello\"")
        val goFile = createContainer(projectPsiDir(), "test_test.go")
        addStep(step, goFile)
        assertTrue(goFile.text.contains("func iSayHello("))
    }

    fun testCreateStepDefinitionDuplicate() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        val step = gherkinStep(getTestName(true) + "/test.feature", "I do something")
        val goFile = createContainer(projectPsiDir(), "test_test.go")
        addStep(step, goFile)
        addStep(step, goFile)
        val text = goFile.text
        // Function declaration is de-duplicated (a second one would break Go compilation);
        // ctx.Step registration is not — godog surfaces the ambiguity at runtime instead.
        assertEquals(1, "func iDoSomething".toRegex().findAll(text).count())
        assertEquals(2, "ctx\\.Step\\(".toRegex().findAll(text).count())
    }

    fun testCreateStepDefDoesNotInvalidateExistingStepDef() {
        myFixture.enableInspections(CucumberStepInspection())
        myFixture.copyDirectoryToProject(getTestName(true), "")
        val step = gherkinStep(getTestName(true) + "/test.feature", "I do something new")
        val vf = myFixture.findFileInTempDir("step_test.go")!!
        val goFile = PsiManager.getInstance(project).findFile(vf) as GoFile
        addStep(step, goFile)
        myFixture.configureByFile(getTestName(true) + "/test.feature")
        myFixture.testHighlighting(true, true, true)
    }

    fun testCreateStepDefinitionExistingFile() {
        myFixture.copyDirectoryToProject(getTestName(true), "")
        val step = gherkinStep(getTestName(true) + "/test.feature", "I do something new")
        val vf = myFixture.findFileInTempDir("step_test.go")!!
        val goFile = PsiManager.getInstance(project).findFile(vf) as GoFile
        addStep(step, goFile)
        val text = goFile.text
        assertTrue(text.contains("func iDoSomethingNew("))
        assertTrue(text.contains("func iDoSomething("))
        assertEquals(2, "ctx\\.Step\\(".toRegex().findAll(text).count())
        assertTrue("new step def must be preceded by a blank line", text.contains("\n\nfunc iDoSomethingNew("))
        assertTrue("file must end with a trailing newline", text.endsWith("\n"))
    }
}

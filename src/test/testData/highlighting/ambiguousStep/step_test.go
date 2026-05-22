package ambiguousstep

import (
	"testing"

	"github.com/cucumber/godog"
)

func theStepIsCucumberExpr(path string) error      { return nil }
func theStepIsRegex(path string) error             { return nil }
func anotherStepDuplicate1(path string) error      { return nil }
func anotherStepDuplicate2(path string) error      { return nil }
func anotherStepDuplicate3(path string) error      { return nil }
func unambiguousStep() error                       { return nil }
func branchNarrowPattern(path string) error        { return nil }
func branchBroadPattern(path string) error         { return nil }

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`the step is {word}`, theStepIsCucumberExpr)
	ctx.Step(`^the step is (.+)$`, theStepIsRegex)
	ctx.Step(`another step is {word} blah!`, anotherStepDuplicate1)
	ctx.Step(`another step is {word} blah!`, anotherStepDuplicate2)
	ctx.Step(`another step is {word} blah!`, anotherStepDuplicate3)
	ctx.Step(`this step is very unambiguous`, unambiguousStep)
	ctx.Step(`^(current)? branch ([^ ]+)$`, branchNarrowPattern)
	ctx.Step(`^(current )?branch (\S+)(?: in '(.+)')?$`, branchBroadPattern)
}

func TestFeatures(t *testing.T) {
	suite := godog.TestSuite{
		ScenarioInitializer: InitializeScenario,
		Options: &godog.Options{
			Format:   "pretty",
			Paths:    []string{"."},
			TestingT: t,
		},
	}
	if suite.Run() != 0 {
		t.Fatal("non-zero status returned, failed to run feature tests")
	}
}

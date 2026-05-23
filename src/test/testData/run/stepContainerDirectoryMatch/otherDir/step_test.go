package otherdir

import (
	"testing"

	"github.com/cucumber/godog"
)

func aStepDefinedInTheOtherDirectory() error { return nil }

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`^a step defined in the other directory$`, aStepDefinedInTheOtherDirectory)
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

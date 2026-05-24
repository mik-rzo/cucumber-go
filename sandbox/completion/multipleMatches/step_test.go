package multiplematches

import (
	"testing"

	"github.com/cucumber/godog"
)

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`^I perform an action$`, iPerformAnAction)
	ctx.Step(`^I perform another action$`, iPerformAnotherAction)
	ctx.Step(`^I perform (\d+) actions$`, iPerformNActions)
}

func TestMultipleMatches(t *testing.T) {
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

func iPerformAnAction() error {
	return nil
}

func iPerformAnotherAction() error {
	return nil
}

func iPerformNActions(n int) error {
	return nil
}

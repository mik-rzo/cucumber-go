package runconfiguration

import (
	"testing"

	"github.com/cucumber/godog"
)

func iRunThisScenarioWithGodog() error {
	return nil
}

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`^I run this scenario with godog$`, iRunThisScenarioWithGodog)
}

func TestRunConfiguration(t *testing.T) {
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

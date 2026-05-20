package guttericon

import (
	"testing"

	"github.com/cucumber/godog"
)

func iRunTheScenario() error {
	return nil
}

func iCheckTheResult() error {
	return nil
}

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`^I run the scenario$`, iRunTheScenario)
	ctx.Step(`^I check the result$`, iCheckTheResult)
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

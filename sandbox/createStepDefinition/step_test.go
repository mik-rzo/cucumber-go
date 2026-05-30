package stepcreation

import (
	"testing"

	"github.com/cucumber/godog"
)

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`^I have an existing step definition$`, iHaveAnExistingStepDefinition)
}

func TestCreateStepDefinition(t *testing.T) {
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

func iHaveAnExistingStepDefinition() error {
	return nil
}

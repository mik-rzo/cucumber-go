package stepwithdocstring

import (
	"testing"

	"github.com/cucumber/godog"
)

func myStep() error {
	return nil
}

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`^the following is not highlighted:$`, myStep)
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

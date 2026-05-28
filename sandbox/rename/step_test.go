package rename

import (
	"regexp"
	"testing"

	"github.com/cucumber/godog"
)

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`I stop the server`, iStopTheServer)
	ctx.Step("I start the server", iStartTheServer)
	ctx.Step(regexp.MustCompile(`^I restart the server$`), iRestartTheServer)
}

func TestStepRename(t *testing.T) {
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

func iStopTheServer() error {
	return nil
}

func iStartTheServer() error {
	return nil
}

func iRestartTheServer() error {
	return nil
}

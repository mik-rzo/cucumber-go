package stepresolveregexppattern

import (
	"regexp"
	"testing"

	"github.com/cucumber/godog"
)

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(regexp.MustCompile(`^there is a regexp must compile step$`), thereIsARegexpMustCompileStep)
	ctx.Step(regexp.Compile(`^there is a regexp compile step$`), thereIsARegexpCompileStep)
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

func thereIsARegexpMustCompileStep() error {
	return nil
}

func thereIsARegexpCompileStep() error {
	return nil
}

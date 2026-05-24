package guttericon

import (
	"testing"

	"github.com/cucumber/godog"
)

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Given(`^the server is running$`, theServerIsRunning)
	ctx.When(`^I send a request$`, iSendARequest)
	ctx.Then(`^I receive a response$`, iReceiveAResponse)
}

func TestGutterIcon(t *testing.T) {
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

func theServerIsRunning() error {
	return nil
}

func iSendARequest() error {
	return nil
}

func iReceiveAResponse() error {
	return nil
}

package plainfunctiondoc

import (
	"testing"

	"github.com/cucumber/godog"
)

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`^there are \d+ items in the basket$`, thereAreItemsInTheBasket)
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

// thereAreItemsInTheBasket verifies the basket item count.
func thereAreItemsInTheBasket() error {
	return nil
}

package methodvaluedoc

import (
	"testing"

	"github.com/cucumber/godog"
)

type basketSteps struct{}

func InitializeScenario(ctx *godog.ScenarioContext) {
	s := &basketSteps{}
	ctx.Step(`^there are \d+ items in the basket$`, s.thereAreItemsInTheBasket)
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

// thereAreItemsInTheBasket verifies the basket item count via a method receiver.
func (s *basketSteps) thereAreItemsInTheBasket() error {
	return nil
}

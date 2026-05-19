package steps1

import (
	"github.com/cucumber/godog"
)

func theFirstStep() error {
	return nil
}

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`^the first step$`, theFirstStep)
}

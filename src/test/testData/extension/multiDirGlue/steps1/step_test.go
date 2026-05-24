package steps1

import (
	"github.com/cucumber/godog"
)

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`^the first step$`, theFirstStep)
}

func theFirstStep() error {
	return nil
}

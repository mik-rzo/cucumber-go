package steps2

import (
	"github.com/cucumber/godog"
)

func theSecondStep() error {
	return nil
}

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`^the second step$`, theSecondStep)
}

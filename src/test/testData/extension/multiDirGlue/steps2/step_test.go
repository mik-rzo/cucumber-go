package steps2

import (
	"github.com/cucumber/godog"
)

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`^the second step$`, theSecondStep)
}

func theSecondStep() error {
	return nil
}

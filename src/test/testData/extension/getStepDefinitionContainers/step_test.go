package getstepdefinitioncontainers

import (
	"github.com/cucumber/godog"
)

func aStep() error {
	return nil
}

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`^a step$`, aStep)
}

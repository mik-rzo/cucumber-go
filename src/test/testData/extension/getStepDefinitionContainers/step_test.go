package extensiongetstepdefinitioncontainers

import (
	"github.com/cucumber/godog"
)

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`^a step$`, aStep)
}

func aStep() error {
	return nil
}

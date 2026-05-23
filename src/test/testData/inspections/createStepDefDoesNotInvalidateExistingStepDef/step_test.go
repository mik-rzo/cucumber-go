package steps

import (
	"context"

	"github.com/cucumber/godog"
)

func iDoSomethingExisting(ctx context.Context) (context.Context, error) {
	return ctx, nil
}

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`^I do something existing$`, iDoSomethingExisting)
}

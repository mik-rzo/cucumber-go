package steps

import (
	"context"
	"github.com/cucumber/godog"
)

func iDoSomething(ctx context.Context) (context.Context, error) {
	return ctx, godog.ErrPending
}

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`^I do something$`, iDoSomething)
}

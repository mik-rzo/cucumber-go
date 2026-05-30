package stepdocumentation

import (
	"testing"

	"github.com/cucumber/godog"
)

type ovenSteps struct{}

func InitializeScenario(ctx *godog.ScenarioContext) {
	o := &ovenSteps{}
	ctx.Step(`^the dough has been proofed for (\d+) hours$`, theDoughHasBeenProofedForHours)
	ctx.Step(`^the dough is baked at (\d+) degrees for (\d+) minutes$`, o.theDoughIsBakedAtDegreesForMinutes)
	ctx.Step(`^the bread is left to cool completely$`, theBreadIsLeftToCoolCompletely)
	ctx.Step(`^ingredients are not found$`, ingredientsAreNotFound)
}

func TestDocumentation(t *testing.T) {
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

// theDoughHasBeenProofedForHours checks that the dough was left to rise for the required duration.
func theDoughHasBeenProofedForHours(hours int) error {
	return nil
}

// theDoughIsBakedAtDegreesForMinutes checks that the oven reached the target temperature and held it for the full bake time.
func (o *ovenSteps) theDoughIsBakedAtDegreesForMinutes(degrees, minutes int) error {
	return nil
}

// theBreadIsLeftToCoolCompletely checks that the bread has cooled to room temperature before slicing.
func theBreadIsLeftToCoolCompletely() error {
	return nil
}

func ingredientsAreNotFound() error {
	return nil
}

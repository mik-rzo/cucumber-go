package ambiguousstep

import (
	"testing"

	"github.com/cucumber/godog"
)

func test1a(path string) error { return nil }
func test1b(path string) error { return nil }
func test2a(path string) error { return nil }
func test2b(path string) error { return nil }
func test2c(path string) error { return nil }
func unambiguousStep() error   { return nil }
func testA(path string) error  { return nil }
func testB(path string) error  { return nil }

func InitializeScenario(ctx *godog.ScenarioContext) {
	ctx.Step(`the step is {word}`, test1a)
	ctx.Step(`^the step is (.+)$`, test1b)
	ctx.Step(`another step is {word} blah!`, test2a)
	ctx.Step(`another step is {word} blah!`, test2b)
	ctx.Step(`another step is {word} blah!`, test2c)
	ctx.Step(`this step is very unambiguous`, unambiguousStep)
	ctx.Step(`^(current)? branch ([^ ]+)$`, testA)
	ctx.Step(`^(current )?branch (\S+)(?: in '(.+)')?$`, testB)
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

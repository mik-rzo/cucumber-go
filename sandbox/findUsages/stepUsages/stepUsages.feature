Feature: find usages - step usages

  Scenario: open step_test.go, place caret on the step pattern and ⌥ + F7 to find all matching Gherkin steps
    Given I say "hello"

  Scenario: each matching Gherkin step is listed as a separate result
    Given I say "world"
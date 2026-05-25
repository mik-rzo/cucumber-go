Feature: find usages - cucumber expression

  Scenario: open step_test.go, place caret on the step pattern and ⌥ + F7 to find all matching Gherkin steps
    Given the response code is 200

  Scenario: each matching Gherkin step is listed as a separate result
    Given the response code is 404

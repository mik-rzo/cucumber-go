Feature: find usages - with usages

  Scenario: open step_test.go, place caret on the step pattern and ⌥ + F7 to find all matching Gherkin steps
    Given the forecast is clear

  Scenario: each matching Gherkin step is listed as a separate result
    Given the forecast is clear

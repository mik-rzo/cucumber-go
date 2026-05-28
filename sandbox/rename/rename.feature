Feature: step rename

  Scenario: place caret on the step text below and ⇧ + F6
    Given I start the server

  Scenario: open step_test.go, place caret inside any step pattern string and ⇧ + F6 to rename — all matching Gherkin steps update
    Given I stop the server
    And I start the server
    And I restart the server

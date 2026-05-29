Feature: hover documentation

  Scenario: place caret on a step and ⌃ + Q to see the Go handler's doc comment
    Given the dough has been kneaded for 10 minutes
    When the dough is baked at 220 degrees for 30 minutes
    Then the bread is left to cool completely

  Scenario: anonymous function handlers show no documentation
    Given ingredients are not found
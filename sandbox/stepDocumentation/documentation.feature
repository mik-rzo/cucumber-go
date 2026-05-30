Feature: step documentation

  Scenario: hover over a step to see the Go handler's doc comment
    Given the dough has been proofed for 10 hours
    When the dough is baked at 220 degrees for 30 minutes
    Then the bread is left to cool completely

  Scenario: place caret on a step and press F1 to see the Go handler's doc comment
    Given the dough has been proofed for 10 hours
    When the dough is baked at 220 degrees for 30 minutes
    Then the bread is left to cool completely

  Scenario: step handlers with no doc comment show no documentation
    Given ingredients are not found

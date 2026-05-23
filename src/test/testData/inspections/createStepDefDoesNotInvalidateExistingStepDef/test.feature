Feature: step creation preserves existing

  Scenario: existing step stays resolved after adding new definition
    Given I do something existing
    And I do something new

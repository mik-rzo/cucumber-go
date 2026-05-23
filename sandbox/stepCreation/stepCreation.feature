Feature: step creation

  Scenario: existing step (already has a definition)
    Given I have an existing step definition

  Scenario: undefined steps (⌥ + ↩ to create)
    Given I need a step definition
    And I need another step definition

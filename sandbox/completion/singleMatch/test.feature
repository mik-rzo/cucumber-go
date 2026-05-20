Feature: completion - single match

  # Place caret after "I perform an" and press Ctrl+Space:
  # - only "I perform an action" is offered and auto-inserted
  Scenario: single match
    Given I perform an action

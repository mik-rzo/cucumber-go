Feature: completion - multiple matches

  # Place caret after "I perform" and press Ctrl+Space:
  # - both "I perform an action" and "I perform another action" are offered
  Scenario: multiple matches
    Given I perform an action
    And I perform another action

Feature: completion - no match

  # Place caret after "xyz" and press Ctrl+Space:
  # - no completion items offered
  Scenario: no match
    Given xyz no matching step

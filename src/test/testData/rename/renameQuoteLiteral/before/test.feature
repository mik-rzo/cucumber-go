Feature: rename quote literal

  Scenario: First
    Given I have 42 EUR on my account
    And I have 0 EUR on my account

  Scenario: Second
    Given I have<caret> 25 EUR on my account

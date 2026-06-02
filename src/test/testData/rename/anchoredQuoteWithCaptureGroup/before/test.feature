Feature: rename anchored quote with capture group

  Scenario: First
    Given I withdraw<caret> 21 EUR
    And I withdraw 37 EUR
    And I withdraw 0 EUR

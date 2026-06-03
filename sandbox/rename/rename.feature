Feature: rename

  Scenario: place caret on a step below and ⇧ + F6 to rename — step definition and all matching steps update
    Given I stop the server
    And I start the server
    And I restart the server

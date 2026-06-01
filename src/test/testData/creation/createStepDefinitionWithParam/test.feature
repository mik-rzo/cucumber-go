Feature: with param
  Scenario Outline: step has one outline parameter
    Given I have <count> items
  Examples:
    | count |
    | 3     |

Feature: inspection

  Scenario: resolved step has no warning
    Given I have a resolved step

  Scenario: unresolved step is highlighted with a warning
    Given I have no step definition

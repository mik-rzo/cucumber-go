Feature: Docstring
  Scenario: Does not highlight docstring content
    Then the step is not highlighted:
      """
      This is a doc string.
      """

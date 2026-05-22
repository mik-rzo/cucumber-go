Feature: Docstring
  Scenario: Does not highlight docstring content
    Then the following is not highlighted:
      """
      This is a doc string.
      """

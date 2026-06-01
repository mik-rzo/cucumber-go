Feature: step with docstring
  Scenario: docstring content is not highlighted
    Then the following is not highlighted:
      """
      This is a doc string.
      """

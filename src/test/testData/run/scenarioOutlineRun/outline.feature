Feature: run configuration for a scenario outline

  Scenario Outline: the response code is checked
    Given <caret>the server is running

    Examples:
      | status |
      | up     |
      | down   |

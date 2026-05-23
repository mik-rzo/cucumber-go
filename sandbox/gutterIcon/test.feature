Feature: gutter icon

  Scenario: open step_test.go to see the Cucumber gutter icons next to each step definition
    Given the server is running
    When I send a request
    Then I receive a response

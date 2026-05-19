Feature: scenario parameter

  Scenario Outline: response code
    Then the response code should be <info descr="null"><<info descr="null">code</info>></info>
    Examples:
      | <info descr="null">code</info> |
      | 200                            |

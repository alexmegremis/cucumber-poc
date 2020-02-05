Feature: the simple greeting can be retrieved

  Scenario: client makes call to GET /api/v1/hello
    When the client calls /api/v1/hello
    Then the client receives status code of 200
    And the response says Hello world

  Scenario Outline: client makes call to GET /api/v1/hello with a name
    When the client calls /api/v1/hello with nameFirst <nameFirst> and nameLast <nameLast>
    Then the client receives status code of 200
    And the response says Hello <nameFirst>
    And the person is with nameFirst <nameFirst> and nameLast <nameLast>

    Examples:
      | nameFirst | nameLast |
      | Alex      | Megremis |
      | Watson    | Basset   |

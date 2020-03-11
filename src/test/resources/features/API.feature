Feature: The Greeting API works

  Background:
    Given the DBs were reset
    And  the application DB has loaded testData/baseData

  Scenario: client makes call to GET /api/v1/hello
    When the client calls /api/v1/hello
    Then the client receives status code of 200
    And the response says Hello world

#  Scenario Outline: client makes call to GET /api/v1/hello with a name
#    When the client calls /api/v1/hello with nameFirst <nameFirst> and nameLast <nameLast>
#    Then the client receives status code of 200
#    And the response says Hello <nameFirst>
#    And the person is with nameFirst <nameFirst> and nameLast <nameLast>
#
#    Examples:
#      | nameFirst | nameLast |
#      | Alex      | Megremis |
#      | Watson    | Basset   |

#  Scenario Outline: client makes call to GET /api/v1/person/spring/find with a name
#    When the client calls /api/v1/person/spring/find with nameFirst <nameFirst> and nameLast <nameLast>
#    Then the client receives status code of 200
#    And the person is with nameFirst <nameFirst> and nameLast <nameLast>
#
#    Examples:
#      | id | nameFirst | nameLast |
#      | 1  | Alex      | Megremis |
#      | 2  | Zoe       | Megremis |
#      | 3  | Thomas    | Megremis |
#      | 4  | Watson    | Megremis |
#      | 5  | Olive     | Megremis |

  Scenario Outline: client makes call to GET /api/v1/person/hibernate/find with a name
    When the client calls <url> with nameFirst <nameFirst>, nameLast <nameLast>, and applicationName <applicationName>
    Then the client receives status code of 200
    And the person is with nameFirst <nameFirst> and nameLast <nameLast>
    Examples:
      | url                           | nameFirst | nameLast | applicationName |
      | /api/v1/person/spring/find    | Alex      | Megremis | CORE01          |
      | /api/v1/person/spring/find    | Zoe       | Megremis | CORE01          |
      | /api/v1/person/spring/find    | Thomas    | Megremis | CORE01          |
      | /api/v1/person/spring/find    | Watson    | Megremis | CORE01          |
      | /api/v1/person/spring/find    | Olive     | Megremis | CORE01          |
#      | /api/v1/person/spring/find    | Dimitrios | Megremis | CORE01          |
      | /api/v1/person/hibernate/find | Alex      | Megremis | CORE01          |
      | /api/v1/person/hibernate/find | Zoe       | Megremis | CORE01          |
      | /api/v1/person/hibernate/find | Thomas    | Megremis | CORE01          |
      | /api/v1/person/hibernate/find | Watson    | Megremis | CORE01          |
      | /api/v1/person/hibernate/find | Olive     | Megremis | CORE01          |
#      | /api/v1/person/hibernate/find | Dimitrios | Megremis | CORE01          |

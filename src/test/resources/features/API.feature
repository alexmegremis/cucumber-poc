Feature: The Greeting API works

  Background:
    Given The DB was reset
    And  The DB has loaded base

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
    When the client calls <url> with nameFirst <nameFirst> and nameLast <nameLast>
      | url                           |
      | /api/v1/person/spring/find    |
      | /api/v1/person/hibernate/find |
    Then the client receives status code of 200
    And the person is with nameFirst <nameFirst> and nameLast <nameLast>
    Examples:
      | id | nameFirst | nameLast |
      | 1  | Alex      | Megremis |
      | 2  | Zoe       | Megremis |
      | 3  | Thomas    | Megremis |
      | 4  | Watson    | Megremis |
      | 5  | Olive     | Megremis |
      | 6  | Dimitrios | Megremis |

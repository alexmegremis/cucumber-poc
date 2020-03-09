Feature: The DB is correctly populated

  Background:
    Given the DB was reset
    And  the DB has loaded base

  Scenario: There is a person recorded
    When The client gets a row count
    Then The row count is non zero

  Scenario Outline: There is a person recorded
#    Given The DB was reset
#    And  The DB has loaded base
    When The client gets row with ID <id>
    Then a row is returned
    And The person has nameFirst <nameFirst> and nameLast <nameLast>

    Examples:
      | id | nameFirst | nameLast |
      | 1  | Alex      | Megremis |
      | 2  | Zoe       | Megremis |
      | 3  | Thomas    | Megremis |
      | 4  | Watson    | Megremis |
      | 5  | Olive     | Megremis |

  Scenario Outline: Persons not in DB are not found
#    Given The DB was reset
#    And  The DB has loaded base
    When The client gets row with ID <id>
    Then a row is not returned

    Examples:
      | id | nameFirst | nameLast |
      | 6  | Dimitrios | Megremis |
      | 7  | Dimitrios | Megremis |
      | 17 | Dimitrios | Megremis |

  # https://thepracticaldeveloper.com/2017/08/03/microservices-end-to-end-tests-with-cucumber-and-spring-boot/
  # https://dzone.com/articles/a-guide-to-good-cucumber-practices

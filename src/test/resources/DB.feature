Feature: The DB is correctly populated

  Scenario: There is a person recorded
    Given The DB has loaded baseDB.sql and baseData.sql
    When The client gets a row count
    Then The row count is non zero

  Scenario Outline: There is a person recorded
    Given The DB has loaded baseDB.sql and baseData.sql
    When The client gets row with ID <id>
    Then a row is returned
    And The person has nameFirst <nameFirst> and nameLast <nameLast>

    Examples:
      | id | nameFirst | nameLast |
      | 1  | Alex      | Megremis |
      | 2  | Watson    | Basset   |
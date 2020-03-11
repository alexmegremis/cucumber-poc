Feature: The DB is correctly populated

  Background:
    Given the DBs were reset
    And  the application DB has loaded testData/baseData
    And global Person entities
      | id | nameFirst | nameLast | email                 |
      | 1  | Alex      | Megremis | alex@alexmegremis.com |
    And global Principal entities
      | id | name      | idPersonOwner | datetimeCreated | datetimeSuperseded |
      | 1  | amegremis | 1             | 2020-01-15      | 2019-01-31         |
      |    |           | 1             |                 |                    |

  Scenario: There is a person recorded
    Then local Person is found that looks like
      | id | nameFirst | nameLast |
      | 1  | Alex      | Megremis |
      | 3  | Thomas    | Megremis |
      |    | Olive     |          |
    And table PERSON has exactly 5 rows

  Scenario: There is a person recorded
    Then global Person is found that looks like
      | 0 |

  # https://thepracticaldeveloper.com/2017/08/03/microservices-end-to-end-tests-with-cucumber-and-spring-boot/
  # https://dzone.com/articles/a-guide-to-good-cucumber-practices

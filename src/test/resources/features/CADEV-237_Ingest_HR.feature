Feature: Ingest HR data into local DB.

  Background:
    Given HR ingestion
    And The DB was reset
    And Person entities
      | id | nameFirst | nameLast | email                      |
      | 1  | Alex      | Megremis | alex@alexmegremis.com      |
      | 2  | Zoe       | Nguyen   | zoe@alexmegremis.com       |
      | 3  | Thomas    | Megremis | thomas@alexmegremis.com    |
      | 4  | Watson    | Megremis | watson@alexmegremis.com    |
      | 5  | Olive     | Megremis | olive@alexmegremis.com     |
      | 6  | Dimitrios | Megremis | dimitrios@alexmegremis.com |
    And Principal entities
      | id | name         | idPersonOwner | datetimeCreated | datetimeSuperseded |
      | 1  | alexmegremis | 1             | 2019-01-20      |                    |
      | 2  | amegremis    | 1             | 2019-01-30      | 2019-01-31         |
      | 3  | admin        | 1             | 2019-02-09      |                    |
      | 4  | znguyen      | 2             | 2019-02-19      |                    |
      | 5  | zoemegremis  | 2             | 2019-03-01      |                    |
      | 6  | wmegremis    | 4             | 2019-03-11      |                    |


  Scenario: Ingest from default file.
    Given file default
    When ingestion is done
    Then tables PERSON,PRINCIPAL have over 20 rows

  Scenario: Ingest from custom file.
    Given file testData/HR_data_lite.csv
    When ingestion is done
    Then table PERSON has exactly 12 rows
    And table PRINCIPAL has exactly 5 rows

  Scenario: Ingested persons verified
    Given file testData/HR_data_lite.csv
    When < ingestion is done
    Then entity Person is created
#    Examples:
#      | id | nameFirst | nameLast | email                      |
#      | 1  | Alex      | Megremis | alex@alexmegremis.com      |
#      | 2  | Zoe       | Nguyen   | zoe@alexmegremis.com       |
#      | 3  | Thomas    | Megremis | thomas@alexmegremis.com    |
#      | 4  | Watson    | Megremis | watson@alexmegremis.com    |
#      | 5  | Olive     | Megremis | olive@alexmegremis.com     |
#      | 6  | Dimitrios | Megremis | dimitrios@alexmegremis.com |
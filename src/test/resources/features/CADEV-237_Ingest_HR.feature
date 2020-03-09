Feature: Ingest HR data into local DB.

  Background:
    Given HR ingestion
    And the DB was reset
    And global Person entities
      | id | nameFirst | nameLast | email                      |
      | 1  | Alex      | Megremis | alex@alexmegremis.com      |
      |    | Alex      |          |                            |
      | 6  | Zoe       | Nguyen   | zoe@alexmegremis.com       |
      |    |           | Nguyen   |                            |
    And global Principal entities
      | id | name         | idPersonOwner | datetimeCreated | datetimeSuperseded |
      | 1  | alexmegremis | 1             | 2019-01-20      |                    |
      | 2  | amegremis    | 1             | 2019-01-30      | 2019-01-31         |
      | 3  | admin        | 1             | 2019-02-09      |                    |
      | 4  | znguyen      | 2             | 2019-02-19      |                    |
      | 5  | zoemegremis  | 2             | 2019-03-01      |                    |
      | 6  | wmegremis    | 4             | 2019-03-11      |                    |


  Scenario: Ingest from default file.
    Given file default
    When ingestion is triggered
    Then ingestion is successful
    And tables PERSON,PRINCIPAL have over 5 rows

  Scenario: Ingest from custom file.
    Given file testData/HR_data_lite.csv
    When ingestion is triggered
    Then ingestion is successful
    And table PERSON has exactly 12 rows
    And table PRINCIPAL has exactly 5 rows

  Scenario: Ingested persons verified
    Given file testData/HR_data_lite.csv
    And the DB has loaded full
    When ingestion is triggered
    Then ingestion is successful
    And local Person is found that looks like
      | id | nameFirst | nameLast | email |
      |    | Thomas    |          |       |
      |    |           | Nguyen   |       |
    And global Person is found that looks like
      | 1  |
      | 2  |
      | 3  |
    And local Principal is found that looks like
      | id | name      | idPersonOwner | datetimeCreated | datetimeSuperseded |
      |    | amegremis | 1             | 2020-01-15      |                    |
      |    | cnguyen   | 2             | 2020-02-01      |                    |
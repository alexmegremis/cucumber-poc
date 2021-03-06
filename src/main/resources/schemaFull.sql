DROP TABLE IF EXISTS MAP_PRINCIPAL_REPO;
DROP TABLE IF EXISTS REPO;
DROP TABLE IF EXISTS PRINCIPAL;
DROP TABLE IF EXISTS PERSON;
DROP TABLE IF EXISTS APPLICATION;


CREATE TABLE PERSON (
    ID         INTEGER PRIMARY KEY,
    NAME_FIRST VARCHAR(20) NOT NULL,
    NAME_LAST  VARCHAR(20) NOT NULL
);

CREATE TABLE PRINCIPAL (
    ID                  INTEGER PRIMARY KEY,
    ID_PERSON_OWNER     INTEGER      NOT NULL,
    NAME                VARCHAR(256) NOT NULL,
    DATETIME_CREATED    DATETIME     NOT NULL,
    DATETIME_SUPERSEDED DATETIME,
    FOREIGN KEY (ID_PERSON_OWNER) REFERENCES PERSON(ID),
    UNIQUE (ID_PERSON_OWNER, NAME)
);

CREATE TABLE APPLICATION (
    ID         INTEGER PRIMARY KEY,
    NAME       VARCHAR(256) NOT NULL,
    IDENTIFIER VARCHAR(256) NOT NULL
);

CREATE TABLE REPO (
    ID                  INTEGER PRIMARY KEY,
    NAME                VARCHAR(256) NOT NULL,
    ID_APPLICATION      INTEGER      NOT NULL,
    DATETIME_CREATED    DATETIME     NOT NULL,
    DATETIME_SUPERSEDED DATETIME,
    FOREIGN KEY (ID_APPLICATION) REFERENCES APPLICATION(ID)
);

CREATE TABLE MAP_PRINCIPAL_REPO (
    ID                  INTEGER PRIMARY KEY,
    ID_PRINCIPAL        INTEGER      NOT NULL,
    ID_REPO             INTEGER      NOT NULL,
    PERMISSION          VARCHAR(256) NOT NULL,
    PERMISSION_BITS     INTEGER      NOT NULL,
    DATETIME_CREATED    DATETIME     NOT NULL,
    DATETIME_SUPERSEDED DATETIME
);

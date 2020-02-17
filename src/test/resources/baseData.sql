INSERT
INTO PERSON (ID, NAME_FIRST, NAME_LAST)
VALUES (1, 'Alex', 'Megremis'),
       (2, 'Zoe', 'Megremis'),
       (3, 'Thomas', 'Megremis'),
       (4, 'Watson', 'Megremis'),
       (5, 'Olive', 'Megremis');

INSERT
INTO PRINCIPAL(ID, ID_PERSON_OWNER, NAME, DATETIME_CREATED, DATETIME_MODIFIED)
VALUES (1, 1, 'amegremis', '2020-01-29', NULL),
       (2, 3, 'oktomas', '2020-02-01', NULL);
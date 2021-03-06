DROP TABLE IF EXISTS PERSONES;
DROP TABLE IF EXISTS ADRESS;
DROP TABLE IF EXISTS COMPANY;

BEGIN;

CREATE TABLE IF NOT EXISTS PERSONES (
  FIRSTNAME VARCHAR, LASTNAME VARCHAR, PHONE VARCHAR, CBVALUE VARCHAR
);

CREATE TABLE IF NOT EXISTS ADRESS (
  STREETNUM VARCHAR, STREETNAME VARCHAR, POSTALCODE VARCHAR, CITY VARCHAR
);

CREATE TABLE IF NOT EXISTS COMPANY (
  COMPANYNAME VARCHAR, NBPERSONNES INTEGER
);

COMMIT;
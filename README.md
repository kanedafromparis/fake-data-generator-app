OpenShift 3 Java EE Fake Data Generator Application
===================================================

This repository contains a testing application for creating data on *on OpenShift* It's purpose is to check PV behaviour

ENVIRONEMNT Variables are :

- ROOT_FILE :  directory to store date
- HSQLDB_SERVICE_HOST : directory to store hsqldb dtabase
- HSQLDB_DRIVER : The divres that might always be org.hsqldb.jdbcDriver
- HSQLDB_DATABASE : the name of the database
- HSQLDB_USER : the user name
- HSQLDB_PASSWORD : the password


`example :
export ROOT_FILE=/var/tmp/fake
export HSQLDB_SERVICE_HOST=/var/tmp/fake
export HSQLDB_DRIVER=org.hsqldb.jdbcDriver
export HSQLDB_DATABASE=FAKE
export HSQLDB_USER=SA
export HSQLDB_PASSWORD=

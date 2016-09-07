package io.github.kanedafromparis.ose.fakedatagen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by csabourdin on 07/09/16.
 */
public class CreateDataGenerator {

    public String create() {
        String databaseURL = "jdbc:hsqlhb://";
        databaseURL += System.getenv("HSQLDB_SERVICE_HOST");
        databaseURL += "/" + System.getenv("HSQLDB_DATABASE");
        String username = System.getenv("HSQLDB_USER");
        String password = System.getenv("HSQLDB_PASSWORD");
        String rootfile = System.getenv("ROOT_FILE");
        try {
            Connection connection = DriverManager.getConnection(databaseURL, username, password);

            String SQL_PERSONES = "CREATE TABLE IF NOT EXISTS PERSONES (FIRSTNAME VARCHAR, LASTNAME VARCHAR, PHONE VARCHAR,CBVALUE VARCHAR);";
            String SQL_ADRESS = "CREATE TABLE IF NOT EXISTS ADRESS (STREETNUM VARCHAR, STREETNAME VARCHAR, POSTALCODE VARCHAR, CITY VARCHAR);";
            String SQL_COMPANY = "CREATE TABLE IF NOT EXISTS COMPANY (COMPANYNAME VARCHAR, NBPERSONNES INTEGER);";


            connection.createStatement().execute(SQL_PERSONES);
            connection.createStatement().execute(SQL_ADRESS);
            connection.createStatement().execute(SQL_COMPANY);
            connection.close();
            return "Table created";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }


    }
}

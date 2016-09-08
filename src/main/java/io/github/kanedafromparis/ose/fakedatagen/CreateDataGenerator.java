package io.github.kanedafromparis.ose.fakedatagen;

import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Created by csabourdin on 07/09/16.
 */
public class CreateDataGenerator {

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String databaseURL = "jdbc:hsqldb://";
        databaseURL += StringUtils.defaultIfBlank(System.getenv("HSQLDB_SERVICE_HOST"), System.getProperty("user.dir")+"/target");
        databaseURL += "/" + StringUtils.defaultIfBlank(System.getenv("HSQLDB_DATABASE"),"FAKE;hsqldb.lock_file=false");
        String username = StringUtils.defaultIfBlank(System.getenv("HSQLDB_USER"),"SA");
        String password = StringUtils.defaultIfEmpty(System.getenv("HSQLDB_PASSWORD"),StringUtils.EMPTY);
        String driver = StringUtils.defaultIfBlank(System.getenv("HSQLDB_DRIVER"),"org.hsqldb.jdbcDriver");

        Class.forName(driver);
        Connection connection = DriverManager.getConnection(databaseURL, username, password);
        return connection;
    }

    public String create() {
        try {
            Connection connection = this.getConnection();
            createTable(connection);
            connection.close();
            return "Table created";

        } catch (SQLException e)

        {
            e.printStackTrace();
            return e.getLocalizedMessage();
        } catch (ClassNotFoundException e)
        {

            e.printStackTrace();
            return e.getLocalizedMessage();
        }


    }

    public void createTable(Connection connection) throws SQLException {

        String SQL_PERSONES = "CREATE TABLE IF NOT EXISTS PERSONES (FIRSTNAME VARCHAR(255), LASTNAME VARCHAR(255), PHONE VARCHAR(255),CBVALUE VARCHAR(255));";
        String SQL_ADRESS = "CREATE TABLE IF NOT EXISTS ADRESS (STREETNUM VARCHAR(255), STREETNAME VARCHAR(255), POSTALCODE VARCHAR(255), CITY VARCHAR(255));";
        String SQL_COMPANY = "CREATE TABLE IF NOT EXISTS COMPANY (COMPANYNAME VARCHAR(255), NBPERSONNES INTEGER);";


        connection.createStatement().execute(SQL_PERSONES);
        connection.createStatement().execute(SQL_ADRESS);
        connection.createStatement().execute(SQL_COMPANY);

    }
}

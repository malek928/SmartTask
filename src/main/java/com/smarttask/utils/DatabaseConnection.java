package com.smarttask.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();

        // 1. Look inside the "resources" folder for the secret file
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {

            if (input == null) {
                System.out.println("ERROR: Cannot find db.properties. Please create it in the resources folder!");
                return null;
            }

            // 2. Load the file
            props.load(input);

            // 3. Read the values from the file
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            // 4. Connect to MySQL!
            return DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Failed to connect to the database.");
        }
    }
}
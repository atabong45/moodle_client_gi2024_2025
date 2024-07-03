package com.example.moodle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static  String JDBC_URL = "jdbc:mysql://localhost:3307/moodleclient";
    public static  String JDBC_USERNAME = "root";
    public static String JDBC_PASSWORD = "juve5000";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);

        //07610b69998e79696d32049408004c44
    }
}

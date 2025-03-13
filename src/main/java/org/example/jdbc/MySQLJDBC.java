package org.example.jdbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MySQLJDBC {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connection successful!");

            Statement statement = connection.createStatement();

            String query = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println("Column 1: " + resultSet.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

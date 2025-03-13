package org.example.servlet;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect("login.html?error=driver_not_found");
            return;
        }

        String usernameOrEmail = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carManagement", "root", "root");
             PreparedStatement statement = connection.prepareStatement( "SELECT * FROM users WHERE username = ? OR email = ?")) {

            statement.setString(1, usernameOrEmail);
            statement.setString(2, usernameOrEmail);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    if (storedPassword.equals(password)) {
                        response.sendRedirect("DriveEasy.html");
                    } else {
                        response.sendRedirect("login.html?error=invalid");
                    }
                } else {
                    response.sendRedirect("login.html?error=invalid");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("login.html?error=error");
        }
    }
}

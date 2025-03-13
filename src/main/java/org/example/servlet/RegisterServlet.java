package org.example.servlet;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect("registration.html?error=driver_not_found");
            return;
        }

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");

        if (!password.equals(confirmPassword)) {
            response.sendRedirect("registration.html?error=passwords_do_not_match");
            return;
        }

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carManagement", "root", "root");

            String checkQuery = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, username);

            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                response.sendRedirect("registration.html?error=username_exists");
            } else {
                String insertQuery = "INSERT INTO users (username, email, phone, password) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, username);
                insertStatement.setString(2, email);
                insertStatement.setString(3, phone);
                insertStatement.setString(4, password);

                int rowsAffected = insertStatement.executeUpdate();
                if (rowsAffected > 0) {
                    response.sendRedirect("login.html");
                } else {
                    response.sendRedirect("registration.html?error=registration_failed");
                }
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("registration.html?error=error");  // Redirect to error page
        }
    }
}

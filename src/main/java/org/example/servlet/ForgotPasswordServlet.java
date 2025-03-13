package org.example.servlet;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ForgotPasswordServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            response.sendRedirect("ForgotPassword.html?error=mismatch");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect("ForgotPassword.html?error=driver_not_found");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carManagement", "root", "root");
             PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {

            selectStatement.setString(1, username);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE users SET password = ? WHERE username = ?")) {
                        updateStatement.setString(1, newPassword);
                        updateStatement.setString(2, username);

                        int rowsUpdated = updateStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            response.sendRedirect("login.html?success=password_reset");
                        } else {
                            response.sendRedirect("ForgotPassword.html?error=update_failed");
                        }
                    }
                } else {
                    response.sendRedirect("ForgotPassword.html?error=user_not_found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("ForgotPassword.html?error=error");
        }
    }
}

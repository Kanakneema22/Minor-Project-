package org.example.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class BookingServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect("Booking.html?error=driver_not_found");
            return;
        }

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String pickupDate = request.getParameter("pickupDate");
        String dropoffDate = request.getParameter("dropoffDate");
        String carType = request.getParameter("carType");
        String associatedCar = request.getParameter("associatedCar");

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carManagement", "root", "root")) {
            String insertQuery = "INSERT INTO bookings (name, email, phone,carType,associatedCar, pickupDate, dropoffDate) VALUES (?, ?, ?, ?, ?,?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, phone);
                preparedStatement.setString(4, carType);
                preparedStatement.setString(5, associatedCar);
                try {
                    java.sql.Date pickupSqlDate = Date.valueOf(pickupDate);
                    java.sql.Date dropoffSqlDate = Date.valueOf(dropoffDate);
                    preparedStatement.setDate(6, pickupSqlDate);
                    preparedStatement.setDate(7, dropoffSqlDate);
                } catch (IllegalArgumentException e) {
                    response.sendRedirect("Booking.html?error=Invalid date format.");
                    return;
                }
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    response.sendRedirect("payment.html");
                } else {
                    response.sendRedirect("Booking.html?error=booking_failed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("Booking.html?error=sql_error");
        }
    }
}

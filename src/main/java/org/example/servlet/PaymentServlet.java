package org.example.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.*;

public class PaymentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect("payment.html?error=driver_not_found");
            return;
        }

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String zip = request.getParameter("zip");

        String nameOnCard = request.getParameter("nameOnCard");
        String cardNumber = request.getParameter("cardNumber");
        String expMonth = request.getParameter("expMonth");
        String expYear = request.getParameter("expYear");
        String cvv = request.getParameter("cvv");

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carManagement", "root", "root")) {
            String insertQuery = "INSERT INTO payment (fullName, email, address, city, state, zip, nameOnCard, cardNumber, expMonth, expYear, cvv) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

                preparedStatement.setString(1, fullName);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, address);
                preparedStatement.setString(4, city);
                preparedStatement.setString(5, state);
                preparedStatement.setString(6, zip);

                preparedStatement.setString(7, nameOnCard);
                preparedStatement.setString(8, cardNumber);
                preparedStatement.setString(9, expMonth);
                preparedStatement.setString(10, expYear);
                preparedStatement.setString(11, cvv);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    response.sendRedirect("success.html?name=" + URLEncoder.encode(fullName, "UTF-8"));

                } else {
                    response.sendRedirect("payment.html?error=booking_failed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("payment.html?error=sql_error");
        }
    }
}

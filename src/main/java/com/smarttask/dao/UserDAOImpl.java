package com.smarttask.dao;

import com.smarttask.model.User;
import com.smarttask.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl {

    // ROLE 1: Register a new user into the database
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // We replace the "?" in the SQL string with the actual username and password
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Returns true if the user was saved successfully

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ROLE 2: Find a user by their username (Used for Login)
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            // If we find a match in the database, we package it into our Java 'User' object
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Returns null if the user doesn't exist in the database
    }
}
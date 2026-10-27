package com.mcq.dao;

import com.mcq.database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AdminDAO.java
 * Data Access Object for Admin authentication operations.
 */
public class AdminDAO {

    /**
     * Validates admin login credentials against the MySQL 'admin' table.
     * Uses PreparedStatement to prevent SQL Injection attacks.
     * 
     * @param username Provided username
     * @param password Provided password
     * @return true if credentials match a record, false otherwise
     */
    public boolean validateAdmin(String username, String password) {
        String query = "SELECT id FROM admin WHERE username = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                // If a matching record is found, return true
                return rs.next();
            }
            
        } catch (SQLException e) {
            System.err.println("[AdminDAO Error] Failed to validate admin: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
